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
import org.forgeide.model.ProjectResource;
import org.forgeide.model.ProjectResource.ResourceType;

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

   @POST
   @Path("/newfolder")
   @Consumes("application/json")
   @Produces(MediaType.APPLICATION_JSON)
   public ProjectResource createProjectFolder(Map<String,String> properties)
   {
      ProjectResource r = new ProjectResource();
      r.setName(properties.get("name"));
      r.setResourceType(ResourceType.DIRECTORY);

      if (properties.containsKey("projectId"))
      {
         Long projectId = Long.valueOf(properties.get("projectId"));
         r.setProject(controller.lookupProject(projectId));
      }

      if (properties.containsKey("parentResourceId"))
      {
         Long resourceId = Long.valueOf(properties.get("parentResourceId"));
         r.setParent(controller.lookupResource(resourceId));
      }

      controller.createResource(r);
      return r;
   }

   @POST
   @Path("/newclass")
   @Consumes("application/json")
   @Produces(MediaType.APPLICATION_JSON)
   public ProjectResource createProjectClass(Map<String,String> properties)
   {
      ProjectResource r = new ProjectResource();
      r.setName(properties.get("name") + ".java");
      r.setResourceType(ResourceType.FILE);

      Project p = controller.lookupProject(Long.valueOf(properties.get("projectId")));
      r.setProject(p);

      String folder = properties.get("folder");
      String pkg = properties.get("package");

      ProjectResource parentDir = controller.createDirStructure(p, folder);
      ProjectResource parentPkg = controller.createPackage(p, parentDir, pkg);
      r.setParent(parentPkg);

      controller.createResource(r);
      return r;
   }
}