package org.forgeide.service;

import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.forgeide.forge.ui.IDEUIContext;
import org.forgeide.qualifiers.Forge;
import org.jboss.forge.addon.ui.command.CommandFactory;
import org.jboss.forge.addon.ui.command.UICommand;
import org.jboss.forge.addon.ui.controller.CommandController;

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

   @GET
   @Path("/list")
   @Produces(MediaType.APPLICATION_JSON)
   public Map<String, List<String>> getCommands()
   {
      return availableCommands.get();
   }

   public void loadCommand(String command) {
      IDEUIContext context = new IDEUIContext();
      UICommand cmd = commandFactory.get().getCommandByName(context, command);
      
      //CommandController controller = controllerFactory.createSingleController(new MockUIContext(), new MockUIRuntime(),
        //       exampleCommand);
      
      //cmd.
   }
}
