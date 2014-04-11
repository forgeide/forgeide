package org.forgeide.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.websocket.Session;

import org.forgeide.controller.ProjectController;
import org.forgeide.events.NewProjectEvent;
import org.forgeide.events.SessionCreatedEvent;

/**
 * Registers the websocket client sessions
 *
 * @author Shane Bryzak
 *
 */
@ApplicationScoped
public class SessionRegistry
{
   private List<Session> sessions = Collections.synchronizedList(new ArrayList<Session>());

   @Inject ProjectController projectController;

   @Inject Event<SessionCreatedEvent> sessionCreatedEvent;

   public void registerSession(Session session)
   {
      sessions.add(session);
      sessionCreatedEvent.fire(new SessionCreatedEvent(session));
   }

   public void unregisterSession(Session session)
   {
      sessions.remove(session);
   }

   public void broadcast(Message message)
   {
      for (Session session : sessions)
      {
         session.getAsyncRemote().sendObject(message);
      }
   }

   public void newProjectEventObserver(@Observes NewProjectEvent event)
   {
      Message m = new Message(Message.CAT_PROJECT, Message.OP_PROJECT_NEW);
      m.setPayloadValue("id", event.getProject().getId());
      m.setPayloadValue("name", event.getProject().getName());

      broadcast(m);
   }
}
