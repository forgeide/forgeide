package org.forgeide.service.websockets;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.Session;

import org.forgeide.annotations.MessageHandler;
import org.forgeide.annotations.MessageOperation;
import org.forgeide.controller.ResourceController;

/**
 * Message handler for resource operations
 *
 * @author Shane Bryzak
 */
@ApplicationScoped
@MessageHandler("resource")
public class ResourceMessageHandler
{
   @Inject ResourceController controller;

   @MessageOperation("open")
   public void handleOpen(Message msg, Session session)
   {
      Long resourceId = Long.valueOf(msg.getPayload().get("id").toString());
      controller.openResource(resourceId, session);
   }
}
