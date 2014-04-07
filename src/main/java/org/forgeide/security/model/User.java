package org.forgeide.security.model;

import org.picketlink.idm.model.AbstractIdentityType;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.annotation.AttributeProperty;
import org.picketlink.idm.model.annotation.Unique;

/**
 * Represents the user object
 *
 * @author Shane Bryzak
 */
public class User extends AbstractIdentityType implements Account
{
   private static final long serialVersionUID = 8790565692762081434L;

   @Unique
   @AttributeProperty
   private String username;

   @AttributeProperty
   private String firstName;

   @AttributeProperty
   private String lastName;

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
}
