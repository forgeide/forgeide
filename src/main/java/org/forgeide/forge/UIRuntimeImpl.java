package org.forgeide.forge;

import org.jboss.forge.addon.ui.UIRuntime;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.input.UIPrompt;
import org.jboss.forge.addon.ui.progress.DefaultUIProgressMonitor;
import org.jboss.forge.addon.ui.progress.UIProgressMonitor;

public class UIRuntimeImpl implements UIRuntime
{
   private final UIProgressMonitor progressMonitor = new DefaultUIProgressMonitor();
   private final UIPrompt prompt = new UIPromptImpl();

   public UIRuntimeImpl()
   {
   }

   @Override
   public UIProgressMonitor createProgressMonitor(UIContext context)
   {
      return progressMonitor;
   }

   @Override
   public UIPrompt createPrompt(UIContext context)
   {
      return prompt;
   }
}