package org.forgeide.service.websockets;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.Session;

import org.forgeide.controller.ResourceController;
import org.xwidgets.websocket.Message;
import org.xwidgets.websocket.MessageHandler;

/**
 * Message handler for resource operations
 *
 * @author Shane Bryzak
 */
@ApplicationScoped
public class ResourceMessageHandler
{
   @Inject ResourceController controller;

   @MessageHandler("resource.open")
   public void handleOpen(Message msg, Session session)
   {
      Long resourceId = Long.valueOf(msg.getPayload().get("id").toString());
      controller.openResource(resourceId, session);
   }
}
