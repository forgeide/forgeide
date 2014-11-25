package org.forgeide.service;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.forgeide.controller.GitHubRegistrationController;

/**
 * GitHub related RESTful services
 *
 * @author Shane Bryzak
 */
@Path("/github")
@Stateless
public class GitHubServices
{
   public static final String REDIRECT_URI = "https://forgeide.org/github_auth_callback.html";

   @Inject GitHubRegistrationController controller;

   public class GitHubState
   {
      private String state;
      private String redirectUri;

      public GitHubState(String state, String redirectUri)
      {
         this.state = state;
         this.redirectUri = redirectUri;
      }

      public String getState()
      {
         return state;
      }

      public String getRedirectUri()
      {
         return redirectUri;
      }
   }

   @GET
   @Path("/generateState")
   @Produces(MediaType.APPLICATION_JSON)
   public GitHubState generateState()
   {
      String state = controller.generateState();

      return new GitHubState(state, REDIRECT_URI);
   }
}
