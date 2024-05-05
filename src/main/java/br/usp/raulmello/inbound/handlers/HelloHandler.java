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
        Logger.info("Mensagem recebida: \"{}\"", message);
        if (nodeContext.getNeighbors().contains(message.getOrigin())) {
            Logger.info("Vizinho ja esta na tabela: {}", message.getOrigin());
        } else {
            nodeContext.getNeighbors().add(message.getOrigin());
            Logger.info("Adicionando vizinho na tabela: {}", message.getOrigin());
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
