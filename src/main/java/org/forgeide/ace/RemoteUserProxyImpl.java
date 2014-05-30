package org.forgeide.ace;

import ch.iserver.ace.UserDetails;
import ch.iserver.ace.net.RemoteUserProxy;

/**
 *
 * @author Shane Bryzak
 *
 */
public class RemoteUserProxyImpl implements RemoteUserProxy
{
   private String id;
   private UserDetails userDetails;

   public RemoteUserProxyImpl(String id, UserDetails userDetails)
   {
      this.id = id;
      this.userDetails = userDetails;
   }

   @Override
   public String getId()
   {
      return id;
   }

   @Override
   public UserDetails getUserDetails()
   {
      return userDetails;
   }

}
