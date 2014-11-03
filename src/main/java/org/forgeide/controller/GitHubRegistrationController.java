package org.forgeide.controller;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * Handles GitHub registration state
 *
 * @author Shane Bryzak
 */
@ApplicationScoped
public class GitHubRegistrationController
{
   private Cache<String,Session> stateCache;

   public GitHubRegistrationController()
   {
      CacheBuilder.newBuilder()
         .concurrencyLevel(4)
         .maximumSize(1000)
         .expireAfterWrite(10, TimeUnit.MINUTES)
         .build();
   }

   public String generateState(Session session)
   {
      String state = UUID.randomUUID().toString().replaceAll("-", "");
      stateCache.put(state, session);
      return state;
   }
}
