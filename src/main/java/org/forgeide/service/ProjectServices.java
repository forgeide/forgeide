package org.forgeide.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.forgeide.controller.ProjectController;
import org.forgeide.controller.ResourceController;
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
   @Inject ProjectController projectController;
   @Inject ResourceController resourceController;

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

      projectController.createProject(p);

      return p;
   }

   @POST
   @Path("/newfolder")
   @Consumes("application/json")
   @Produces(MediaType.APPLICATION_JSON)
   public ProjectResource[] createProjectFolder(Map<String,String> properties)
   {
      String[] parts = properties.get("name").split("/");
      List<ProjectResource> resources = new ArrayList<ProjectResource>();

      ProjectResource parent = null;

      if (properties.containsKey("parentResourceId"))
      {
         Long resourceId = Long.valueOf(properties.get("parentResourceId"));
         parent = projectController.lookupResource(resourceId);
      }

      for (String part : parts) 
      {
         ProjectResource r = new ProjectResource();
         r.setName(part);
         r.setResourceType(ResourceType.DIRECTORY);
         r.setParent(parent);

         if (properties.containsKey("projectId"))
         {
            Long projectId = Long.valueOf(properties.get("projectId"));
            r.setProject(projectController.lookupProject(projectId));
         }

         projectController.createResource(r);
         parent = r;
      }

      return resources.toArray(new ProjectResource[resources.size()]);
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

      Project p = projectController.lookupProject(Long.valueOf(properties.get("projectId")));
      r.setProject(p);

      String folder = properties.get("folder");
      String pkg = properties.get("package");

      ProjectResource parentDir = projectController.createDirStructure(p, folder);
      ProjectResource parentPkg = projectController.createPackage(p, parentDir, pkg);
      r.setParent(parentPkg);

      projectController.createResource(r);
      return r;
   }
}