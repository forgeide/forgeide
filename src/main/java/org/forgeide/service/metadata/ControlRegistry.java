package org.forgeide.service.metadata;

import org.jboss.forge.addon.ui.input.InputComponent;
import org.jboss.forge.furnace.Furnace;

/**
 *
 * @author Shane Bryzak
 */
public class ControlRegistry
{
   // The order seems to be important here
  private static final InputControl[] CONTROLS = {
     new CheckboxControl(),
     new ComboControl(),
     new RadioControl(),
     new ResourceChooserControl(),
     new FileChooserControl(),
     new DirectoryChooserControl(),
     new TextBoxControl(),
     new SpinnerControl(),
     new PasswordTextBoxControl(),
     new JavaPackageChooserControl(),
     new JavaClassChooserControl()
  };

  public static void init(Furnace furnace) {
     for (InputControl meta : CONTROLS) {
        meta.init(furnace);
     }
  }

  public static InputControl getControlFor(InputComponent<?, ?> input) {
     for (InputControl meta : CONTROLS) {
        if (meta.handles(input)) {
           return meta;
        }
     }

     throw new IllegalArgumentException("No control found for input type of " +
         input.getValueType());
  }
}
