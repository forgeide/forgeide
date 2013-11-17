package org.forgeide.security;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.picketlink.idm.PartitionManager;

/**
 * Performs default identity initialization
 *
 * @author Shane Bryzak
 *
 */
@Singleton
@Startup
public class IDMInitializer {
    @Inject
    private PartitionManager partitionManager;

    @PostConstruct
    public void create() {
        // Placeholder
    }
}
