package org.forgeide.forge.metadata;

import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.ui.hints.InputType;
import org.jboss.forge.addon.ui.input.UIInput;

/**
 *
 * @author Shane Bryzak
 *
 */
public class ResourceChooserControl extends InputControl
{

   @Override
   public String getSupportedInputType()
   {
      return InputType.DIRECTORY_PICKER;
   }

   @Override
   public Class<?> getProducedType()
   {
      return Resource.class;
   }

   @Override
   public Class<?>[] getSupportedInputComponentTypes()
   {
      return new Class<?>[] { UIInput.class };
   }

}