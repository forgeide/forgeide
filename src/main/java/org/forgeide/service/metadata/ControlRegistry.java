package org.forgeide.service.metadata;

import org.jboss.forge.addon.ui.input.InputComponent;

/**
 *
 * @author Shane Bryzak
 */
public class ControlRegistry
{
  private static final InputControl[] CONTROLS = {
     new CheckboxControl(),
     new ComboControl(),
     new DirectoryChooserControl(),
     new FileChooserControl(),
     new JavaClassChooserControl(),
     new JavaPackageChooserControl(),
     new PasswordTextBoxControl(),
     new RadioControl(),
     new SpinnerControl(),
     new TextBoxControl()
  };

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
