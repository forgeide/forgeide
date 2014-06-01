package org.forgeide.forge.metadata;

import org.jboss.forge.addon.ui.hints.InputType;
import org.jboss.forge.addon.ui.input.UISelectOne;

/**
 *
 * @author Shane Bryzak
 *
 */
public class RadioControl extends InputControl
{

   @Override
   public String getSupportedInputType()
   {
      return InputType.RADIO;
   }

   @Override
   public Class<?> getProducedType()
   {
      return Object.class;
   }

   @Override
   public Class<?>[] getSupportedInputComponentTypes()
   {
      return new Class<?>[] { UISelectOne.class };
   }

}
