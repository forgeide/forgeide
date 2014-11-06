package org.forgeide.controller;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.websocket.Session;

import org.forgeide.model.GitHubAuthorization;
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

/**
 * Handles GitHub registration state
 *
 * @author Shane Bryzak
 */
@ApplicationScoped
public class GitHubRegistrationController
{
   @Inject SessionRegistry registry;

   @Inject @Configuration(key = "github.client_id") String clientId;

   @Inject @Configuration(key = "github.client_secret") String clientSecret;

   @Inject EntityManager entityManager;

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

   public String generateState(String sessionId, String userId)
   {
      String state = UUID.randomUUID().toString().replaceAll("-", "");
      GitHubAuthorization auth = entityManager.find(GitHubAuthorization.class, userId);
      if (auth == null)
      {
         auth = new GitHubAuthorization();
         auth.setUserId(userId);
         auth.setSessionId(sessionId);
         auth.setAccessState(state);
         entityManager.persist(auth);
      }
      else
      {
         auth.setSessionId(sessionId);
         auth.setAccessState(state);
         entityManager.merge(auth);
      }
      return state;
   }

   public void processCode(String state, String code) throws Exception
   {
      try
      {
         GitHubAuthorization auth = entityManager.createQuery(
                  "select a from GitHubAuthorization a where a.accessState = :accessState"
                  , GitHubAuthorization.class)
                  .getSingleResult();

         Session session = registry.getSession(auth.getSessionId());

         Message msg = new Message(Message.CAT_GITHUB, Message.OP_GITHUB_AUTHORIZING);
         session.getAsyncRemote().sendObject(msg);

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
         request.setParser(new JsonObjectParser(JSON_FACTORY));

         HttpResponse response = request.execute();

         try
         {
            AuthorizationResponse authResponse = response.parseAs(AuthorizationResponse.class);
            auth.setAccessToken(authResponse.getAccessToken());
            auth.setScopes(authResponse.getScope());
            entityManager.merge(auth);

            msg = new Message(Message.CAT_GITHUB, Message.OP_GITHUB_AUTHORIZED);
            session.getAsyncRemote().sendObject(msg);
         }
         finally
         {
            response.disconnect();
         }
      }
      catch (NoResultException ex)
      {
         throw new IllegalStateException("No authorization request found with specified state value.");
      }
   }
}
