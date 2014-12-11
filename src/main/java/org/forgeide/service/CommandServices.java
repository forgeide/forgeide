package org.forgeide.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.forgeide.forge.UIRuntimeImpl;
import org.forgeide.forge.metadata.ControlMetadata;
import org.forgeide.forge.metadata.ControlRegistry;
import org.forgeide.forge.metadata.InputControl;
import org.forgeide.forge.metadata.ResultMetadata;
import org.forgeide.forge.ui.IDEUIContext;
import org.forgeide.qualifiers.Forge;
import org.jboss.forge.addon.ui.command.CommandFactory;
import org.jboss.forge.addon.ui.command.UICommand;
import org.jboss.forge.addon.ui.controller.CommandController;
import org.jboss.forge.addon.ui.controller.CommandControllerFactory;
import org.jboss.forge.addon.ui.input.InputComponent;
import org.jboss.forge.addon.ui.result.Failed;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.furnace.Furnace;

/**
 * Provides RESTful services for querying Forge commands
 *
 * @author Shane Bryzak
 *
 */
@Path("/commands")
@Stateless
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
       throws Exception 
   {
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

   @POST
   @Path("/execute/{command}")
   @Consumes("application/json")
   @Produces(MediaType.APPLICATION_JSON)
   public ResultMetadata executeCommand(@PathParam("command") String command, Map<String,Object> parameters)
       throws Exception
   {
      IDEUIContext context = new IDEUIContext();
      UICommand cmd = commandFactory.get().getCommandByName(context, command);

      CommandController controller = controllerFactory.get().createSingleController(
               context, new UIRuntimeImpl(), cmd);
      controller.initialize();

      for (String key : parameters.keySet()) {
         controller.setValueFor(key, parameters.get(key));
      }

      //controller.setValueFor("targetLocation", new ResourcePath());

      ResultMetadata rm = new ResultMetadata();
      try
      {
         Result result = controller.execute();
         if (result instanceof Failed) {
            rm.setPassed(false);
            rm.setMessage(result.getMessage());
            if (((Failed) result).getException() != null) 
            {
               rm.setException(((Failed) result).getException().getMessage());
            }
         }
         else
         {
            rm.setPassed(true);
            rm.setMessage(result.getMessage());
         }
      }
      catch (Exception ex)
      {
         rm.setPassed(false);
         rm.setException(ex.getMessage());
      }

      return rm;
   }

}
