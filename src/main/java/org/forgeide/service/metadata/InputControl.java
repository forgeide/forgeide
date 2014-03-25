package org.forgeide.service.metadata;

import java.io.Serializable;

import javax.persistence.Transient;

import org.jboss.forge.addon.ui.hints.InputType;
import org.jboss.forge.addon.ui.input.InputComponent;
import org.jboss.forge.addon.ui.util.InputComponents;
import org.jboss.forge.furnace.Furnace;
import org.jboss.forge.furnace.proxy.Proxies;

/**
 * Encapsulates a subset of InputComponent's properties for rendering the client-side UI
 *
 * @author Shane Bryzak
 */
public abstract class InputControl
{
   private Furnace furnace;

   public void init(Furnace furnace) {
      this.furnace = furnace;
   }

   protected Furnace getFurnace() {
      return furnace;
   }

   public abstract String getSupportedInputType();

   public abstract Class<?> getProducedType();

   public abstract Class<?>[] getSupportedInputComponentTypes();

   public boolean handles(InputComponent<?, ?> input) {
      boolean handles = false;
      for (Class<?> inputType : getSupportedInputComponentTypes()) {
         if (inputType.isInstance(input)) {
            handles = true;
            break;
         }
      }

      if (handles) {
         String inputTypeHint = InputComponents.getInputType(input);
         if (inputTypeHint != null && !inputTypeHint.equals(InputType.DEFAULT)) {
            handles = Proxies.areEquivalent(inputTypeHint,
                  getSupportedInputType());
         } else {
            // Fallback to standard type
            handles = getProducedType().isAssignableFrom(
                  input.getValueType());
         }
      }

      return handles;
   }

   public void populateMetadata(InputComponent component, ControlMetadata meta) {
      meta.setLabel(component.getLabel());
      meta.setName(component.getName());
      meta.setDescription(component.getDescription());
      meta.setEnabled(component.isEnabled());
      meta.setRequired(component.isRequired());
      meta.setRequiredMessage(component.getRequiredMessage());
      meta.setShortName(component.getShortName());

      meta.setInputType(getSupportedInputType());
   }
}
