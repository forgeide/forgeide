package org.forgeide.controller;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.forgeide.events.SessionCreatedEvent;
import org.forgeide.model.Project;
import org.forgeide.model.ProjectAccess;
import org.forgeide.service.Message;

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
