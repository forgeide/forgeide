/*
 * $Id: ServerLogicImpl.java 2839 2006-03-27 17:51:32Z sim $
 *
 * ace - a collaborative editor
 * Copyright (C) 2005 Mark Bigler, Simon Raess, Lukas Zbinden
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package ch.iserver.ace.collaboration.jupiter.server;

import org.jboss.logging.Logger;

import ch.iserver.ace.DocumentDetails;
import ch.iserver.ace.DocumentModel;
import ch.iserver.ace.algorithm.Algorithm;
import ch.iserver.ace.algorithm.jupiter.Jupiter;
import ch.iserver.ace.collaboration.InvitationCallback;
import ch.iserver.ace.collaboration.JoinRequest;
import ch.iserver.ace.collaboration.Participant;
import ch.iserver.ace.collaboration.RemoteUser;
import ch.iserver.ace.collaboration.jupiter.AcknowledgeStrategy;
import ch.iserver.ace.collaboration.jupiter.AcknowledgeStrategyFactory;
import ch.iserver.ace.collaboration.jupiter.AlgorithmWrapperImpl;
import ch.iserver.ace.collaboration.jupiter.JoinRequestImpl;
import ch.iserver.ace.collaboration.jupiter.NullAcknowledgeStrategyFactory;
import ch.iserver.ace.collaboration.jupiter.PublisherConnection;
import ch.iserver.ace.collaboration.jupiter.RemoteUserImpl;
import ch.iserver.ace.collaboration.jupiter.UserRegistry;
import ch.iserver.ace.collaboration.jupiter.server.document.SimpleServerDocument;
import ch.iserver.ace.net.DocumentServer;
import ch.iserver.ace.net.InvitationPort;
import ch.iserver.ace.net.ParticipantConnection;
import ch.iserver.ace.net.ParticipantPort;
import ch.iserver.ace.net.PortableDocument;
import ch.iserver.ace.net.RemoteUserProxy;
import ch.iserver.ace.util.ParameterValidator;
import ch.iserver.ace.util.ThreadDomain;

/**
 * Default implementation of the ServerLogic interface.
 */
public class ServerLogicImpl implements ServerLogic, FailureHandler, AccessControlStrategy {
	
	/**
	 * The logger used by instances of this class.
	 */
	private static final Logger LOG = Logger.getLogger(ServerLogicImpl.class);
	
	/**
	 * The CompositeForwarder used to forward events to other participants.
	 */
	private final CompositeForwarder compositeForwarder;
	
	/**
	 * The FailureHandler used to handle failures in this class.
	 */
	private final FailureHandler failureHandler;
		
	/**
	 * The DocumentServer object from the network layer.
	 */
	private DocumentServer server;
	
	/**
	 * ThreadDomain used to wrap all incoming operations.
	 */
	private final ThreadDomain incomingDomain;
	
	/**
	 * ThreadDomain used to wrap all outgoing operations.
	 */
	private final ThreadDomain outgoingDomain;
	
	/**
	 * The AccessControlStrategy used by this object.
	 */
	private AccessControlStrategy accessControlStrategy;
	
	/**
	 * The factory used to create AcknowledgeStrategy objects.
	 */
	private AcknowledgeStrategyFactory acknowledgeStrategyFactory = new NullAcknowledgeStrategyFactory();

	/**
	 * The connection to the publisher of the document.
	 */
	private PublisherConnection publisherConnection;
	
	/**
	 * The server copy of the current document. This document is sent to
	 * joining users.
	 */
	private final ServerDocument document;
	
	/**
	 * The user registry of the application.
	 */
	private final UserRegistry registry;
	
	/**
	 * Flag indicating whether this object is accepting joins.
	 */
	private boolean acceptingJoins;
		
	/**
	 * The ParticipantManager of the session.
	 */
	private final ParticipantManager participants;
			
	/**
	 * Creates a new ServerLogicImpl instance.
	 * 
	 * @param publisher the publisher of the document
	 * @param incomingDomain the incoming thread domain
	 * @param outgoingDomain the outgoing thread domain
	 * @param document the initial document model
	 * @param registry the user registry
	 */
	public ServerLogicImpl(RemoteUserProxy publisher,
		                   ThreadDomain incomingDomain,
	                       ThreadDomain outgoingDomain, 
	                       DocumentModel document,
	                       UserRegistry registry) {
		ParameterValidator.notNull("document", document);
		ParameterValidator.notNull("registry", registry);
		
		this.incomingDomain = incomingDomain;
		this.outgoingDomain = outgoingDomain;
		this.registry = registry;
		this.accessControlStrategy = this;

		this.document = createServerDocument(document, publisher);
		this.failureHandler = (FailureHandler) incomingDomain.wrap(this, FailureHandler.class);
		
		// add the document updater to the composite forwarder
		Forwarder forwarder = new DocumentUpdater(this.document);
		
		this.compositeForwarder = createForwarder(forwarder, this);
		this.participants = createParticipantManager(compositeForwarder);
	}

