package org.forgeide.service.websockets;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.websocket.Session;

import org.forgeide.events.NewProjectEvent;
import org.forgeide.events.NewResourceEvent;
import org.xwidgets.websocket.Message;

/**
 * Registers websocket client sessions
 *
 * @author Shane Bryzak
 *
 */
@ApplicationScoped
public class SessionRegistry
{
   private Map<String,Session> sessions = Collections.synchronizedMap(new HashMap<String,Session>());

   public void registerSession(Session session)
   {
      sessions.put(session.getId(), session);
   }

   public Session getSession(String id)
   {
      return sessions.get(id);
   }

   public void unregisterSession(Session session)
   {
      sessions.remove(session);
   }

   public void sendMessage(Session session, Message msg)
   {
      for (Session s : sessions.values())
      {
         if (s.equals(session)) 
         {
            s.getAsyncRemote().sendObject(msg);
            break;
         }
      }
   }

   public void broadcastMessage(Message message)
   {
      for (Session session : sessions.values())
      {
         session.getAsyncRemote().sendObject(message);
      }
   }

   public void newProjectEventObserver(@Observes NewProjectEvent event)
   {
      Message m = new Message(Message.CAT_PROJECT, Message.OP_PROJECT_NEW);
      m.setPayloadValue("project", event.getProject());
      broadcastMessage(m);
   }

   public void newProjectResourceEventObserver(@Observes NewResourceEvent event)
   {
      Message m = new Message(Message.CAT_RESOURCE, Message.OP_RESOURCE_NEW);
      m.setPayloadValue("resource", event.getResource());
      broadcastMessage(m);
   }
}
