package org.forgeide.security;

import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.forgeide.security.model.User;
import org.picketlink.authentication.AuthenticationException;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.basic.Realm;
import org.picketlink.idm.credential.Token;
import org.picketlink.idm.credential.storage.TokenCredentialStorage;
import org.picketlink.idm.query.IdentityQuery;
import org.picketlink.json.JsonException;
import org.picketlink.json.jose.JWS;
import org.picketlink.json.jose.JWSBuilder;

@ApplicationScoped
public class JWSTokenProvider implements Token.Provider
{
   @Inject
   private PartitionManager partitionManager;

   @Inject
   private IdentityManager identityManager;

   @Inject
   private UserTransaction userTransaction;

   @Override
   public Account getAccount(Token token) {
       JWS jws = unMarshall(token);

       IdentityQuery<User> query = this.identityManager.createIdentityQuery(User.class);

       query.setParameter(User.ID, jws.getSubject());

       List<User> result = query.getResultList();

       if (!result.isEmpty()) {
           return result.get(0);
       }

       return null;
   }

   @Override
   public Token create(Object value) {
       return new Token(value.toString());
   }

   @Override
   public Token issue(Account account) {
       JWSBuilder builder = new JWSBuilder();

       builder
           .id(UUID.randomUUID().toString())
           .rsa256(getPrivateKey())
           .issuer(account.getPartition().getName())
           .issuedAt(getCurrentTime())
           .subject(account.getId())
           .expiration(getCurrentTime() + (5 * 60))
           .notBefore(getCurrentTime());

       Token token = new Token(builder.build().encode());

       boolean isNewTransaction = true;

       try {
           isNewTransaction = Status.STATUS_ACTIVE != this.userTransaction.getStatus();

           if (isNewTransaction) {
               this.userTransaction.begin();
           }

           this.identityManager.updateCredential(account, token);

           if (isNewTransaction) {
               this.userTransaction.commit();
           }
       } catch (Exception e) {
           if (isNewTransaction) {
               try {
                   this.userTransaction.rollback();
               } catch (SystemException ignore) {
               }
           }

           throw new AuthenticationException("Could not issue token for account [" + account + "]", e);
       }

       return token;
   }

   @Override
   public Token renew(Token token) {
       return issue(getAccount(token));
   }

   @Override
   public boolean validate(Token token) {
       Account account = getAccount(token);

       if (account != null) {
           TokenCredentialStorage tokenStorage = this.identityManager.retrieveCurrentCredential(account, TokenCredentialStorage.class);
           return tokenStorage != null && tokenStorage.getValue().equals(token.getToken());
       }

       return false;
   }

   @Override
   public void invalidate(Account account) {
       issue(account);
   }

   @Override
   public boolean supports(Token token) {
       return unMarshall(token) != null;
   }

   @Override
   public <T extends TokenCredentialStorage> T getTokenStorage(Account account, Token token) {
       return null;
   }

   private JWS unMarshall(Token token) {
       try {
           return new JWSBuilder().build(token.getToken(), getPublicKey());
       } catch (JsonException ignore) {
       }

       return null;
   }

   private byte[] getPublicKey() {
       return getPartition().<byte[]>getAttribute("PublicKey").getValue();
   }

   private byte[] getPrivateKey() {
       return getPartition().<byte[]>getAttribute("PrivateKey").getValue();
   }

   private int getCurrentTime() {
       return (int) (System.currentTimeMillis() / 1000);
   }

   private Realm getPartition() {
       return partitionManager.getPartition(Realm.class, Realm.DEFAULT_REALM);
   }
}
