package org.forgeide.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Defines a service that may be activated for a specific project template
 *
 * @author Shane Bryzak
 *
 */
@Entity
public class TemplateService implements Serializable
{
   private static final long serialVersionUID = -4464418469755806741L;

   @Id @GeneratedValue
   private Long id;

   @ManyToOne @JoinColumn(name = "PROJECT_TEMPLATE_ID")
   private ProjectTemplate template;

   private String code;

   private String name;

   private String description;

   private String forgeCommand;

   private int steps;

   @JsonIgnore
   public Long getId()
   {
      return id;
   }

   public void setId(Long id)
   {
      this.id = id;
   }

   @JsonIgnore
   public ProjectTemplate getTemplate()
   {
      return template;
   }

   public void setTemplate(ProjectTemplate template)
   {
      this.template = template;
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

   public String getForgeCommand()
   {
      return forgeCommand;
   }

   public void setForgeCommand(String forgeCommand)
   {
      this.forgeCommand = forgeCommand;
   }

   public int getSteps()
   {
      return steps;
   }

   public void setSteps(int steps)
   {
      this.steps = steps;
   }
}
