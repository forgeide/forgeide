package org.forgeide.security;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.forgeide.security.model.User;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.basic.Realm;

/**
 * Performs default identity initialization
 * 
 * @author Shane Bryzak
 * 
 */
@Singleton
@Startup
public class IDMInitializer
{
   @Inject
   private PartitionManager partitionManager;

   @PostConstruct
   public void create()
   {
      if (partitionManager.getPartition(Realm.class, Realm.DEFAULT_REALM) == null)
      {
         partitionManager.add(new Realm(Realm.DEFAULT_REALM));
      }

      IdentityManager im = partitionManager.createIdentityManager();
      User u = new User();

      u.setFirstName("Shane");
      u.setLastName("Bryzak");
      u.setUsername("sbryzak");
      im.add(u);
      im.updateCredential(u, new Password("password"));
   }
}
