package org.forgeide.controller;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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

   public void processCode(String state, String code) throws Exception
   {
      // lookup the session ID
      String sessionId = stateCache.getIfPresent(state);
      //Message msg = new Message(Message.CAT_GITHUB, Message.OP_GITHUB_CODE);
      //msg.setPayloadValue("code", code);
      //msg.setPayloadValue("state", state);
      //registry.getSession(sessionId).getAsyncRemote().sendObject(msg);

      // TODO Send a status message

      CloseableHttpClient httpClient = HttpClients.createDefault();
      HttpPost post = new HttpPost("https://github.com/login/oauth/access_token");
      CloseableHttpResponse response = httpClient.execute(post);

      // TODO set the necessary parameters

      try
      {
         HttpEntity entity = response.getEntity();

         // TODO Do something with this
      }
      finally
      {
         response.close();
      }
   }
}
