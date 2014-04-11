package org.forgeide.controller;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.forgeide.events.NewProjectEvent;
import org.forgeide.events.SessionCreatedEvent;
import org.forgeide.model.Project;
import org.forgeide.model.ProjectAccess;
import org.forgeide.model.ProjectAccess.AccessLevel;
import org.forgeide.service.Message;
import org.picketlink.Identity;

/**
 * 
 * @author Shane Bryzak
 *
 */
@ApplicationScoped
public class ProjectController
{
   @Inject
   private Instance<EntityManager> entityManager;

   @Inject Event<NewProjectEvent> newProjectEvent;

   @Inject
   private Identity identity;

   //@LoggedIn
   public void createProject(Project project)
   {
      EntityManager em = entityManager.get();
      em.persist(project);

      ProjectAccess pa = new ProjectAccess();
      pa.setProject(project);
      pa.setAccessLevel(AccessLevel.OWNER);
      pa.setOpen(true);

      //IdentityType id = em.<IdentityType>createQuery("select i from IdentityType i where i.id = :id", IdentityType.class)
//               .setParameter("id", identity.getAccount().getId()).getSingleResult();
  //    pa.setUser(id);

      em.persist(pa);

      newProjectEvent.fire(new NewProjectEvent(project));
   }

   public void sessionCreated(@Observes SessionCreatedEvent event)
   {
      List<Project> projects = listOpenProjects();

      Message m = new Message(Message.CAT_PROJECT, Message.OP_PROJECT_LIST);
      m.setPayloadValue("projects", projects);

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
}
