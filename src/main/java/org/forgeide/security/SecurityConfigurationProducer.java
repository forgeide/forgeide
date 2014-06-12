package org.forgeide.security;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.forgeide.security.model.User;
import org.forgeide.security.schema.IdentityAttribute;
import org.forgeide.security.schema.IdentityType;
import org.forgeide.security.schema.Partition;
import org.forgeide.security.schema.PartitionAttribute;
import org.forgeide.security.schema.UserTokenCredential;
import org.forgeide.security.schema.UserPasswordCredential;
import org.forgeide.security.schema.UserIdentity;
import org.picketlink.config.SecurityConfiguration;
import org.picketlink.config.SecurityConfigurationBuilder;
import org.picketlink.event.PartitionManagerCreateEvent;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.config.IdentityConfiguration;
import org.picketlink.idm.config.IdentityConfigurationBuilder;
import org.picketlink.idm.config.SecurityConfigurationException;
import org.picketlink.idm.credential.handler.CredentialHandler;
import org.picketlink.idm.credential.handler.TokenCredentialHandler;
import org.picketlink.idm.model.Attribute;
import org.picketlink.idm.model.Relationship;
import org.picketlink.idm.model.basic.Realm;
import org.picketlink.internal.EEJPAContextInitializer;

/**
 * Initialize the security configuration
 * 
 * @author Shane Bryzak
 * 
 */
@ApplicationScoped
public class SecurityConfigurationProducer
{
   public static final String KEYSTORE_FILE_PATH = "/keystore.jks";

   @Inject
   private EEJPAContextInitializer contextInitializer;

   @Inject
   private JWSTokenProvider tokenProvider;

   private SecurityConfiguration securityConfig = null;

   private KeyStore keyStore;

   @Produces
   SecurityConfiguration createConfig()
   {
      if (securityConfig == null)
      {
         initConfig();
      }
      return securityConfig;
   }

   @SuppressWarnings("unchecked")
   private void initConfig()
   {
      SecurityConfigurationBuilder builder = new SecurityConfigurationBuilder();
      builder.identity().stateless();

      builder
         .identity()
            .idmConfig()
               .named("default")
               .stores()
               .jpa()
               .mappedEntity(
                        Partition.class,
                        PartitionAttribute.class,
                        IdentityType.class,
                        IdentityAttribute.class,
                        UserIdentity.class,
                        UserPasswordCredential.class,
                        UserTokenCredential.class)
               .supportGlobalRelationship(Relationship.class)
               .addContextInitializer(this.contextInitializer)
               .setCredentialHandlerProperty(CredentialHandler.SUPPORTED_ACCOUNT_TYPES_PROPERTY,
                        new Class[] { User.class })
               .setCredentialHandlerProperty(CredentialHandler.LOGIN_NAME_PROPERTY, "username")
               .setCredentialHandlerProperty(TokenCredentialHandler.TOKEN_PROVIDER, tokenProvider)
               .supportType(User.class)
               .supportAllFeatures();


      securityConfig = builder.build();
   }

   public void configureDefaultPartition(@Observes PartitionManagerCreateEvent event) {
      PartitionManager partitionManager = event.getPartitionManager();

      createDefaultPartition(partitionManager);
//      createDefaultRoles(partitionManager);
//      createAdminAccount(partitionManager);
  }

   private void createDefaultPartition(PartitionManager partitionManager) {
      Realm partition = partitionManager.getPartition(Realm.class, Realm.DEFAULT_REALM);

      if (partition == null) {
          try {
              partition = new Realm(Realm.DEFAULT_REALM);

              partition.setAttribute(new Attribute<byte[]>("PublicKey", getPublicKey()));
              partition.setAttribute(new Attribute<byte[]>("PrivateKey", getPrivateKey()));

              partitionManager.add(partition);
          } catch (Exception e) {
              throw new SecurityConfigurationException("Could not create default partition.", e);
          }
      }
  }

   private byte[] getPrivateKey() throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
      return getKeyStore().getKey("servercert", "test123".toCharArray()).getEncoded();
  }

  private byte[] getPublicKey() throws KeyStoreException {
      return getKeyStore().getCertificate("servercert").getPublicKey().getEncoded();
  }

  private KeyStore getKeyStore() {
      if (this.keyStore == null) {
          try {
              this.keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
              getKeyStore().load(getClass().getResourceAsStream(KEYSTORE_FILE_PATH), "store123".toCharArray());
          } catch (Exception e) {
              throw new SecurityException("Could not load key store.", e);
          }
      }

      return this.keyStore;
  }
}
