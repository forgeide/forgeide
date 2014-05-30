package org.forgeide.ace;

import ch.iserver.ace.CaretUpdateMessage;
import ch.iserver.ace.algorithm.Request;
import ch.iserver.ace.algorithm.Timestamp;
import ch.iserver.ace.net.ParticipantConnection;
import ch.iserver.ace.net.ParticipantPort;
import ch.iserver.ace.net.PortableDocument;
import ch.iserver.ace.net.RemoteUserProxy;

/**
 *
 * @author Shane Bryzak
 *
 */
public class ParticipantConnectionImpl implements ParticipantConnection
{

   @Override
   public void setParticipantId(int participantId)
   {
      System.out.println("### setParticipantId");
   }

   @Override
   public void joinAccepted(ParticipantPort port)
   {
      System.out.println("### joinAccepted");
   }

   @Override
   public void joinRejected(int code)
   {
      System.out.println("### joinRejected");
   }

   @Override
   public RemoteUserProxy getUser()
   {
      System.out.println("### getUser");
      return null;
   }

   @Override
   public void sendDocument(PortableDocument document)
   {
      System.out.println("### sendDocument");
   }

   @Override
   public void sendRequest(int participantId, Request request)
   {
      System.out.println("### sendRequest");
   }

   @Override
   public void sendCaretUpdateMessage(int participantId, CaretUpdateMessage message)
   {
      System.out.println("### sendCaretUpdateMessage");
   }

   @Override
   public void sendAcknowledge(int siteId, Timestamp timestamp)
   {
      System.out.println("### sendAcknowledge");
   }

   @Override
   public void sendParticipantJoined(int participantId, RemoteUserProxy proxy)
   {
      System.out.println("### sendParticipantJoined");
   }

   @Override
   public void sendParticipantLeft(int participantId, int reason)
   {
      System.out.println("### sendParticipantLeft");
   }

   @Override
   public void sendKicked()
   {
      System.out.println("### sendKicked");
   }

   @Override
   public void close()
   {
      System.out.println("### close");
   }

}
