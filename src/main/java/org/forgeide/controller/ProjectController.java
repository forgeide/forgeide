package org.forgeide.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.forgeide.events.NewProjectEvent;
import org.forgeide.events.NewResourceEvent;
import org.forgeide.forge.UIRuntimeImpl;
import org.forgeide.forge.metadata.ResultMetadata;
import org.forgeide.forge.ui.IDEUIContext;
import org.forgeide.model.GitHubAuthorization;
import org.forgeide.model.Project;
import org.forgeide.model.ProjectAccess;
import org.forgeide.model.ProjectAccess.AccessLevel;
import org.forgeide.model.ProjectResource;
import org.forgeide.model.ProjectResource.ResourceType;
import org.forgeide.model.ProjectTemplate;
import org.forgeide.model.Project_;
import org.forgeide.model.ResourceContent;
import org.forgeide.model.ServiceParameter;
import org.forgeide.model.TemplateService;
import org.forgeide.qualifiers.Configuration;
import org.forgeide.qualifiers.Forge;
import org.forgeide.security.annotations.LoggedIn;
import org.forgeide.service.ProjectServices.ProjectParams;
import org.jboss.forge.addon.projects.Projects;
import org.jboss.forge.addon.ui.command.CommandFactory;
import org.jboss.forge.addon.ui.command.UICommand;
import org.jboss.forge.addon.ui.context.UISelection;
import org.jboss.forge.addon.ui.controller.CommandControllerFactory;
import org.jboss.forge.addon.ui.controller.SingleCommandController;
import org.jboss.forge.addon.ui.controller.WizardCommandController;
import org.jboss.forge.addon.ui.output.UIMessage;
import org.jboss.forge.addon.ui.result.Failed;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.util.Selections;
import org.jboss.forge.addon.ui.wizard.UIWizard;
import org.picketlink.Identity;
import org.xwidgets.websocket.Message;
import org.xwidgets.websocket.SessionRegistry;

/**
 * Performs persistence operations for projects
 *
 * @author Shane Bryzak
 */
@ApplicationScoped
public class ProjectController
{
   private static final String COMMAND_PROJECT_NEW = "Project: New";

   @Inject
   private Instance<EntityManager> entityManager;

   @Inject Event<NewProjectEvent> newProjectEvent;

   @Inject Event<NewResourceEvent> newResourceEvent;

   @Inject @Configuration(key = "project.rootdir", defaultValue = "/tmp")
   private String projectRootDir;

   @Inject
   private Instance<Identity> identityInstance;

   @Inject
   @Forge
   Instance<CommandFactory> commandFactory;

   @Inject
   @Forge
   Instance<CommandControllerFactory> controllerFactory;

   @Inject SessionRegistry sessionRegistry;

   private class NewProjectContext extends IDEUIContext
   {
      private IDEUIContext wrapped;

      public NewProjectContext(IDEUIContext wrapped)
      {
         this.wrapped = wrapped;
      }

      @Override
      public <SELECTIONTYPE> UISelection<SELECTIONTYPE> getInitialSelection()
      {
         return wrapped.getSelection();
      }
   }

