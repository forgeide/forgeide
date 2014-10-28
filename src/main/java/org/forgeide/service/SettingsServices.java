package org.forgeide.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.forgeide.model.GitHubAuthorization;
import org.picketlink.Identity;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * RESTful endpoint for managing user settings
 *
 * @author Shane Bryzak
 *
 */
@Path("/settings")
@Stateless
public class SettingsServices
{
   @Inject EntityManager entityManager;
   @Inject Identity identity;

   @JsonTypeInfo(use = Id.NONE)
   public static class GitHubAuth
   {
      private String scopes;

      public String getScopes()
      {
         return scopes;
      }

      public void setScopes(String scopes)
      {
         this.scopes = scopes;
      }
   }

   @GET
   @Path("/github")
   @Produces(MediaType.APPLICATION_JSON)
   public GitHubAuth listGitHubAuth()
   {
      List<GitHubAuthorization> results = entityManager.createQuery(
               "select a from GitHubAuthorization a where a.userId = :userId", GitHubAuthorization.class)
          .setParameter("userId", identity.getAccount().getId())
          .getResultList();

      if (results.size() == 1)
      {
         GitHubAuthorization value = results.get(0);

         GitHubAuth auth = new GitHubAuth();
         auth.setScopes(value.getScopes());
         return auth;
      } else {
         return null;
      }
   }
}
