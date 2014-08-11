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
import org.picketlink.annotations.PicketLink;
import org.picketlink.authentication.web.TokenAuthenticationScheme;
import org.picketlink.config.SecurityConfigurationBuilder;
import org.picketlink.event.PartitionManagerCreateEvent;
import org.picketlink.event.SecurityConfigurationEvent;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.config.SecurityConfigurationException;
import org.picketlink.idm.model.Attribute;
import org.picketlink.idm.model.basic.Realm;

/**
 * Initialize the security configuration
 * 
 * @author Shane Bryzak
 * 
 */
@ApplicationScoped
public class SecurityConfiguration
{
   public static final String KEYSTORE_FILE_PATH = "/keystore.jks";

   @Inject
   private TokenAuthenticationScheme tokenAuthenticationScheme;

   @Produces
   @PicketLink
   public TokenAuthenticationScheme configureTokenAuthenticationScheme() {
       return this.tokenAuthenticationScheme;
   }

   private KeyStore keyStore;

   @SuppressWarnings("unchecked")
   public void initConfig(@Observes SecurityConfigurationEvent event)
   {
      SecurityConfigurationBuilder builder = event.getBuilder();
      builder.identity().stateless();

      builder
         .identity()
            .idmConfig()
               .named("default")
                  .stores()
                     .jpa()
                        .supportType(User.class)
                        .supportAllFeatures();
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
