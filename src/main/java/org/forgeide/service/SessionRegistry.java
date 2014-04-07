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

   public List<Session> getSessions()
   {
      return Collections.unmodifiableList(sessions);
   }
}
