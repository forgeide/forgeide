/**
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.forgeide.forge.ui;

import java.io.PrintStream;

import org.jboss.forge.addon.ui.UIProvider;
import org.jboss.forge.addon.ui.output.UIOutput;

/**
 * 
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 */
public enum IDEUIProvider implements UIProvider
{
   INSTANCE;

   @Override
   public boolean isGUI()
   {
      return true;
   }

   @Override
   public UIOutput getOutput()
   {
      // FIXME: Should write to an output area
      return new UIOutput()
      {

         @Override
         public PrintStream out()
         {
            return System.out;
         }

         @Override
         public PrintStream err()
         {
            return System.err;
         }

         @Override
         public void error(PrintStream arg0, String arg1)
         {
            // TODO Auto-generated method stub
            
         }

         @Override
         public void info(PrintStream arg0, String arg1)
         {
            // TODO Auto-generated method stub
            
         }

         @Override
         public void success(PrintStream arg0, String arg1)
         {
            // TODO Auto-generated method stub
            
         }

         @Override
         public void warn(PrintStream arg0, String arg1)
         {
            // TODO Auto-generated method stub
            
         }
      };
   }

}
