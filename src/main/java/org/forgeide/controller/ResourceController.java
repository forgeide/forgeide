package org.forgeide.controller;

import java.util.HashMap;
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

   private Map<Long,List<Session>> subscribers = new ConcurrentHashMap<Long,List<Session>>();

   @Inject
   private Instance<EntityManager> entityManager;

   @Lock(LockType.READ)
   public void openResource(Long resourceId)
   {
      if (!resourceContent.containsKey(resourceId)) {
         loadResourceContent(resourceId);
      }
   }

   private synchronized void loadResourceContent(Long resourceId)
   {
      if (!resourceContent.containsKey(resourceId))
      {
         ProjectResource resource = entityManager.get().find(ProjectResource.class, resourceId);
         ResourceContent content = entityManager.get().find(ResourceContent.class, resource);
         resourceContent.put(resourceId, content.getContent());
      }
   }

}
