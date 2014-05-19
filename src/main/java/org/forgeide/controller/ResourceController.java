package org.forgeide.controller;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.websocket.Session;

import org.forgeide.model.ProjectResource;
import org.forgeide.model.ResourceContent;

/**
 *
 * @author Shane Bryzak
 *
 */
@Singleton
public class ResourceController
{
   private Map<Long,byte[]> resourceContent = new ConcurrentHashMap<Long,byte[]>();

   private Map<Long,Set<Session>> subscribers = new ConcurrentHashMap<Long,Set<Session>>();

   @Inject
   private Instance<EntityManager> entityManager;

   @Lock(LockType.READ)
   public void openResource(Long resourceId, Session subscriber)
   {
      if (!resourceContent.containsKey(resourceId)) {
         loadResourceContent(resourceId);
      }

      subscribe(resourceId, subscriber);
   }

   private synchronized void loadResourceContent(Long resourceId)
   {
      if (!resourceContent.containsKey(resourceId))
      {
         ResourceContent content = entityManager.get().find(ResourceContent.class, resourceId);
         resourceContent.put(resourceId, content.getContent());
      }
   }

   private synchronized void subscribe(Long resourceId, Session subscriber)
   {
      if (!subscribers.containsKey(resourceId))
      {
         subscribers.put(resourceId, new HashSet<Session>());
      }

      subscribers.get(resourceId).add(subscriber);
   }

   private synchronized void unsubscribe(Long resourceId, Session subscriber)
   {
      subscribers.get(resourceId).remove(subscriber);
   }
}
