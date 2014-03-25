package org.forgeide.service.metadata;

import java.io.File;

import org.jboss.forge.addon.ui.hints.InputType;
import org.jboss.forge.addon.ui.input.UIInput;

/**
 *
 * @author Shane Bryzak
 */
public class FileChooserControl extends InputControl
{
   @Override
   public String getSupportedInputType()
   {
      return InputType.FILE_PICKER;
   }

   @Override
   public Class<File> getProducedType()
   {
      return File.class;
   }

   @Override
   public Class<?>[] getSupportedInputComponentTypes()
   {
      return new Class<?>[] { UIInput.class };
   }

}
