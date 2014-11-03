package org.forgeide.controller;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.forgeide.service.websockets.Message;
import org.forgeide.service.websockets.SessionRegistry;

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
   // state:session ID cache
   private Cache<String,String> stateCache;

   @Inject SessionRegistry registry;

   public GitHubRegistrationController()
   {
      stateCache = CacheBuilder.newBuilder()
         .concurrencyLevel(4)
         .maximumSize(1000)
         .expireAfterWrite(10, TimeUnit.MINUTES)
         .build();
   }

   public String generateState(String sessionId)
   {
      String state = UUID.randomUUID().toString().replaceAll("-", "");
      stateCache.put(state, sessionId);
      return state;
   }

   public void processCode(String state, String code)
   {
      // lookup the session ID
      String sessionId = stateCache.getIfPresent(state);
      Message msg = new Message("github", "receivedCode");
      msg.setPayloadValue("code", code);
      registry.getSession(sessionId).getAsyncRemote().sendObject(msg);
   }
}
