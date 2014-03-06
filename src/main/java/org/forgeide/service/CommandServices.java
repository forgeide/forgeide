package org.forgeide.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jboss.forge.furnace.Furnace;

@RequestScoped
public class CommandServices
{
   @Inject
   Furnace furnace;

   public void listCommandCategories()
   {
      System.out.println("LIST");
      // furnace.getAddonRegistry()
   }
}
