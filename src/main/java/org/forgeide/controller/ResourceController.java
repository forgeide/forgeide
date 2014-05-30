package org.forgeide.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.websocket.Session;

import org.forgeide.ace.ParticipantConnectionImpl;
import org.forgeide.ace.RemoteUserProxyImpl;
import org.forgeide.model.ProjectAccess;
import org.forgeide.model.ResourceContent;
import org.forgeide.security.model.User;
import org.picketlink.Identity;
import org.picketlink.idm.IdentityManager;

import ch.iserver.ace.DocumentDetails;
import ch.iserver.ace.DocumentModel;
import ch.iserver.ace.UserDetails;
import ch.iserver.ace.collaboration.jupiter.UserRegistryImpl;
import ch.iserver.ace.collaboration.jupiter.server.ServerLogic;
import ch.iserver.ace.collaboration.jupiter.server.ServerLogicImpl;
import ch.iserver.ace.util.CallerThreadDomain;

/**
 *
 * @author Shane Bryzak
 *
 */
@Singleton
public class ResourceController
{
   private Map<Long,ServerLogic> resources = new ConcurrentHashMap<Long,ServerLogic>();

   //private Map<Long,Set<Session>> subscribers = new ConcurrentHashMap<Long,Set<Session>>();

   @Inject
   private Instance<Identity> identity;

   @Inject
   private Instance<IdentityManager> identityManager;

   @Inject
   private Instance<EntityManager> entityManager;

   @Lock(LockType.READ)
   public void openResource(Long resourceId, Session subscriber)
   {
      subscribe(resourceId, subscriber);
   }

   private synchronized void loadResourceContent(Long resourceId)
   {
      if (!resources.containsKey(resourceId))
      {
         ResourceContent content = entityManager.get().find(ResourceContent.class, resourceId);

         List<ProjectAccess> results = entityManager.get().createQuery(
                  "select a from ProjectAccess a where a.project = :project " +
                  "and a.accessLevel = :accessLevel", ProjectAccess.class)
                  .setParameter("project", content.getResource().getProject())
                  .setParameter("accessLevel", ProjectAccess.AccessLevel.OWNER)
                  .getResultList();

         User owner = null;
         // The project has no owner - make the current user the owner
         if (results.isEmpty())
         {
            owner = (User) identity.get().getAccount();
         } else
         {
            owner = identityManager.get().lookupIdentityById(User.class, results.get(0).getUser().getId());
         }

         ServerLogic serverLogic = new ServerLogicImpl(
                  new RemoteUserProxyImpl(owner.getId(), new UserDetails(owner.getUsername())),
                  new CallerThreadDomain(),
                  new CallerThreadDomain(),
                  new DocumentModel(new String(content.getContent()), 0, 0, 
                           new DocumentDetails(content.getResource().getId().toString())),
                  new UserRegistryImpl()
                  );
         resources.put(resourceId, serverLogic);
      }
   }

   private synchronized void subscribe(Long resourceId, Session subscriber)
   {
      if (!resources.containsKey(resourceId)) {
         loadResourceContent(resourceId);
      }

      User user = (User) identity.get().getAccount();

      resources.get(resourceId).join(new ParticipantConnectionImpl(
               new RemoteUserProxyImpl(user.getId(), new UserDetails(user.getUsername())
               )));

      /*if (!subscribers.containsKey(resourceId))
      {
         subscribers.put(resourceId, new HashSet<Session>());
      }

      subscribers.get(resourceId).add(subscriber);*/
   }

   private synchronized void unsubscribe(Long resourceId, Session subscriber)
   {
      //subscribers.get(resourceId).remove(subscriber);
   }
}
