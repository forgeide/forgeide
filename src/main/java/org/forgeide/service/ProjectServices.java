package org.forgeide.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.forgeide.events.NewProjectEvent;
import org.forgeide.model.Project;
import org.forgeide.model.ProjectAccess;

/**
 * Project-related RESTful services
 *
 * @author Shane Bryzak
 */
@Path("/projects")
@Stateless
public class ProjectServices
{
   @Inject EntityManager entityManager;

   @Inject Event<NewProjectEvent> newProjectEvent;

   @POST
   @Path("/create")
   @Consumes("application/json")
   @Produces(MediaType.APPLICATION_JSON)
   public Project createProject(Map<String,String> properties)
   {
      Project p = new Project();
      p.setName(properties.get("name"));
      p.setTopLevelPackage(properties.get("topLevelPackage"));
      p.setVersion(properties.get("version"));
      p.setFinalName(properties.get("finalName"));
      //p.setType(type);

      entityManager.persist(p);

      newProjectEvent.fire(new NewProjectEvent(p));

      return p;
   }
}