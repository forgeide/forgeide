package org.forgeide.controller;

import java.util.UUID;

import javax.ejb.Stateful;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.forgeide.model.GitHubAuthorization;
import org.forgeide.qualifiers.Configuration;
import org.forgeide.security.annotations.LoggedIn;
import org.picketlink.Identity;
import org.xwidgets.websocket.Message;
import org.xwidgets.websocket.SessionRegistry;

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
@Stateful
@ApplicationScoped
public class GitHubRegistrationController
{
   @Inject SessionRegistry registry;

   @Inject @Configuration(key = "github.client_id") String clientId;

   @Inject @Configuration(key = "github.client_secret") String clientSecret;

   @Inject Instance<EntityManager> entityManagerInstance;

   @Inject Instance<Identity> identity;

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

   //@LoggedIn
   public String generateState()
   {
      String state = UUID.randomUUID().toString().replaceAll("-", "");
      String userId = identity.get().getAccount().getId();

      EntityManager em = entityManagerInstance.get();
      GitHubAuthorization auth = em.find(GitHubAuthorization.class, userId);

      if (auth == null)
      {
         auth = new GitHubAuthorization();
         auth.setUserId(userId);
         auth.setAccessState(state);
         em.persist(auth);
      }
      else
      {
         auth.setAccessState(state);
         em.merge(auth);
      }
      return state;
   }

   public void processCode(String state, String code) throws Exception
   {
      EntityManager em = entityManagerInstance.get();
      try
      {
         GitHubAuthorization auth = em.createQuery(
                  "select a from GitHubAuthorization a where a.accessState = :accessState"
                  , GitHubAuthorization.class)
                  .setParameter("accessState", state)
                  .getSingleResult();

         Message msg = new Message("github.authorizing");
//         session.getAsyncRemote().sendObject(msg);

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
            em.merge(auth);

            msg = new Message("github.authorized");
//            session.getAsyncRemote().sendObject(msg);
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
