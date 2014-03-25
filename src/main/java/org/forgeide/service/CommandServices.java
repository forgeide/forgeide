package org.forgeide.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.forgeide.forge.ui.IDEUIContext;
import org.forgeide.qualifiers.Forge;
import org.forgeide.service.metadata.ControlMetadata;
import org.forgeide.service.metadata.ControlRegistry;
import org.forgeide.service.metadata.InputControl;
import org.jboss.forge.addon.ui.command.CommandFactory;
import org.jboss.forge.addon.ui.command.UICommand;
import org.jboss.forge.addon.ui.controller.CommandController;
import org.jboss.forge.addon.ui.controller.CommandControllerFactory;
import org.jboss.forge.addon.ui.facets.HintsFacet;
import org.jboss.forge.addon.ui.input.InputComponent;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.input.UIInputMany;
import org.jboss.forge.addon.ui.input.UISelectMany;
import org.jboss.forge.addon.ui.input.UISelectOne;
import org.jboss.forge.addon.ui.util.InputComponents;
import org.jboss.forge.furnace.Furnace;

/**
 * Provides RESTful services for querying Forge commands
 * 
 * @author Shane Bryzak
 * 
 */
@Path("/commands")
@Model
public class CommandServices
{
   @Inject
   @Forge
   Instance<Map<String, List<String>>> availableCommands;

   @Inject
   @Forge
   Instance<CommandFactory> commandFactory;

   @Inject
   @Forge
   Instance<CommandControllerFactory> controllerFactory;

   @Inject
   Instance<Furnace> furnace;

   @PostConstruct
   public void init() {
      ControlRegistry.init(furnace.get());
   }

   @GET
   @Path("/list")
   @Produces(MediaType.APPLICATION_JSON)
   public Map<String, List<String>> getCommands()
   {
      return availableCommands.get();
   }

   @GET
   @Path("/get/{command}")
   @Produces(MediaType.APPLICATION_JSON)
   public List<ControlMetadata> getCommandMetadata(@PathParam("command") String command) 
       throws Exception {
      IDEUIContext context = new IDEUIContext();
      UICommand cmd = commandFactory.get().getCommandByName(context, command);

      CommandController controller = controllerFactory.get().createSingleController(
               context, new UIRuntimeImpl(), cmd);
      controller.initialize();

      //Map<String,InputComponentMetadata> components = new HashMap<String,InputComponentMetadata>();
      List<ControlMetadata> controlMetadata = new ArrayList<ControlMetadata>();

      for (String key : controller.getInputs().keySet()) {
         InputComponent component = controller.getInputs().get(key);

         InputControl control = ControlRegistry.getControlFor(component);

         ControlMetadata meta = new ControlMetadata();

         control.populateMetadata(component, meta);

         controlMetadata.add(meta);
      }

      return controlMetadata;
   }

}
