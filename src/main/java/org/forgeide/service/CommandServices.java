package org.forgeide.service;

import java.io.File;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.jboss.forge.addon.ui.command.CommandFactory;
import org.jboss.forge.furnace.Furnace;

@RequestScoped
public class CommandServices
{
   @Inject
   Furnace furnace;

   public void listCommandCategories()
   {

      // furnace.getAddonRegistry()
   }
}
