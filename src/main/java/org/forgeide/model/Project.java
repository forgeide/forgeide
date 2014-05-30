package org.forgeide.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Represents a project
 *
 * @author Shane Bryzak
 */
@Entity
public class Project implements Serializable
{
   private static final long serialVersionUID = 5410491316071577992L;

   @Id
   @GeneratedValue
   private Long id;

   private String name;

   private String topLevelPackage;

   private String version;

   private String finalName;

   @ManyToOne
   private ProjectType type;

   public Long getId()
   {
      return id;
   }

   public void setId(Long id)
   {
      this.id = id;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public String getTopLevelPackage()
   {
      return topLevelPackage;
   }

   public void setTopLevelPackage(String topLevelPackage)
   {
      this.topLevelPackage = topLevelPackage;
   }

   public String getVersion()
   {
      return version;
   }

   public void setVersion(String version)
   {
      this.version = version;
   }

   public String getFinalName()
   {
      return finalName;
   }

   public void setFinalName(String finalName)
   {
      this.finalName = finalName;
   }

   public ProjectType getType()
   {
      return type;
   }

   public void setType(ProjectType type)
   {
      this.type = type;
   }

}