	/**
	 * Creates a new ParticipantManager for this session.
	 * 
	 * @return the newly created participant manager
	 */
	protected ParticipantManager createParticipantManager(CompositeForwarder forwarder) {
		return new ParticipantManagerImpl(forwarder);
	}
	
	/**
	 * Creates a new server document used to keep track of the document's
	 * content on the server side.
	 * 
	 * @param document the initial document content
	 * @param publisher the RemoteUserProxy of the publisher
	 * @return the server document ready to be used
	 */
	protected ServerDocument createServerDocument(DocumentModel document, RemoteUserProxy publisher) {
		ServerDocument doc;
		if (Boolean.getBoolean("newserverdocument")) {
			LOG.debug("using new server document");
			doc = new SimpleServerDocument();
			doc.participantJoined(ParticipantConnection.PUBLISHER_ID, null);
		} else {
			doc = new ServerDocumentImpl(publisher);
		}
		doc.insertString(ParticipantConnection.PUBLISHER_ID, 0, document.getContent());
		doc.updateCaret(ParticipantConnection.PUBLISHER_ID, document.getDot(), document.getMark());
		return doc;
	}
	
	/**
	 * Creates a new forwarder responsible to forward the results of the
	 * command processor to all other participants.
	 * 
	 * @param forwarder the default forwarder
	 * @param failureHandler the failure handler of the session
	 * @return the initialized forwarder
	 */
	protected CompositeForwarder createForwarder(Forwarder forwarder, FailureHandler failureHandler) {
		return new CompositeForwarderImpl(forwarder, failureHandler);
	}
		
	/**
	 * Creates the publisher port for the publisher of the session.
	 * 
	 * @param connection the connection to the publisher
	 * @return the publisher port used by the publisher to communicate with
	 *         the session
	 */
	protected PublisherPort createPublisherPort(ParticipantConnection connection) {
		Algorithm algorithm = new Jupiter(false);
		int participantId = ParticipantConnection.PUBLISHER_ID;
		PublisherPort port = new PublisherPortImpl(
						this, 
						this, 
						participantId, 
						new AlgorithmWrapperImpl(algorithm), 
						compositeForwarder);
		Forwarder forwarder = createForwarder(participantId, connection, algorithm);
		participants.addParticipant(participantId, forwarder, connection);
		return (PublisherPort) incomingDomain.wrap(port, PublisherPort.class, true);
	}
	
	/**
	 * Creates a new forwarder for a participant.
	 * 
	 * @param participantId the participant id of the new participant
	 * @param connection the connection to the participant
	 * @param algorithm the algorithm to be used for that participant
	 * @return the forwarder for that particular participant
	 */
	protected Forwarder createForwarder(int participantId, ParticipantConnection connection, Algorithm algorithm) {
		AcknowledgeStrategy acknowledger = getAcknowledgeStrategyFactory().createStrategy();
		ParticipantForwarder proxy = new ParticipantForwarder(participantId, algorithm, connection);
		proxy.setAcknowledgeStrategy(acknowledger);
		return proxy;
	}

	/**
	 * Initializes the publisher connection and returns a corresponding publisher port.
	 * 
	 * @param publisherConnection the connection to the publisher
	 * @return the port for the publisher
	 */
	public PublisherPort initPublisherConnection(PublisherConnection publisherConnection) {
		this.publisherConnection = (PublisherConnection) outgoingDomain.wrap(
						new PublisherConnectionWrapper(
								publisherConnection, getFailureHandler()),
						PublisherConnection.class);
		return createPublisherPort(this.publisherConnection);
	}
		
	/**
	 * @return whether this server logic is accepting joins
	 */
	protected boolean isAcceptingJoins() {
		return acceptingJoins;
	}
	
	/**
	 * @return the current copy of the server-side document
	 */
	protected PortableDocument getDocument() {
		return document.toPortableDocument();
	}
	
	/**
	 * @return the user registry of the application
	 */
	protected UserRegistry getUserRegistry() {
		return registry;
	}
	
	/**
	 * @return the document server of this session
	 */
	protected DocumentServer getDocumentServer() {
		return server;
	}
	
	/**
	 * Sets the document server of this session.
	 * 
	 * @param server the server of this session
	 */
	public void setDocumentServer(DocumentServer server) {
		ParameterValidator.notNull("server", server);
		this.server = server;
	}
	
	/**
	 * @return the participant manager of the server logic
	 */
	protected ParticipantManager getParticipantManager() {
		return participants;
	}
	
