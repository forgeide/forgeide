package org.forgeide.service.websockets;

import javax.inject.Inject;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.xwidgets.websocket.Message;
import org.xwidgets.websocket.MessageHandlerFactory;

/**
 * Endpoint for websocket services
 *
 * @author Shane Bryzak
 */
@ServerEndpoint(value = "/websocket/services", encoders = {MessageEncoder.class}, decoders = {MessageDecoder.class})
public class WSServiceEndpoint
{
   @Inject SessionRegistry registry;
   @Inject MessageHandlerFactory handlerFactory;

   @OnMessage
   public void receiveMessage(Message msg, Session session)
   {
      handlerFactory.handleMessage(msg, session);
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
