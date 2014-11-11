package org.forgeide.service.websockets;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.Session;

import org.forgeide.annotations.MessageOperation;
import org.forgeide.controller.GitHubRegistrationController;
import org.xwidgets.websocket.Message;
import org.xwidgets.websocket.MessageHandler;

/**
 * Handles various GitHub registration tasks
 *
 * @author Shane Bryzak
 */
@ApplicationScoped
@MessageHandler("github")
public class GitHubMessageHandler
{
   @Inject GitHubRegistrationController controller;

   @Inject SessionRegistry registry;

   @MessageOperation("generateState")
   public void generateState(Message msg, Session session)
   {
      String state = controller.generateState(session.getId());
      Message m = new Message(Message.CAT_GITHUB, Message.OP_GITHUB_STATE);
      m.setPayloadValue("state", state);
      session.getAsyncRemote().sendObject(m);
   }

}
