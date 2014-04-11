package org.forgeide.security;

import javax.enterprise.context.ApplicationScoped;

import org.apache.deltaspike.security.api.authorization.Secures;
import org.forgeide.security.annotations.LoggedIn;
import org.picketlink.Identity;

/**
 * Provides authorization logic for typesafe security bindings
 *
 * @author Shane Bryzak
 */
@ApplicationScoped
public class CustomAuthorizer
{

   @Secures @LoggedIn
   public boolean checkLoggedIn(Identity identity)
   {
      return identity.isLoggedIn();
   }
}
