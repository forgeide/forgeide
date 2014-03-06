package org.forgeide.producer;

import java.io.File;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.jboss.forge.furnace.Furnace;
import org.jboss.forge.furnace.repositories.AddonRepositoryMode;
import org.jboss.forge.furnace.se.FurnaceFactory;

/**
 * 
 * @author Shane Bryzak
 * 
 */
@ApplicationScoped
public class FurnaceProducer {

    @Inject ServletContext servletContext;

    private final Furnace furnace;

    public FurnaceProducer() {
        furnace = FurnaceFactory.getInstance();
        furnace.startAsync();

        String path = servletContext.getRealPath("/WEB-INF/addon-repository");
        File repoDir = new File(path);

        furnace.addRepository(AddonRepositoryMode.IMMUTABLE, repoDir);
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
