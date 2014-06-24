package org.forgeide.producer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.forgeide.forge.ui.IDEUIContext;
import org.forgeide.qualifiers.Forge;
import org.jboss.forge.addon.ui.command.CommandFactory;
import org.jboss.forge.addon.ui.command.UICommand;
import org.jboss.forge.addon.ui.controller.CommandControllerFactory;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.furnace.Furnace;
import org.jboss.forge.furnace.addons.AddonRegistry;
import org.jboss.forge.furnace.repositories.AddonRepositoryMode;
import org.jboss.forge.furnace.se.FurnaceFactory;

/**
 * Producer method for Forge objects, starts the Furnace service
 *
 * @author Shane Bryzak
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@ApplicationScoped
public class FurnaceProducer
{
   private Furnace furnace;

   private Map<String, List<String>> availableCommands;

   private CommandFactory commandFactory;

   private CommandControllerFactory controllerFactory;

   public void setup(File repoDir)
   {
      furnace = FurnaceFactory.getInstance(Thread.currentThread().getContextClassLoader(), Thread.currentThread().getContextClassLoader());
      furnace.addRepository(AddonRepositoryMode.IMMUTABLE, repoDir);
      Future<Furnace> future = furnace.startAsync();

      try
      {
         future.get();
      }
      catch (InterruptedException | ExecutionException e)
      {
         throw new RuntimeException("Furnace failed to start.", e);
      }

      availableCommands = new HashMap<>();

      AddonRegistry addonRegistry = furnace.getAddonRegistry();
      commandFactory = addonRegistry.getServices(CommandFactory.class).get();
      IDEUIContext context = new IDEUIContext();
      for (UICommand cmd : commandFactory.getCommands())
      {
         UICommandMetadata metadata = cmd.getMetadata(context);
         if (!availableCommands.containsKey(metadata.getCategory().getName()))
         {
            availableCommands.put(metadata.getCategory().getName(), new ArrayList<String>());
         }
         availableCommands.get(metadata.getCategory().getName()).add(metadata.getName());
      }

      controllerFactory = (CommandControllerFactory) addonRegistry
               .getServices(CommandControllerFactory.class.getName()).get();
   }

   @Produces
   public Furnace getFurnace()
   {
      return furnace;
   }

   @Produces
   @Forge
   public Map<String, List<String>> getAvailableCommands()
   {
      return availableCommands;
   }

   @Produces
   @Forge
   public CommandFactory getCommandFactory()
   {
      return commandFactory;
   }

   @Produces
   @Forge
   public CommandControllerFactory getControllerFactory()
   {
      return controllerFactory;
   }

   @PreDestroy
   public void destroy()
   {
      furnace.stop();
   }

}
