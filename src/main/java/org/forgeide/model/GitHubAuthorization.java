package org.forgeide.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Stores the user's GitHub settings
 *
 * @author Shane Bryzak
 *
 */
@Entity
public class GitHubAuthorization
{
   @Id
   private String userId;

   private String scopes;

   private String accessToken;

   /**
    * Unguessable random string used for setting up oAuth
    */
   @Column(unique = true)
   private String accessState;

   public String getUserId() {
      return userId;
   }

   public void setUserId(String userId) {
      this.userId = userId;
   }

   public String getScopes() {
      return scopes;
   }

   public void setScopes(String scopes) {
      this.scopes = scopes;
   }

   public String getAccessToken() {
      return accessToken;
   }

   public void setAccessToken(String accessToken) {
      this.accessToken = accessToken;
   }

   public String getAccessState() {
      return accessState;
   }

   public void setAccessState(String accessState) {
      this.accessState = accessState;
   }

}
