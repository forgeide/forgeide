package ch.iserver.ace.collaboration.jupiter.server;

import ch.iserver.ace.CaretUpdate;
import ch.iserver.ace.algorithm.Operation;
import ch.iserver.ace.net.RemoteUserProxy;

/**
 * A Forwarder is used on the server to forward incoming events to
 * participants. It is responsible to generate requests to be
 * sent to participants.
 */
public interface Forwarder {

	/**
	 * Sends a caret <var>update</var> received from the participant
	 * with the given <var>participantId</var> to the forwarder.
	 * 
	 * @param participantId the participant id of the participant that sent
	 *                      the caret update
	 * @param update the caret update
	 */
	void sendCaretUpdate(int participantId, CaretUpdate update);

	/**
	 * Sends an Operation received from the participant with the given
	 * <var>participantId</var> to the forwarder.
	 * 
	 * @param participantId the participant id of the sender
	 * @param op the operation
	 */
	void sendOperation(int participantId, Operation op);

	/**
	 * Sends a message to the forwarder that the given participant left the
	 * session.
	 * 
	 * @param participantId the participant that left the session
	 * @param reason the reason why the participant left
	 */
	void sendParticipantLeft(int participantId, int reason);

	/**
	 * Sends a message to the forwarder that a new user joined the session.
	 * 
	 * @param participantId the participant id of the joined user
	 * @param user the proxy of the joined user
	 */
	void sendParticipantJoined(int participantId, RemoteUserProxy user);

	/**
	 * Closes the forwarder. No methods should be invoked after this method
	 * on the forwarder.
	 */
	void close();

}
