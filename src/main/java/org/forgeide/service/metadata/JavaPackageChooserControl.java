package org.forgeide.service.metadata;

import org.jboss.forge.addon.ui.hints.InputType;
import org.jboss.forge.addon.ui.input.UIInput;

/**
 * 
 * @author Shane Bryzak
 *
 */
public class JavaPackageChooserControl extends InputControl
{
   @Override
   public String getSupportedInputType()
   {
      return InputType.JAVA_PACKAGE_PICKER;
   }

   @Override
   public Class<?> getProducedType()
   {
      return String.class;
   }

   @Override
   public Class<?>[] getSupportedInputComponentTypes()
   {
      return new Class<?>[] { UIInput.class };
   }

}