   @LoggedIn
   public void createProject(Project project, ProjectParams params)
       throws Exception
   {
      EntityManager em = entityManager.get();
      em.persist(project);

      ProjectAccess pa = new ProjectAccess();
      pa.setProject(project);
      pa.setAccessLevel(AccessLevel.OWNER);
      pa.setOpen(true);
      pa.setUserId(identityInstance.get().getAccount().getId());

      em.persist(pa);

      ProjectTemplate template = entityManager.get().createQuery(
               "select t from ProjectTemplate t where t.code = :code", ProjectTemplate.class)
               .setParameter("code", params.getTemplate())
               .getSingleResult();

      IDEUIContext context = new IDEUIContext();
      UICommand cmd = commandFactory.get().getNewCommandByName(context, COMMAND_PROJECT_NEW);

      if (cmd == null) 
      {
         throw new RuntimeException("Could not locate [Project: New] Forge command");
      }

      WizardCommandController controller = controllerFactory.get().createWizardController(
               context, new UIRuntimeImpl(), (UIWizard) cmd);
      controller.initialize();

      File rootDir = getProjectRootDir(project.getName());
      controller.setValueFor("targetLocation", rootDir);

      controller.setValueFor("named", project.getName());
      controller.setValueFor("type", "From Archetype");

      if (!controller.isValid())
      {
         List<UIMessage> messages = controller.validate();
         StringBuilder sb = new StringBuilder();
         for (UIMessage msg : messages)
         {
            if (sb.length() > 0) {
               sb.append(", ");
            }
            sb.append("[");
            sb.append(msg.getDescription());
            sb.append("]");
         }
         throw new RuntimeException(
                  "Unable to create project, validation error/s in New Project wizard - " +
                  sb.toString());
      }

      controller.next().initialize();

      controller.setValueFor("archetypeGroupId", template.getArchetypeGroupId());
      controller.setValueFor("archetypeArtifactId", template.getArchetypeArtifactId());
      controller.setValueFor("archetypeVersion", template.getArchetypeVersion());

      ResultMetadata rm = new ResultMetadata();
      try
      {
         Result result = controller.execute();
         if (result instanceof Failed) {
            rm.setPassed(false);
            rm.setMessage(result.getMessage());
            if (((Failed) result).getException() != null) 
            {
               rm.setException(((Failed) result).getException().getMessage());
            }
         }
         else
         {
            // Select the newly created project in the context
            context = new NewProjectContext(context);

            // Run each of the commands for the selected services
            for (String serviceCode : params.getServices()) {
               TemplateService service = entityManager.get().createQuery(
                        "select s from TemplateService s where s.template = :template and s.code = :code",
                        TemplateService.class)
                        .setParameter("template", template)
                        .setParameter("code", serviceCode)
                        .getSingleResult();
               executeService(service, context);
            }

            File projectDir = new File(rootDir, project.getName());

            // Create and commit the git repo
            Git.init()
              .setDirectory(projectDir)
              .call();

            Repository localRepo = FileRepositoryBuilder.create(
                     new File(projectDir.getAbsolutePath(), ".git"));

            Git git = new Git(localRepo);

            // run the add
            git.add()
                    .addFilepattern(".")
                    .call();

            // and then commit the changes
            git.commit()
                    .setMessage("initial commit")
                    .call();

            // Now we push the changes to GitHub 
            // First lookup the user's GitHub authorization record
            GitHubAuthorization auth = em.find(GitHubAuthorization.class, 
                     identityInstance.get().getAccount().getId());

            if (auth != null)
            {
               // Use the GitHub API to create the repository
               RepositoryService service = new RepositoryService();
               service.getClient().setOAuth2Token(auth.getAccessToken());
               org.eclipse.egit.github.core.Repository remoteRepo = new org.eclipse.egit.github.core.Repository();
               remoteRepo.setName(project.getName());
               remoteRepo = service.createRepository(remoteRepo);

               // After creating the remote repository, we want to push our local repo to it
               // We start by creating a remote configuration
               final StoredConfig config = localRepo.getConfig();
               RemoteConfig remoteConfig = new RemoteConfig(config, "origin");
               URIish uri = new URIish(remoteRepo.getHtmlUrl());
               remoteConfig.addURI(uri);
               remoteConfig.update(config);
               config.save();

               // Create the credentials provider for jGit
               CredentialsProvider cp = new UsernamePasswordCredentialsProvider(
                        auth.getAccessToken(), "");

               // Push the repo
               RefSpec spec = new RefSpec("refs/heads/master:refs/heads/x");
               git.push()
                  .setCredentialsProvider(cp)
                  .setRemote("origin")
                  .setRefSpecs(spec)
                  .call();
            }

            localRepo.close();

            rm.setPassed(true);
            //rm.setMessage(result.getMessage());
         }
      }
      catch (Exception ex)
      {
         rm.setPassed(false);
         rm.setException(ex.getMessage());
         throw new RuntimeException(ex);
      }

      newProjectEvent.fire(new NewProjectEvent(project));
   }

   private void executeService(TemplateService service, IDEUIContext context)
            throws Exception
   {
      UICommand cmd = commandFactory.get().getNewCommandByName(context, service.getForgeCommand());

      if (cmd == null) 
      {
         throw new RuntimeException("Could not locate [" + service.getForgeCommand() + 
                  "] Forge command");
      }

      if (service.getSteps() > 1)
      {
         WizardCommandController controller = controllerFactory.get().createWizardController(
                  context, new UIRuntimeImpl(), (UIWizard) cmd);
         controller.initialize();
      } 
      else
      {
         SingleCommandController controller = controllerFactory.get().createSingleController(
                  context, new UIRuntimeImpl(), cmd);
         controller.initialize();

         List<ServiceParameter> params = entityManager.get().createQuery(
                  "select p from ServiceParameter p where p.service = :service",
                  ServiceParameter.class)
                  .setParameter("service", service)
                  .getResultList();
         for (ServiceParameter p : params)
         {
            controller.setValueFor(p.getParamName(), p.getParamValue());
         }

         Result result = controller.execute();
      }
   }

   private File getProjectRootDir(String projectName)
   {
      File rootDir = new File(projectRootDir);
      if (!rootDir.exists())
      {
         rootDir.mkdirs();
      }

      return rootDir;
   }

