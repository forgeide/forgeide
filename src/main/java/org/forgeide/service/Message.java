package org.forgeide.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.json.JsonValue;

/**
 * Broadcast messages are transmitted to the client to indicate a change in project state
 *
 * @author Shane Bryzak
 */
public class Message implements Serializable
{
   private static final long serialVersionUID = -7283438779264064519L;

   // Message category
   private String cat;

   // Message operation
   private String op;

   // Message payload
   private Map<String,Object> payload = new HashMap<String,Object>();

   public Message(String cat, String op)
   {
      this.cat = cat;
      this.op = op;
   }

   public String getCat()
   {
      return cat;
   }

   public String getOp()
   {
      return op;
   }

   public void setPayloadValue(String name, Object value)
   {
      payload.put(name, value);
   }

   public Map<String,Object> getPayload()
   {
      return payload;
   }
}
