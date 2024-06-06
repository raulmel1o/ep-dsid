package br.usp.raulmello.inbound.handlers;

import br.usp.raulmello.Node;
import br.usp.raulmello.ui.Logger;
import br.usp.raulmello.utils.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class HelloHandler extends AbstractHandler {
    public HelloHandler(final Socket clientSocket, final Node nodeContext, final Message message) {
        super(clientSocket, nodeContext, message);
    }

    @Override
    public void run() {
        Logger.info("Mensagem recebida: \"{}\"", message.toString().replace("\n", ""));
        if (nodeContext.getNeighbors().contains(message.getOrigin())) {
            Logger.info("\tVizinho ja esta na tabela: {}\n", message.getOrigin());
        } else {
            nodeContext.getNeighbors().add(message.getOrigin());
            Logger.info("\tAdicionando vizinho na tabela: {}\n", message.getOrigin());
        }

        trackMessage(message.getOrigin(), message.getSequenceNumber());

        try {
            final ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
            out.writeObject("HELLO_OK");
        } catch (IOException e) {
            Logger.debug("Got exception: {}", e.getMessage());
        }
    }
}
