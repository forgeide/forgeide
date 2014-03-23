package org.forgeide.service;

import java.io.Serializable;

/**
 * Encapsulates a subset of InputComponent's properties for rendering the client-side UI
 *
 * @author Shane Bryzak
 */
public class InputComponentMetadata implements Serializable
{
   private String label;
   private String name;
   private String description;
   private Class valueType;
   private boolean enabled;
   private boolean required;
   private String requiredMessage;
   private char shortName;
   public String getLabel()
   {
      return label;
   }
   public void setLabel(String label)
   {
      this.label = label;
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
   public Class getValueType()
   {
      return valueType;
   }
   public void setValueType(Class valueType)
   {
      this.valueType = valueType;
   }
   public boolean isEnabled()
   {
      return enabled;
   }
   public void setEnabled(boolean enabled)
   {
      this.enabled = enabled;
   }
   public boolean isRequired()
   {
      return required;
   }
   public void setRequired(boolean required)
   {
      this.required = required;
   }
   public String getRequiredMessage()
   {
      return requiredMessage;
   }
   public void setRequiredMessage(String requiredMessage)
   {
      this.requiredMessage = requiredMessage;
   }

   public char getShortName() 
   {
      return shortName;
   }

   public void setShortName(char shortName) 
   {
      this.shortName = shortName;
   }
}
