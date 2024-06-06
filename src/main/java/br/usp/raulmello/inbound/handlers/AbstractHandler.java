package br.usp.raulmello.inbound.handlers;

import br.usp.raulmello.Node;
import br.usp.raulmello.utils.Address;
import br.usp.raulmello.utils.Message;

import java.net.Socket;

public abstract class AbstractHandler implements Runnable {
    protected final Socket clientSocket;
    protected final Node nodeContext;
    protected final Message message;

    protected AbstractHandler(final Socket clientSocket, final Node nodeContext, final Message message) {
        this.clientSocket = clientSocket;
        this.nodeContext = nodeContext;
        this.message = message;
    }

    protected void trackMessage(final Address origin, final int sequenceNumber) {
        nodeContext.getSequenceNumberTracker().put(origin, sequenceNumber);
    }

    protected boolean verifyMessageAlreadyProcessed(final Address origin, final int sequenceNumber) {
        final var neighborSequenceNumber = nodeContext.getSequenceNumberTracker().get(origin);

        return neighborSequenceNumber != null && neighborSequenceNumber >= sequenceNumber;
    }

    protected void decrementMessageTtl(final Message message) {
        message.setTtl(message.getTtl() - 1);
    }
}
