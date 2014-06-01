package org.forgeide.forge.metadata;

import org.jboss.forge.addon.ui.hints.InputType;
import org.jboss.forge.addon.ui.input.UIInput;

/**
 * 
 * @author Shane Bryzak
 *
 */
public class SpinnerControl extends InputControl
{
   @Override
   public String getSupportedInputType()
   {
      return InputType.DEFAULT;
   }

   @Override
   public Class<?> getProducedType()
   {
      return Integer.class;
   }

   @Override
   public Class<?>[] getSupportedInputComponentTypes()
   {
      return new Class[] { UIInput.class };
   }

}
