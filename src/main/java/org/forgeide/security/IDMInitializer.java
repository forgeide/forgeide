package org.forgeide.security;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.forgeide.model.Project;
import org.forgeide.model.ProjectAccess;
import org.forgeide.model.ProjectAccess.AccessLevel;
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

   @Inject
   private EntityManager entityManager;

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

      Project p = new Project();
      p.setName("Meeting Planner");
      p.setDescription("Simplified meeting and event planning, provides comprehensive features for venue, resource and attendee management.");

      entityManager.persist(p);

      ProjectAccess pa = new ProjectAccess();
      pa.setProject(p);
      pa.setAccessLevel(AccessLevel.OWNER);
      pa.setUserId(u.getId());

      entityManager.persist(pa);

      p = new Project();
      p.setName("Customer Directory");
      p.setDescription("Basic customer contact directory, with highly customisable attributes and RESTful interface for managing and querying entities.");

      entityManager.persist(p);

      pa = new ProjectAccess();
      pa.setProject(p);
      pa.setAccessLevel(AccessLevel.OWNER);
      pa.setUserId(u.getId());

      entityManager.persist(pa);
   }
}