   @LoggedIn
   public void createResource(ProjectResource resource)
   {
      EntityManager em = entityManager.get();
      em.persist(resource);

      ResourceContent content = new ResourceContent();
      content.setResource(resource);
      content.setContent(new byte[0]);

      em.persist(content);

      newResourceEvent.fire(new NewResourceEvent(resource));
   }

   public Project lookupProject(Long id)
   {
      EntityManager em = entityManager.get();
      return em.find(Project.class, id);
   }

   public ProjectResource lookupResource(Long id)
   {
      EntityManager em = entityManager.get();
      return em.find(ProjectResource.class, id);
   }

   public ProjectResource lookupResourceByName(Project project, ProjectResource parent, String name)
   {
      EntityManager em = entityManager.get();

      TypedQuery<ProjectResource> q = em.createQuery(
               "select r from ProjectResource r where r.project = :project and r.parent = :parent and r.name = :name",
               ProjectResource.class);
      q.setParameter("project", project);
      q.setParameter("parent", parent);
      q.setParameter("name", name);

      try
      {
        return q.getSingleResult();
      }
      catch (NoResultException ex)
      {
         return null;
      }
   }

   // TODO refactor this so it's a polled request
   /**public void sessionCreated(@Observes SessionCreatedEvent event)
   {
      List<Project> projects = listOpenProjects();

      List<ProjectResource> resources = new ArrayList<ProjectResource>();
      for (Project p : projects)
      {
         resources.addAll(listResources(p));
      }

      Message m = new Message(Message.CAT_PROJECT, Message.OP_PROJECT_LIST);
      m.setPayloadValue("projects", projects);
      m.setPayloadValue("resources", resources);

      event.getSession().getAsyncRemote().sendObject(m);
   }*/

   public List<Project> listOpenProjects()
   {
      TypedQuery<ProjectAccess> q = entityManager.get().<ProjectAccess>createQuery("select p from ProjectAccess p where p.open = true", 
               ProjectAccess.class);

      List<Project> projects = new ArrayList<Project>();
      for (ProjectAccess a : q.getResultList())
      {
         projects.add(a.getProject());
      }

      return projects;
   }

   public List<Project> listProjects(String searchTerm)
   {
      if (!identityInstance.get().isLoggedIn()) {
         return Collections.emptyList();
      }

      EntityManager em = entityManager.get();

      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaQuery<ProjectAccess> cq = cb.createQuery(ProjectAccess.class);
      Root<ProjectAccess> from = cq.from(ProjectAccess.class);
      Join<ProjectAccess,Project> project = from.join("project");

      List<Predicate> predicates = new ArrayList<Predicate>();

      predicates.add(cb.equal(from.get("userId"), identityInstance.get().getAccount().getId()));

      if (searchTerm != null && !"".equals(searchTerm))
      {
         predicates.add(cb.like(cb.lower(project.get(Project_.name)), "%" + searchTerm.toLowerCase() + "%"));
      }

      cq.where(predicates.toArray(new Predicate[predicates.size()]));

      TypedQuery<ProjectAccess> q = em.createQuery(cq);

      List<Project> projects = new ArrayList<Project>();
      for (ProjectAccess a : q.getResultList())
      {
         projects.add(a.getProject());
      }

      return projects;
   }

   public List<ProjectResource> listResources(Project project)
   {
      TypedQuery<ProjectResource> q = entityManager.get().<ProjectResource>createQuery("select r from ProjectResource r where r.project = :project", 
               ProjectResource.class);
      q.setParameter("project", project);
      return q.getResultList();
   }

   public ProjectResource createDirStructure(Project project, String directory)
   {
      String[] parts = directory.split(ProjectResource.PATH_SEPARATOR);

      ProjectResource parent = null;
      for (int i = 0; i < parts.length; i++)
      {
         // If the first part is equal to the project name, ignore it
         if (i == 0 && parts[i].equals(project.getName()))
         {
            continue;
         }

         ProjectResource r = lookupResourceByName(project, parent, parts[i]);
         // If the directory resource doesn't exist, create it
         if (r == null)
         {
            r = new ProjectResource();
            r.setProject(project);
            r.setResourceType(ResourceType.DIRECTORY);
            r.setName(parts[i]);
            r.setParent(parent);
            createResource(r);
         }

         parent = r;
      }

      return parent;
   }

   public ProjectResource createPackage(Project project, ProjectResource folder, String pkgName)
   {

      return null;
   }

   public void newProjectEventObserver(@Observes NewProjectEvent event)
   {
      Message m = new Message("project.new");
      m.setPayloadValue("project", event.getProject());
      sessionRegistry.broadcastMessage(m);
   }
}
