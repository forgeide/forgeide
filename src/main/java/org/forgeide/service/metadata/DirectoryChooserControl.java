package org.forgeide.service.metadata;

import java.io.File;

import org.jboss.forge.addon.ui.hints.InputType;
import org.jboss.forge.addon.ui.input.UIInput;

/**
 *
 * @author Shane Bryzak
 *
 */
public class DirectoryChooserControl extends InputControl
{

   @Override
   public String getSupportedInputType()
   {
      return InputType.DIRECTORY_PICKER;
   }

   @Override
   public Class<?> getProducedType()
   {
      return File.class;
   }

   @Override
   public Class<?>[] getSupportedInputComponentTypes()
   {
      return new Class<?>[] { UIInput.class };
   }

}
