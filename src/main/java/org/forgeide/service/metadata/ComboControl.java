package org.forgeide.service.metadata;

import java.util.ArrayList;
import java.util.List;

import org.jboss.forge.addon.convert.Converter;
import org.jboss.forge.addon.convert.ConverterFactory;
import org.jboss.forge.addon.ui.hints.InputType;
import org.jboss.forge.addon.ui.input.InputComponent;
import org.jboss.forge.addon.ui.input.UISelectOne;
import org.jboss.forge.addon.ui.util.InputComponents;

/**
 *
 * @author Shane Bryzak
 *
 */
public class ComboControl extends InputControl
{
   @Override
   public String getSupportedInputType()
   {
      return InputType.DROPDOWN;
   }

   @Override
   public Class<?> getProducedType()
   {
      return Object.class;
   }

   @Override
   public Class<?>[] getSupportedInputComponentTypes()
   {
      return new Class<?>[] { UISelectOne.class };
   }

   private Converter<Object, String> getConverter(UISelectOne<Object> selectOne) {
      return (Converter<Object, String>) InputComponents.getItemLabelConverter(
             (ConverterFactory) getFurnace().getAddonRegistry().getServices(ConverterFactory.class.getName()).get(),
             selectOne);
   }

   @Override
   public void populateMetadata(InputComponent component, ControlMetadata meta) {
      super.populateMetadata(component, meta);

      if (component instanceof UISelectOne) {
         UISelectOne selectOne = (UISelectOne) component;
         List<String> values = new ArrayList<String>();

         Converter<Object, String> converter = getConverter(selectOne);
         if (selectOne.getValueChoices() != null) {
            for (Object choice : selectOne.getValueChoices()) {
               String itemLabel = converter.convert(choice);
               values.add(itemLabel);
            }
         }

         meta.setValueChoices(values.toArray(new String[values.size()]));
      }
   }
}
