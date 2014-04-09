package org.forgeide.service;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * Message encoder
 *
 * @author Shane Bryzak
 */
public class MessageEncoder implements Encoder.Text<Message>
{
   @Override
   public void init(EndpointConfig config)
   {

   }

   @Override
   public void destroy()
   {

   }

   @Override
   public String encode(Message msg) throws EncodeException
   {
      JsonObjectBuilder messageBuilder = Json.createObjectBuilder()
               .add("c", msg.getCat())
               .add("o", msg.getOp());

      if (msg.getPayload() != null)
      {
         JsonObjectBuilder payloadBuilder = Json.createObjectBuilder();
         for (String key : msg.getPayload().keySet())
         {
            addBuilderValue(payloadBuilder, key, msg.getPayload().get(key));
         }

         messageBuilder.add("payload", payloadBuilder);
      }

      return messageBuilder.build().toString();
   }

   private void addBuilderValue(JsonObjectBuilder builder, String name, Object value)
   {
      if (String.class.isInstance(value))
      {
         builder.add(name, (String) value);
      }
      else if (Boolean.class.isInstance(value))
      {
         builder.add(name, (Boolean) value);
      }
      else if (Integer.class.isInstance(value))
      {
         builder.add(name, (Integer) value);
      }
      else if (Long.class.isInstance(value))
      {
         builder.add(name, (Long) value);
      }
      else if (Double.class.isInstance(value))
      {
         builder.add(name, (Double) value);
      }
      else if (value == null)
      {
         builder.addNull(name);
      }
      else
      {
         throw new IllegalArgumentException("Value [" + value + "] can not be converted to JSON");
      }
   }
}
