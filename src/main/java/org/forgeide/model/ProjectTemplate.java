package org.forgeide.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Defines an individual project template, based on a specific Maven archetype
 *
 * @author Shane Bryzak
 *
 */
@Entity
public class ProjectTemplate implements Serializable
{
   private static final long serialVersionUID = -7254198210498047638L;

   @Id @GeneratedValue
   private Long id;

   private String code;

   private String name;

   private String description;

   private String archetypeGroupId;

   private String archetypeArtifactId;

   private String archetypeVersion;

   public Long getId()
   {
      return id;
   }

   public void setId(Long id)
   {
      this.id = id;
   }

   public String getCode()
   {
      return code;
   }

   public void setCode(String code)
   {
      this.code = code;
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

   public String getArchetypeGroupId()
   {
      return archetypeGroupId;
   }

   public void setArchetypeGroupId(String archetypeGroupId)
   {
      this.archetypeGroupId = archetypeGroupId;
   }

   public String getArchetypeArtifactId()
   {
      return archetypeArtifactId;
   }

   public void setArchetypeArtifactId(String archetypeArtifactId)
   {
      this.archetypeArtifactId = archetypeArtifactId;
   }

   public String getArchetypeVersion()
   {
      return archetypeVersion;
   }

   public void setArchetypeVersion(String archetypeVersion)
   {
      this.archetypeVersion = archetypeVersion;
   }

}
