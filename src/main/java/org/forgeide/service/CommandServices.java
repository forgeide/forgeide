package org.forgeide.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.jboss.forge.addon.ui.command.CommandFactory;
import org.jboss.forge.addon.ui.command.UICommand;
import org.jboss.forge.addon.ui.controller.CommandController;
import org.jboss.forge.addon.ui.controller.CommandControllerFactory;
import org.jboss.forge.addon.ui.input.InputComponent;

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
   public Map<String,InputComponentMetadata> getCommandMetadata(@PathParam("command") String command) 
       throws Exception {
      IDEUIContext context = new IDEUIContext();
      UICommand cmd = commandFactory.get().getCommandByName(context, command);

      CommandController controller = controllerFactory.get().createSingleController(
               context, new UIRuntimeImpl(), cmd);
      controller.initialize();

      Map<String,InputComponentMetadata> components = new HashMap<String,InputComponentMetadata>();

      for (String key : controller.getInputs().keySet()) {
         InputComponent component = controller.getInputs().get(key);

         InputComponentMetadata meta = new InputComponentMetadata();
         meta.setLabel(component.getLabel());
         meta.setName(component.getName());
         meta.setDescription(component.getDescription());
         meta.setEnabled(component.isEnabled());
         meta.setRequired(component.isRequired());
         meta.setRequiredMessage(component.getRequiredMessage());
         meta.setShortName(component.getShortName());
         meta.setValueType(component.getValueType());

         components.put(key, meta);
      }

      return components;
   }
   
   
   // To set a value:
   
   // CommandController.setValueFor(input,"value")
}
