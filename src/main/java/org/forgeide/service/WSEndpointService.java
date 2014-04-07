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
@ServerEndpoint("/websocket/projects")
public class WSEndpointService
{
   @Inject SessionRegistry registry;

   @OnMessage
   public void onMessage(String message, Session session)
   {
      String msg = "Received message: " + message;
      System.out.println(msg);

      for (Session s : registry.getSessions())
      {
         s.getAsyncRemote().sendText(msg);
      }

      //return message;
   }

   @OnOpen
   public void onOpen(Session session)
   {
      System.out.println("Session opened: " + session.getId());

      registry.registerSession(session);

      session.getAsyncRemote().sendText("test message");
   }

   @OnClose
   public void onClose(CloseReason reason, Session session)
   {
      registry.unregisterSession(session);

      String msg = "";
      if (reason != null)
      {
         msg = reason.getReasonPhrase();
      }
      System.out.println("WebSocket closed: " + msg);
   }
}
