package org.forgeide.service.websockets;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;

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
      Gson gson = new Gson();
      return gson.toJson(msg);
   }
}
