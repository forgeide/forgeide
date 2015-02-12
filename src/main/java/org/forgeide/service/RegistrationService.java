package org.forgeide.service;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.forgeide.security.model.User;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.credential.Password;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * 
 * @author Shane Bryzak
 *
 */
@Path("/register")
@Stateless
public class RegistrationService
{
   @Inject IdentityManager identityManager;

   @JsonTypeInfo(use = Id.NONE)
   public static class RegistrationParams
   {
      private String username;
      private String firstName;
      private String lastName;
      private String password;

      public String getUsername()
      {
         return username;
      }

      public void setUsername(String username)
      {
         this.username = username;
      }

      public String getFirstName()
      {
         return firstName;
      }

      public void setFirstName(String firstName)
      {
         this.firstName = firstName;
      }

      public String getLastName()
      {
         return lastName;
      }

      public void setLastName(String lastName)
      {
         this.lastName = lastName;
      }

      public String getPassword()
      {
         return password;
      }

      public void setPassword(String password)
      {
         this.password = password;
      }
   }

   @POST
   @Consumes("application/json")
   @Produces(MediaType.APPLICATION_JSON)
   public User register(RegistrationParams params) throws Exception
   {
      User user = new User();
      user.setUsername(params.getUsername());
      user.setFirstName(params.getFirstName());
      user.setLastName(params.getLastName());

      identityManager.add(user);
      identityManager.updateCredential(user, new Password(params.getPassword()));

      return user;
   }
}
