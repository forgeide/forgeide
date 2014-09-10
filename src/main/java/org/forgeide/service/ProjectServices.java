package org.forgeide.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.forgeide.controller.ProjectController;
import org.forgeide.model.Project;
import org.forgeide.model.ProjectResource;
import org.forgeide.model.ProjectResource.ResourceType;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

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
   //@Inject ResourceController resourceController;

   @JsonTypeInfo(use = Id.NONE)
   public static class ProjectParams
   {
      private String name;
      private String description;
      private String template;
      private String[] services;

      public String getTemplate()
      {
         return template;
      }

      public void setTemplate(String template)
      {
         this.template = template;
      }

      public String[] getServices()
      {
         return services;
      }

      public void setServices(String[] services)
      {
         this.services = services;
      }

      public String getName() 
      {
         return name;
      }

      public void setName(String name)
      {
         this.name = name;
      }

      public String getDescription()
      {
         return description;
      }

      public void setDescription(String description)
      {
         this.description = description;
      }
   }

   @POST
   @Consumes("application/json")
   @Produces(MediaType.APPLICATION_JSON)
   public Project createProject(ProjectParams params)
   {
      Project p = new Project();
      p.setName(params.getName());
      p.setDescription(params.getDescription());

      //p.setTopLevelPackage(properties.get("topLevelPackage"));
      //p.setVersion(properties.get("version"));
      //p.setFinalName(properties.get("finalName"));
      //p.setType(type);

      projectController.createProject(p);

      return p;
   }

   @GET
   @Produces(MediaType.APPLICATION_JSON)
   public List<Project> listProjects(@QueryParam("searchTerm") String searchTerm)
   {
      return projectController.listProjects(searchTerm);
   }

   @GET
   @Path("/{projectId}")
   @Produces(MediaType.APPLICATION_JSON)
   public Project getProject(@PathParam("projectId") String projectId)
   {
      return projectController.lookupProject(Long.valueOf(projectId));
   }

   @POST
   @Path("/foo/newfolder")
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
   @Path("/foo/newclass")
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