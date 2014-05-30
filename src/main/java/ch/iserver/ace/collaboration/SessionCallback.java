package ch.iserver.ace.collaboration;

import ch.iserver.ace.CaretUpdate;
import ch.iserver.ace.algorithm.Operation;
import ch.iserver.ace.util.Lock;

/**
 * Callback interface used by sessions to notify the application about events
 * in the session. This interface groups all the operations common to published
 * sessions and participant sessions.
 */
public interface SessionCallback {
	
	/**
	 * Gets the lock that should be used by the Session before transforming
	 * requests from the network.
	 * 
	 * @return the Lock to guard access to the transformation engine
	 */
	Lock getLock();
	
	/**
	 * Sets the participant id of the local participant. This method should
	 * be the first called method.
	 * 
	 * @param participantId the participant id
	 */
	void setParticipantId(int participantId);
	
	/**
	 * Notifies the session callback that a new user joined the Session.
	 * 
	 * @param participant the participant that joined
	 */
	void participantJoined(Participant participant);

	/**
	 * Notifies the session callback that a user left the Session.
	 * 
	 * @param participant the participant that left the Session
	 * @param code the reason why the participant left
	 */
	void participantLeft(Participant participant, int code);

	/**
	 * Receives an operation from the given participant.
	 * 
	 * @param participant the participant that sent the operation
	 * @param operation the operation to be applied to the document
	 */
	void receiveOperation(Participant participant, Operation operation);

	/**
	 * Receives a caret update from the given participant.
	 * 
	 * @param participant the participant that sent the CaretUpdate
	 * @param update the caret update specification
	 */
	void receiveCaretUpdate(Participant participant, CaretUpdate update);

	/**
	 * Notifies the callback that the session failed. The reason code
	 * gives some more detailed information why the session failed.
	 * 
	 * @param reason the reason code (defined in {@link Session} interface)
	 * @param e the cause of the failure
	 */
	void sessionFailed(int reason, Exception e);

}
