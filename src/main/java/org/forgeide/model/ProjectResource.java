package org.forgeide.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Represents a single project resource, such as a source file
 *
 * @author Shane Bryzak
 */
@Entity
public class ProjectResource implements Serializable
{
   private static final long serialVersionUID = -4146308990787564792L;

   public enum ResourceType {DIRECTORY, FILE};

   @Id
   @GeneratedValue
   private Long id;

   @ManyToOne
   private Project project;

   @ManyToOne
   private ProjectResource parent;

   private String name;

   private ResourceType resourceType;

   public Long getId()
   {
      return id;
   }

   public void setId(Long id)
   {
      this.id = id;
   }

   public Project getProject()
   {
      return project;
   }

   public void setProject(Project project)
   {
      this.project = project;
   }

   public ProjectResource getParent()
   {
      return parent;
   }

   public void setParent(ProjectResource parent)
   {
      this.parent = parent;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public ResourceType getResourceType()
   {
      return resourceType;
   }

   public void setResourceType(ResourceType resourceType)
   {
      this.resourceType = resourceType;
   }

}
