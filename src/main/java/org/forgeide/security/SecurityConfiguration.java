package org.forgeide.security;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.forgeide.security.model.User;
import org.forgeide.security.schema.IdentityAttribute;
import org.forgeide.security.schema.IdentityType;
import org.forgeide.security.schema.Partition;
import org.forgeide.security.schema.PartitionAttribute;
import org.forgeide.security.schema.UserCredential;
import org.forgeide.security.schema.UserIdentity;
import org.picketlink.idm.config.IdentityConfiguration;
import org.picketlink.idm.config.IdentityConfigurationBuilder;
import org.picketlink.idm.credential.handler.CredentialHandler;
import org.picketlink.idm.credential.handler.TokenCredentialHandler;
import org.picketlink.idm.model.Relationship;
import org.picketlink.internal.EEJPAContextInitializer;

/**
 * Initialize the security configuration
 * 
 * @author Shane Bryzak
 * 
 */
@ApplicationScoped
public class SecurityConfiguration
{
   @Inject
   private EEJPAContextInitializer contextInitializer;

   @Inject
   private JWSTokenProvider tokenProvider;

   private IdentityConfiguration identityConfig = null;

   @Produces
   IdentityConfiguration createConfig()
   {
      if (identityConfig == null)
      {
         initConfig();
      }
      return identityConfig;
   }

   @SuppressWarnings("unchecked")
   private void initConfig()
   {
      IdentityConfigurationBuilder builder = new IdentityConfigurationBuilder();

      builder
               .named("default")
               .stores()
               .jpa()
               .mappedEntity(
                        Partition.class,
                        PartitionAttribute.class,
                        IdentityType.class,
                        IdentityAttribute.class,
                        UserIdentity.class,
                        UserCredential.class)
               .supportGlobalRelationship(Relationship.class)
               .addContextInitializer(this.contextInitializer)
               .setCredentialHandlerProperty(CredentialHandler.SUPPORTED_ACCOUNT_TYPES_PROPERTY, new Class[] {User.class})
               .setCredentialHandlerProperty(CredentialHandler.LOGIN_NAME_PROPERTY, "username")
               .setCredentialHandlerProperty(TokenCredentialHandler.TOKEN_PROVIDER, tokenProvider)
               .supportType(User.class)
               .supportAllFeatures();

      identityConfig = builder.build();
   }
}
