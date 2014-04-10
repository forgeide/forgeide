package org.forgeide.service;

import javax.inject.Inject;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * 
 * @author Shane Bryzak
 *
 */
@ServerEndpoint(value = "/websocket/projects", encoders = {MessageEncoder.class})
public class WSEndpointService
{
   @Inject SessionRegistry registry;

   @OnMessage
   public void onMessage(String data, Session session)
   {
      String msg = "Received message: " + data;
      System.out.println(msg);

      //registry.sendAll(msg);

      //return message;
   }

   @OnOpen
   public void onOpen(Session session)
   {
      registry.registerSession(session);
   }

   @OnClose
   public void onClose(CloseReason reason, Session session)
   {
      registry.unregisterSession(session);
   }
}
