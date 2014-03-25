package org.forgeide.service.metadata;

import org.jboss.forge.addon.ui.hints.InputType;
import org.jboss.forge.addon.ui.input.UIInput;

public class CheckboxControl extends InputControl
{
   @Override
   public String getSupportedInputType()
   {
      return InputType.CHECKBOX;
   }

   @Override
   public Class<Boolean> getProducedType()
   {
      return Boolean.class;
   }

   @Override
   public Class<?>[] getSupportedInputComponentTypes()
   {
      return new Class<?>[] { UIInput.class };
   }
}
