package org.forgeide.security.jws;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.picketlink.authentication.AuthenticationException;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.credential.Token;
import org.picketlink.idm.credential.storage.TokenCredentialStorage;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.basic.Realm;
import org.picketlink.json.jose.JWSBuilder;

@ApplicationScoped
public class JWSTokenProvider implements Token.Provider<JWSToken>
{
   @Inject
   private PartitionManager partitionManager;

   @Inject
   private IdentityManager identityManager;

   @Inject
   private UserTransaction userTransaction;

   @Override
   public JWSToken issue(Account account) {
       JWSBuilder builder = new JWSBuilder();

       builder
           .id(UUID.randomUUID().toString())
           .rsa256(getPrivateKey())
           .issuer(account.getPartition().getName())
           .issuedAt(getCurrentTime())
           .subject(account.getId())
           .expiration(getCurrentTime() + (5 * 60))
           .notBefore(getCurrentTime());

       JWSToken token = new JWSToken(builder.build().encode());

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
   public void invalidate(Account account) {
      getIdentityManager().removeCredential(account, TokenCredentialStorage.class);
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

   private IdentityManager getIdentityManager() {
      return this.partitionManager.createIdentityManager(getPartition());
   }

   @Override
   public JWSToken renew(Account account, JWSToken renewToken)
   {
      return issue(account);
   }

   @Override
   public Class<JWSToken> getTokenType()
   {
      return JWSToken.class;
   }
}
