package org.forgeide.producer;

import javax.ejb.Stateless;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.picketlink.annotations.PicketLink;

/**
 * Produces important application resources
 *
 * @author Shane Bryzak
 */
@Stateless
public class Resources {
    @PersistenceContext(unitName = "forgeide-default")
    private EntityManager em;

    @Produces
    public EntityManager getEntityManager() {
        return em;
    }

    @Produces
    @PicketLink
    public EntityManager getPicketLinkEntityManager() {
        return em;
    }
}
