package org.forgeide.service;

import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.forgeide.model.Project;

/**
 * Project-related RESTful services
 *
 * @author Shane Bryzak
 */
@Path("/projects")
@Stateless
public class ProjectServices
{
   public static final String MESSAGE_CAT_PROJECT = "PROJECT";

   public static final String PROJECT_OP_NEW = "NEW";

   @Inject EntityManager entityManager;

   @Inject SessionRegistry registry;

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

      Message m = new Message(MESSAGE_CAT_PROJECT, PROJECT_OP_NEW);

      registry.transmitProjectMessage(p.getId(), m);

      return p;
   }
}