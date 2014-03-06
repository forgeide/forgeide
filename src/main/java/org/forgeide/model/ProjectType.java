package org.forgeide.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Represents the project type
 * 
 * @author Shane Bryzak
 */
@Entity
public class ProjectType implements Serializable
{
   private static final long serialVersionUID = -7274721711808450469L;

   @Id
   @GeneratedValue
   private Long id;
   private String description;

   public Long getId()
   {
      return id;
   }

   public void setId(Long id)
   {
      this.id = id;
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
