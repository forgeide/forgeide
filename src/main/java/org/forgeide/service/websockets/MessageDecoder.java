package org.forgeide.service.websockets;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonException;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;

/**
 * 
 * @author Shane Bryzak
 *
 */
public class MessageDecoder implements Decoder.Text<Message>
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
   public Message decode(String value) throws DecodeException
   {
      Gson gson = new Gson();
      return gson.<Message>fromJson(value, Message.class);
   }

   @Override
   public boolean willDecode(String value)
   {
      try
      {
         Json.createReader(new StringReader(value)).readObject();
         return true;
      }
      catch (JsonException ex)
      {
         ex.printStackTrace();
         return false;
      }
   }

}
