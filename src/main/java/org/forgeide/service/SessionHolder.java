package org.forgeide.service;

import javax.enterprise.context.SessionScoped;
import javax.websocket.Session;

/**
 * Stores the client's websocket Session in session scope
 *
 * @author Shane Bryzak
 */
@SessionScoped
public class SessionHolder
{
   private Session session;

   public Session getSession()
   {
      return session;
   }

   public void setSession(Session session)
   {
      this.session = session;
   }
}
