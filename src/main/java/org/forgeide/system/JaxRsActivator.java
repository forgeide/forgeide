package org.forgeide.system;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.picketlink.rest.AuthenticationEndpoint;

/**
 * Required for activating JAX-RS without any XML configuration
 *
 * @author Shane Bryzak
 *
 */
@ApplicationPath("/rest")
public class JaxRsActivator extends Application
{

   @Override
   public Set<Class<?>> getClasses()
   {
      // TODO Auto-generated method stub
      return null;
   }
  // Intentionally left blank

  /* public Set<Class<?>> getClasses() {
      Set<Class<?>> services = new HashSet<Class<?>>();
      services.add(AuthenticationEndpoint.class);
      return services;
  }*/
}
