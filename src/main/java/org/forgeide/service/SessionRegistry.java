package org.forgeide.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;

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

   public void registerSession(Session session)
   {
      sessions.add(session);
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
/*
   public void sendAll(String msg)
   {
      for (Session s : sessions)
      {
         s.getAsyncRemote().sendText(msg);
      }
   }*/
}