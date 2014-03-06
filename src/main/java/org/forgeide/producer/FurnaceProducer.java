package org.forgeide.producer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.forgeide.forge.ui.IDEUIContext;
import org.forgeide.qualifiers.Forge;
import org.jboss.forge.addon.ui.command.CommandFactory;
import org.jboss.forge.addon.ui.command.UICommand;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.furnace.Furnace;
import org.jboss.forge.furnace.addons.Addon;
import org.jboss.forge.furnace.addons.AddonRegistry;
import org.jboss.forge.furnace.proxy.ClassLoaderAdapterBuilder;
import org.jboss.forge.furnace.repositories.AddonRepositoryMode;
import org.jboss.forge.furnace.util.Sets;

/**
 * 
 * @author Shane Bryzak
 * 
 */
@ApplicationScoped
public class FurnaceProducer
{
   @Inject
   ServletContext servletContext;

   private Furnace furnace;

   private Map<String, List<String>> availableCommands;

   @PostConstruct
   public void setup()
   {
      furnace = getFurnaceInstance(Thread.currentThread().getContextClassLoader());

      String path = servletContext.getRealPath("/WEB-INF/addon-repository");
      File repoDir = new File(path);

      furnace.addRepository(AddonRepositoryMode.IMMUTABLE, repoDir);

      furnace.startAsync();

      while (!furnace.getStatus().isStarted())
      {
         try
         {
            Thread.sleep(500);
         }
         catch (InterruptedException e)
         {
            return;
         }
      }

      // Query the available commands
      availableCommands = new HashMap<String, List<String>>();

      AddonRegistry addonRegistry = furnace.getAddonRegistry();
      CommandFactory commandFactory = addonRegistry.getServices(CommandFactory.class).get();
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
                        result.add(addon.getClassLoader());
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