	/**
	 * @return the forwarder used to forward events
	 */
	protected Forwarder getCompositeForwarder() {
		return compositeForwarder;
	}
	
	/**
	 * @return the failure handler of this server logic
	 */
	protected FailureHandler getFailureHandler() {
		return failureHandler;
	}
		
	/**
	 * Starts the server logic. Unless this method is called, no joins are
	 * accepted.
	 */
	public void start() {
		acceptingJoins = true;
	}
	
	/**
	 * @return the connection object for the publisher of the session
	 */
	protected PublisherConnection getPublisherConnection() {
		return publisherConnection;
	}
	
	/**
	 * @return the current access control strategy of the session
	 */
	protected AccessControlStrategy getAccessControlStrategy() {
		return accessControlStrategy;
	}
	
	/**
	 * Sets the new access control strategy of the session.
	 * 
	 * @param strategy the new strategy
	 */
	public void setAccessControlStrategy(AccessControlStrategy strategy) {
		this.accessControlStrategy = strategy;
	}
	
	/**
	 * Sets the new AcknowledgeStrategyFactory object.
	 * 
	 * @param factory the factory for AcknowledgeStrategy objects
	 */
	public void setAcknowledgeStrategyFactory(AcknowledgeStrategyFactory factory) {
		this.acknowledgeStrategyFactory = factory;
	}
	
	/**
	 * @return the factory to create acknowledge strategy objects
	 */
	protected AcknowledgeStrategyFactory getAcknowledgeStrategyFactory() {
		return acknowledgeStrategyFactory;
	}
			
	// --> server logic methods <--

