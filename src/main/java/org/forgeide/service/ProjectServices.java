package org.forgeide.service;

import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.forgeide.controller.ProjectController;
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
   @Inject ProjectController controller;

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

      controller.createProject(p);

      return p;
   }
}