package org.forgeide.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import org.forgeide.forge.ui.IDEUIContext;
import org.jboss.forge.addon.ui.command.CommandFactory;
import org.jboss.forge.addon.ui.command.UICommand;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.furnace.Furnace;
import org.jboss.forge.furnace.addons.AddonRegistry;

@Model
public class CommandServices
{
   @Inject
   Furnace furnace;

   private List<String> commandNames = new ArrayList<>();

   public void listCommandCategories()
   {
      AddonRegistry addonRegistry = furnace.getAddonRegistry();
      CommandFactory commandFactory = addonRegistry.getServices(CommandFactory.class).get();
      IDEUIContext context = new IDEUIContext();
      for (UICommand cmd : commandFactory.getCommands())
      {
         UICommandMetadata metadata = cmd.getMetadata(context);
         commandNames.add(metadata.getCategory() + " -> " + metadata.getName());
      }
   }

   /**
    * @return the commandNames
    */
   public List<String> getCommandNames()
   {
      return commandNames;
   }
}
