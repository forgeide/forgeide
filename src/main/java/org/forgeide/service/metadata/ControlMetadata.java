package org.forgeide.service.metadata;

import java.io.Serializable;

/**
 * 
 * @author Shane Bryzak
 *
 */
public class ControlMetadata implements Serializable
{
   private String label;
   private String name;
   private String description;
   private String value;
   private boolean enabled;
   private boolean required;
   private String requiredMessage;
   private char shortName;
   private String inputType;
   private String[] valueChoices;

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

   public String getValue()
   {
      return value;
   }

   public void setValue(String value)
   {
      this.value = value;
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

   public String getInputType()
   {
      return inputType;
   }

   public void setInputType(String inputType)
   {
      this.inputType = inputType;
   }

   public String[] getValueChoices() {
      return valueChoices;
   }

   public void setValueChoices(String[] valueChoices) {
      this.valueChoices = valueChoices;
   }
}
