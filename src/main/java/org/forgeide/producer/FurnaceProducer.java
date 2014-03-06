package org.forgeide.producer;

import java.io.File;
import java.util.List;
import java.util.ServiceLoader;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.jboss.forge.furnace.Furnace;
import org.jboss.forge.furnace.repositories.AddonRepository;
import org.jboss.forge.furnace.repositories.AddonRepositoryMode;
import org.jboss.forge.furnace.repositories.MutableAddonRepository;
import org.jboss.forge.furnace.se.FurnaceFactory;
import org.jboss.forge.furnace.util.OperatingSystemUtils;

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

        String[] args = {};

        furnace.setArgs(args);

        if (!containsMutableRepository(furnace.getRepositories()))
            furnace.addRepository(AddonRepositoryMode.MUTABLE, new File(OperatingSystemUtils.getUserForgeDir(),
                    "addons"));

        furnace.start();

    }

    @Produces
    public Furnace getFurnace() {
        return furnace;
    }

    private boolean containsMutableRepository(List<AddonRepository> repositories)
    {
       boolean result = false;
       for (AddonRepository repository : repositories)
       {
          if (repository instanceof MutableAddonRepository)
          {
             result = true;
             break;
          }
       }
       return result;
    }


}