	/**
	 * @see ch.iserver.ace.net.DocumentServerLogic#join(ch.iserver.ace.net.ParticipantConnection)
	 */
	public void join(ParticipantConnection target) {
		ParameterValidator.notNull("target", target);
		LOG.info("--> join");
		String id = target.getUser().getId();

		if (!isAcceptingJoins()) {
			target.joinRejected(JoinRequest.SHUTDOWN);
		} else if (participants.isInvited(id)) {
			acceptJoin(target);
			participants.invitationAccepted(id);
		} else if (participants.isParticipant(id)) {
			target.joinRejected(JoinRequest.JOINED);
		} else if (participants.isBlackListed(id)) {
			target.joinRejected(JoinRequest.BLACKLISTED);
		} else if (participants.isJoining(id)) {
			target.joinRejected(JoinRequest.IN_PROGRESS);
		} else {
			ParticipantConnection connection = (ParticipantConnection) outgoingDomain.wrap(
						new ParticipantConnectionWrapper(
								target, 
								getFailureHandler()), 
						ParticipantConnection.class,
						true);
			RemoteUser user = getUserRegistry().getUser(id);
		
			if (user == null) {
				target.joinRejected(JoinRequest.UNKNOWN_USER);
			} else {
				participants.joinRequested(user.getId());
				ServerLogic wrapped = (ServerLogic) incomingDomain.wrap(this, ServerLogic.class);
				JoinRequest request = new JoinRequestImpl(wrapped, user, connection);
				getAccessControlStrategy().joinRequest(getPublisherConnection(), request);
			}
		}
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.ServerLogic#joinAccepted(ch.iserver.ace.net.ParticipantConnection)
	 */
	public void joinAccepted(ParticipantConnection connection) {
		LOG.info("--> join accepted");
		if (!isAcceptingJoins()) {
			LOG.info("join accepted by publisher but shutdown is in progress");
			participants.joinRequestRejected(connection.getUser().getId());
			connection.joinRejected(JoinRequest.SHUTDOWN);
		} else {
			acceptJoin(connection);
			participants.joinRequestAccepted(connection.getUser().getId());
		}
		LOG.info("<-- join accepted");
	}
	
	protected void acceptJoin(ParticipantConnection connection) {
		ParameterValidator.notNull("connection", connection);
		Algorithm algorithm = new Jupiter(false);
		int participantId = participants.getParticipantId(connection.getUser().getId());
		LOG.info("setting participant id " + participantId + " for user " + connection.getUser().getId());
		connection.setParticipantId(participantId);
		
		ParticipantPort portTarget = new ParticipantPortImpl(this, this, participantId, new AlgorithmWrapperImpl(algorithm), compositeForwarder);
		ParticipantPort port = (ParticipantPort) incomingDomain.wrap(portTarget, ParticipantPort.class);
		Forwarder forwarder = createForwarder(participantId, connection, algorithm);
		RemoteUserProxy user = connection.getUser();

		PortableDocument document = getDocument();
		participants.addParticipant(participantId, forwarder, connection);
		
		connection.joinAccepted(port);
		connection.sendDocument(document);
		compositeForwarder.sendParticipantJoined(participantId, user);
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.ServerLogic#joinRejected(ch.iserver.ace.net.ParticipantConnection)
	 */
	public void joinRejected(ParticipantConnection connection) {
		LOG.info("--> joinRejected");
		try {
			connection.joinRejected(JoinRequest.REJECTED);
		} finally {
			participants.joinRequestRejected(connection.getUser().getId());
		}
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.ServerLogic#setDocumentDetails(ch.iserver.ace.DocumentDetails)
	 */
	public void setDocumentDetails(DocumentDetails details) {
		LOG.info("--> setDocumentDetails");
		getDocumentServer().setDocumentDetails(details);
	}
	
	/**
	 * @see ServerLogic#leave(int)
	 */
	public void leave(int participantId) {
		LOG.info("--> leave");
		ParticipantConnection connection = participants.getConnection(participantId);
		if (connection == null) {
			LOG.warn("participant with id " + participantId + " not (or no longer) in session");
			return;
		}
		participants.participantLeft(participantId);
		compositeForwarder.sendParticipantLeft(participantId, Participant.LEFT);
		connection.close();
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.ServerLogic#kick(int)
	 */
	public void kick(int participantId) {
		LOG.info("--> kick");
		if (participantId == PUBLISHER_ID) {
			throw new IllegalArgumentException("cannot kick publisher of session");
		}
		LOG.info("kicking participant " + participantId);
		ParticipantConnection connection = participants.getConnection(participantId);
		if (connection == null) {
			LOG.info("participant with id " + participantId + " not (or no longer) in session");
		} else {
			participants.participantKicked(participantId);
			compositeForwarder.sendParticipantLeft(participantId, Participant.KICKED);
			connection.sendKicked();
			connection.close();
		}
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.ServerLogic#invite(ch.iserver.ace.collaboration.RemoteUser, InvitationCallback)
	 */
	public void invite(RemoteUser user, final InvitationCallback callback) {
		ParameterValidator.notNull("user", user);
		if (!participants.isInvited(user.getId())) {
			participants.userInvited(user.getId());
			final RemoteUserProxy proxy = ((RemoteUserImpl) user).getProxy();
			InvitationPort target = new InvitationPort() {
				public void reject() {
					invitationRejected(proxy);
					callback.invitationRejected();
				}
				public void accept(ParticipantConnection connection) {
					ParameterValidator.notNull("connection", connection);
					invitationAccepted(connection);
					callback.invitationAccepted();
				}
				public RemoteUserProxy getUser() {
					return proxy;
				}
			};
			InvitationPort port = (InvitationPort) incomingDomain.wrap(target, InvitationPort.class, true);
			getDocumentServer().invite(port);
		}
	}
	
	public void invitationRejected(RemoteUserProxy proxy) {
		ParameterValidator.notNull("proxy", proxy);
		participants.invitationRejected(proxy.getId());
	}
	
	public void invitationAccepted(ParticipantConnection target) {
		ParameterValidator.notNull("target", target);
		if (!isAcceptingJoins()) {
			target.joinRejected(JoinRequest.SHUTDOWN);
		} else {
			ParticipantConnection connection = (ParticipantConnection) outgoingDomain.wrap(
							new ParticipantConnectionWrapper(
									target, 
									getFailureHandler()), 
							ParticipantConnection.class,
							true);
			
			String userId = target.getUser().getId();
			RemoteUser user = getUserRegistry().getUser(userId);
			
			if (user == null) {
				target.joinRejected(JoinRequest.UNKNOWN_USER);
			} else {
				acceptJoin(connection);
				participants.invitationAccepted(userId);
			}
		}
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.ServerLogic#shutdown()
	 */
	public void shutdown() {
		LOG.info("--> shutdown");
		acceptingJoins = false;
		compositeForwarder.close();
		try {
			if (getDocumentServer() != null) {
				getDocumentServer().shutdown();
			}
		} finally {
			incomingDomain.dispose();
			LOG.info("<-- shutdown");
		}
	}
	
	// --> start FailureHandler methods <--
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.FailureHandler#handleFailure(int, int)
	 */
	public void handleFailure(int participantId, int reason) {
		LOG.info("handling failed connection to participant " + participantId);
		if (participantId == PUBLISHER_ID) {
			LOG.error("failure related to publisher: " + reason);
			getCompositeForwarder().close();
			shutdown();
		} else {
			participants.participantLeft(participantId);
			getCompositeForwarder().sendParticipantLeft(participantId, Participant.DISCONNECTED);
		}
	}
	
	// --> AccessControlStrategy implementation <--
	
	/**
	 * Default implementation of the AccessControlStrategy interface.
	 * 
	 * @param connection the publisher connection
	 * @param request the join request
	 */
	public void joinRequest(PublisherConnection connection, JoinRequest request) {
		connection.sendJoinRequest(request);
	}

}
