package org.forgeide.service.websockets;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Broadcast messages are transmitted to the client to indicate a change in project state
 *
 * @author Shane Bryzak
 */
public class Message implements Serializable
{
   public static final String CAT_PROJECT = "PROJECT";
   public static final String CAT_RESOURCE = "RESOURCE";

   public static final String OP_PROJECT_NEW = "NEW";
   public static final String OP_PROJECT_LIST = "LIST";

   public static final String OP_RESOURCE_NEW = "NEW";

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
