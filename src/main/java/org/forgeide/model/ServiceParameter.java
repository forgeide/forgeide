package org.forgeide.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Defines parameter values for template services
 *
 * @author Shane Bryzak
 *
 */
@Entity
public class ServiceParameter implements Serializable
{
   private static final long serialVersionUID = -7721822039580527883L;

   @Id @GeneratedValue
   private Long id;

   @ManyToOne
   private TemplateService service;

   private int step;

   private String paramName;

   private String paramType;

   private String paramValue;

   public Long getId()
   {
      return id;
   }

   public void setId(Long id)
   {
      this.id = id;
   }

   public TemplateService getService()
   {
      return service;
   }

   public void setService(TemplateService service)
   {
      this.service = service;
   }

   public int getStep()
   {
      return step;
   }

   public void setStep(int step)
   {
      this.step = step;
   }

   public String getParamName()
   {
      return paramName;
   }

   public void setParamName(String paramName)
   {
      this.paramName = paramName;
   }

   public String getParamType()
   {
      return paramType;
   }

   public void setParamType(String paramType)
   {
      this.paramType = paramType;
   }

   public String getParamValue()
   {
      return paramValue;
   }

   public void setParamValue(String paramValue)
   {
      this.paramValue = paramValue;
   }
}
