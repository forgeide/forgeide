package org.forgeide.producer;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.jboss.forge.furnace.Furnace;
import org.jboss.forge.furnace.se.FurnaceFactory;

/**
 * 
 * @author Shane Bryzak
 * 
 */
@ApplicationScoped
public class FurnaceProducer {

    private final Furnace furnace;

    public FurnaceProducer() {
        furnace = FurnaceFactory.getInstance();
        furnace.startAsync();
    }

    @Produces
    public Furnace getFurnace() {
        return furnace;
    }

    @PreDestroy
    public void destroy() {
        furnace.stop();
    }
}
