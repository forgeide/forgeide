package org.forgeide.security;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.forgeide.model.IdentityType;
import org.forgeide.model.Partition;
import org.forgeide.model.UserCredential;
import org.forgeide.model.UserIdentity;
import org.picketlink.idm.config.IdentityConfiguration;
import org.picketlink.idm.config.IdentityConfigurationBuilder;
import org.picketlink.idm.model.Relationship;
import org.picketlink.internal.EEJPAContextInitializer;

/**
 * Initialize the security configuration
 *
 * @author Shane Bryzak
 *
 */
@ApplicationScoped
public class SecurityConfiguration {
    @Inject
    private EEJPAContextInitializer contextInitializer;

    private IdentityConfiguration identityConfig = null;

    @Produces IdentityConfiguration createConfig() {
        if (identityConfig == null) {
            initConfig();
        }
        return identityConfig;
    }

    @SuppressWarnings("unchecked")
    private void initConfig() {
        IdentityConfigurationBuilder builder = new IdentityConfigurationBuilder();

        builder
            .named("default")
                .stores()
                    .jpa()
                        .mappedEntity(
                                Partition.class,
                                IdentityType.class,
                                UserIdentity.class,
                                UserCredential.class)
                        .supportGlobalRelationship(Relationship.class)
                        .addContextInitializer(this.contextInitializer)
                        .supportAllFeatures();

        identityConfig = builder.build();
    }
}
