package org.forgeide.controller;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.forgeide.qualifiers.Configuration;
import org.forgeide.service.websockets.Message;
import org.forgeide.service.websockets.SessionRegistry;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Key;
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

   @Inject @Configuration(key = "github.client_id") String clientId;

   @Inject @Configuration(key = "github.client_secret") String clientSecret;

   private final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
   private final JsonFactory JSON_FACTORY = new JacksonFactory();

   public static class AuthorizationResponse 
   {
      @Key("access_token")
      private String accessToken;

      @Key("scope")
      private String scope;

      @Key("token_type")
      private String tokenType;

      public String getAccessToken() 
      {
        return accessToken;
      }

      public String getScope()
      {
         return scope;
      }

      public String getTokenType()
      {
         return tokenType;
      }
   }

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
      // Check the state against the state stored
      if (stateCache.asMap().containsKey(state))
      {
         // lookup the session ID
         String sessionId = stateCache.getIfPresent(state);
         Message msg = new Message(Message.CAT_GITHUB, Message.OP_GITHUB_AUTHORIZING);
         registry.getSession(sessionId).getAsyncRemote().sendObject(msg);

         HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory();

         GenericUrl url = new GenericUrl();
         url.setScheme("https");
         url.setHost("github.com");
         url.setRawPath("/login/oauth/access_token");
         url.set("client_id", clientId);
         url.set("client_secret", clientSecret);
         url.set("code", code);

         HttpRequest request = requestFactory.buildPostRequest(url, null);
         request.getHeaders().setAccept("application/json");
         request.setParser(new JsonObjectParser(new JacksonFactory()));

         HttpResponse response = request.execute();

         try
         {
            AuthorizationResponse authResponse = response.parseAs(AuthorizationResponse.class);

            String accessToken = authResponse.getAccessToken();
         }
         finally
         {
            response.disconnect();
         }
      }
      else
      {
         throw new IllegalStateException("State values do not match for GitHub authorization request");
      }
   }
}
