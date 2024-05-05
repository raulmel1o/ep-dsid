package br.usp.raulmello.inbound.handlers;

import br.usp.raulmello.Node;
import br.usp.raulmello.utils.Message;

import java.net.Socket;

public class SearchHandler implements Runnable {
    private final Socket clientSocket;
    private final Node nodeContext;
    private final Message message;

    public SearchHandler(final Socket clientSocket, final Node nodeContext, final Message message) {
        this.clientSocket = clientSocket;
        this.nodeContext = nodeContext;
        this.message = message;
    }

    @Override
    public void run() {

    }
}
