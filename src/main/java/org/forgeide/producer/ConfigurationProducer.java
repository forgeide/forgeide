package org.forgeide.producer;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.forgeide.qualifiers.Configuration;

/**
 * Producer method for configuration values
 *
 * @author Shane Bryzak
 */
public class ConfigurationProducer
{
   private static final String CONFIG_FILE = "forgeide";

   private final ResourceBundle bundle = ResourceBundle.getBundle(CONFIG_FILE);

   @Produces @Configuration
   public String produceConfigurationValue(InjectionPoint injectionPoint)
   {
      Configuration config = injectionPoint.getAnnotated().getAnnotation(Configuration.class);
      if (config.key() == null || config.key().length() == 0)
      {
         return config.defaultValue();
      }
      String value;
      try
      {
         value = bundle.getString(config.key());
         if (value == null || value.trim().length() == 0)
         {
            if (config.required())
            {
               throw new IllegalArgumentException("Configuration value not set");
            }
            else
            {
               return config.defaultValue();
            }
         }
         return value;
      }
      catch (MissingResourceException ex)
      {
         if (config.required())
         {
            throw new IllegalStateException();
         }
         return "Invalid key";
      }
   }
}
