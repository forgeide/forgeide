package org.forgeide.security;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.picketlink.annotations.PicketLink;
import org.picketlink.authentication.web.TokenAuthenticationScheme;

@ApplicationScoped
public class AuthenticationConfiguration
{
   @Inject
   private TokenAuthenticationScheme tokenAuthenticationScheme;

   @Produces
   @PicketLink
   public TokenAuthenticationScheme configureTokenAuthenticationScheme() {
       return this.tokenAuthenticationScheme;
   }
}
