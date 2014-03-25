package org.forgeide.service.metadata;

import org.jboss.forge.addon.ui.hints.InputType;

/**
 * 
 * @author Shane Bryzak
 *
 */
public class PasswordTextBoxControl extends  TextBoxControl
{
   @Override
   public String getSupportedInputType() {
      return InputType.SECRET;
   }
}
