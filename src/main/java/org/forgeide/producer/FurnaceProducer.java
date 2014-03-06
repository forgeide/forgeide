package org.forgeide.producer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.jboss.forge.furnace.addons.AddonRegistry;
import org.jboss.forge.furnace.impl.FurnaceImpl;
import org.jboss.forge.furnace.repositories.AddonRepositoryMode;

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
      furnace = new FurnaceImpl();

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
}
