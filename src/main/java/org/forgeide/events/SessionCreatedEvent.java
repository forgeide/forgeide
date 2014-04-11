package org.forgeide.events;

import javax.websocket.Session;

/**
 * This event is fired whenever a new websockets session is created
 *
 * @author Shane Bryzak
 *
 */
public class SessionCreatedEvent
{
   private Session session;

   public SessionCreatedEvent(Session session)
   {
      this.session = session;
   }

   public Session getSession()
   {
      return session;
   }
}
