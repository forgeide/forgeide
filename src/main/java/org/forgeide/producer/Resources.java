package org.forgeide.producer;

import java.util.logging.Logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Produces important application resources
 * 
 * @author Shane Bryzak
 */
public class Resources
{
   @Produces
   @PersistenceContext(unitName = "forgeide-default")
   private EntityManager em;

   @Produces
   public Logger produceLog(InjectionPoint injectionPoint) {
       return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
   }
}
