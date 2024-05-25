package br.usp.raulmello.inbound.handlers;

import br.usp.raulmello.Node;
import br.usp.raulmello.utils.Message;

import java.net.Socket;

public class DepthFirstSearchHandler extends AbstractHandler {

    public DepthFirstSearchHandler(final Socket client, final Node context, final Message message) {
        super(client, context, message);
    }

    @Override
    public void run() {

    }
}
