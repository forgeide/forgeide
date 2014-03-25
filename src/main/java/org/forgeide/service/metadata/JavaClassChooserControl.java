package org.forgeide.service.metadata;

import org.jboss.forge.addon.ui.hints.InputType;
import org.jboss.forge.addon.ui.input.UIInput;

/**
 * 
 * @author Shane Bryzak
 *
 */
public class JavaClassChooserControl extends InputControl
{
   @Override
   public String getSupportedInputType()
   {
      return InputType.JAVA_CLASS_PICKER;
   }

   @Override
   public Class<String> getProducedType()
   {
      return String.class;
   }

   @Override
   public Class<?>[] getSupportedInputComponentTypes()
   {
      return new Class<?>[] { UIInput.class };
   }

}
