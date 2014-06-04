package org.forgeide.producer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
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
import org.jboss.forge.furnace.addons.Addon;
import org.jboss.forge.furnace.addons.AddonRegistry;
import org.jboss.forge.furnace.proxy.ClassLoaderAdapterBuilder;
import org.jboss.forge.furnace.repositories.AddonRepositoryMode;
import org.jboss.forge.furnace.util.Sets;

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
      furnace = getFurnaceInstance(Thread.currentThread().getContextClassLoader());
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

   private static Furnace getFurnaceInstance(final ClassLoader loader)
   {
      try
      {
         Class<?> furnaceType = loader.loadClass("org.jboss.forge.furnace.impl.FurnaceImpl");
         final Object instance = furnaceType.newInstance();

         final Furnace furnace = (Furnace) ClassLoaderAdapterBuilder
                  .callingLoader(FurnaceProducer.class.getClassLoader())
                  .delegateLoader(loader).enhance(instance, Furnace.class);

         Callable<Set<ClassLoader>> whitelistCallback = new Callable<Set<ClassLoader>>()
         {
            volatile long lastRegistryVersion = -1;
            final Set<ClassLoader> result = Sets.getConcurrentSet();

            @Override
            public Set<ClassLoader> call() throws Exception
            {
               if (furnace.getStatus().isStarted())
               {
                  long registryVersion = furnace.getAddonRegistry().getVersion();
                  if (registryVersion != lastRegistryVersion)
                  {
                     result.clear();
                     lastRegistryVersion = registryVersion;
                     for (Addon addon : furnace.getAddonRegistry().getAddons())
                     {
                        ClassLoader classLoader = addon.getClassLoader();
                        if (classLoader != null)
                           result.add(classLoader);
                     }
                  }
               }

               return result;
            }
         };

         return (Furnace) ClassLoaderAdapterBuilder.callingLoader(FurnaceProducer.class.getClassLoader())
                  .delegateLoader(loader).whitelist(whitelistCallback)
                  .enhance(instance, Furnace.class);
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }

}
