/**
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.forgeide.forge.ui;

import org.jboss.forge.addon.ui.UIProvider;
import org.jboss.forge.addon.ui.context.AbstractUIContext;
import org.jboss.forge.addon.ui.context.UISelection;
import org.jboss.forge.addon.ui.util.Selections;

/**
 * 
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 */
public class IDEUIContext extends AbstractUIContext
{
   @Override
   public <SELECTIONTYPE> UISelection<SELECTIONTYPE> getInitialSelection()
   {
      // TODO: Change later
      return Selections.emptySelection();
   }

   @Override
   public UIProvider getProvider()
   {
      return IDEUIProvider.INSTANCE;
   }
}
