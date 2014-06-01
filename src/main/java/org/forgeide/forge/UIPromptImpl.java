package org.forgeide.forge;

import org.jboss.forge.addon.ui.input.UIPrompt;

public class UIPromptImpl implements UIPrompt
{

   @Override
   public String prompt(String message)
   {
      return null;
   }

   @Override
   public String promptSecret(String message)
   {
      return null;
   }

   @Override
   public boolean promptBoolean(String message)
   {
      // TODO: Change
      return true;
   }

   @Override
   public boolean promptBoolean(String message, boolean defaultValue)
   {
      return defaultValue;
   }

}