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

import org.forgeide.qualifiers.Forge;

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
   @Inject @Forge
   Instance<Map<String,List<String>>> availableCommands;

   @GET
   @Produces(MediaType.APPLICATION_JSON)
   public Map<String,List<String>> getCommands()
   {
      return availableCommands.get();
   }
}
