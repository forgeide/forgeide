package org.forgeide.controller;

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

import org.forgeide.events.NewProjectEvent;
import org.forgeide.events.NewResourceEvent;
import org.forgeide.events.SessionCreatedEvent;
import org.forgeide.forge.UIRuntimeImpl;
import org.forgeide.forge.metadata.ResultMetadata;
import org.forgeide.forge.ui.IDEUIContext;
import org.forgeide.model.Project;
import org.forgeide.model.ProjectAccess;
import org.forgeide.model.ProjectAccess.AccessLevel;
import org.forgeide.model.ProjectResource;
import org.forgeide.model.ProjectResource.ResourceType;
import org.forgeide.model.Project_;
import org.forgeide.model.ResourceContent;
import org.forgeide.qualifiers.Forge;
import org.forgeide.security.annotations.LoggedIn;
import org.forgeide.service.ProjectServices.ProjectParams;
import org.forgeide.service.websockets.Message;
import org.jboss.forge.addon.ui.command.CommandFactory;
import org.jboss.forge.addon.ui.command.UICommand;
import org.jboss.forge.addon.ui.controller.CommandController;
import org.jboss.forge.addon.ui.controller.CommandControllerFactory;
import org.jboss.forge.addon.ui.result.Failed;
import org.jboss.forge.addon.ui.result.Result;
import org.picketlink.Identity;

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

   @Inject
   private Identity identity;

   @Inject
   @Forge
   Instance<CommandFactory> commandFactory;

   @Inject
   @Forge
   Instance<CommandControllerFactory> controllerFactory;

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
      pa.setUserId(identity.getAccount().getId());

      em.persist(pa);

      if ("javaee".equals(params.getTemplate())) 
      {
         IDEUIContext context = new IDEUIContext();
         UICommand cmd = commandFactory.get().getCommandByName(context, COMMAND_PROJECT_NEW);

         if (cmd == null) 
         {
            throw new RuntimeException("Could not locate [Project: New] Forge command");
         }

         CommandController controller = controllerFactory.get().createSingleController(
                  context, new UIRuntimeImpl(), cmd);
         controller.initialize();

         controller.setValueFor("named", project.getName());
         controller.setValueFor("type", "from-archetype");
         controller.setValueFor("archetypeGroupId", "org.jboss.tools.archetypes");
         controller.setValueFor("archetypeArtifactId", "jboss-forge-html5");
         controller.setValueFor("archetypeVersion", "1.0.0-SNAPSHOT");

         //controller.setValueFor("targetLocation", new ResourcePath());

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
               rm.setPassed(true);
               rm.setMessage(result.getMessage());
            }
         }
         catch (Exception ex)
         {
            rm.setPassed(false);
            rm.setException(ex.getMessage());
         }
      }

      newProjectEvent.fire(new NewProjectEvent(project));
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

   public void sessionCreated(@Observes SessionCreatedEvent event)
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
   }

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
      if (!identity.isLoggedIn()) {
         return Collections.emptyList();
      }

      EntityManager em = entityManager.get();

      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaQuery<ProjectAccess> cq = cb.createQuery(ProjectAccess.class);
      Root<ProjectAccess> from = cq.from(ProjectAccess.class);
      Join<ProjectAccess,Project> project = from.join("project");

      List<Predicate> predicates = new ArrayList<Predicate>();

      predicates.add(cb.equal(from.get("userId"), identity.getAccount().getId()));

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
}
