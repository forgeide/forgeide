package org.forgeide.service;

import java.io.File;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.forgeide.producer.FurnaceProducer;
import org.jboss.logging.Logger;

/**
 * Initializes expensive resources at startup time
 *
 * @author Shane Bryzak
 *
 */
@WebListener
public class ServiceInitializer implements ServletContextListener
{
   private Logger log = Logger.getLogger(ServiceInitializer.class);

   @Inject Instance<FurnaceProducer> furnaceProducer;

   @Override
   public void contextInitialized(ServletContextEvent evt)
   {
      String path = evt.getServletContext().getRealPath("/WEB-INF/addon-repository");
      File repoDir = new File(path);

      furnaceProducer.get().setup(repoDir);

      int size = furnaceProducer.get().getAvailableCommands().keySet().size();

      log.infof("Forge Initialized with [%d] commands", size);
   }

   @Override
   public void contextDestroyed(ServletContextEvent arg0)
   {
      // NOOP
   }

}
