package org.forgeide.service.websockets;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.Session;

import org.forgeide.controller.GitHubRegistrationController;
import org.xwidgets.websocket.Message;
import org.xwidgets.websocket.MessageHandler;
import org.xwidgets.websocket.SessionRegistry;

/**
 * Handles various GitHub registration tasks
 *
 * @author Shane Bryzak
 */
@ApplicationScoped
public class GitHubMessageHandler
{
   @Inject GitHubRegistrationController controller;

   @Inject SessionRegistry registry;

   @MessageHandler("github.generateState")
   public void generateState(Message msg, Session session)
   {
      String state = controller.generateState(session.getId());
      Message m = new Message("github.state");
      m.setPayloadValue("state", state);
      m.setPayloadValue("redirectUri", "https://forgeide.org/github_auth_callback.html");
      session.getAsyncRemote().sendObject(m);
   }

}
