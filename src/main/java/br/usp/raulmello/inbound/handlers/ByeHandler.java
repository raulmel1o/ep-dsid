package br.usp.raulmello.inbound.handlers;

import br.usp.raulmello.Node;
import br.usp.raulmello.ui.Logger;
import br.usp.raulmello.utils.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class ByeHandler extends AbstractHandler {
    public ByeHandler(final Node nodeContext, final Message message) {
        super(null, nodeContext, message);
    }

    @Override
    public void run() {
        try {
            final ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
            out.writeObject("BYE_OK");
        } catch (IOException e) {
            Logger.debug("Got exception: {}", e.getMessage());
        }

        Logger.info("Mensagem recebida: \"{}\"", message);
        nodeContext.getNeighbors().remove(message.getOrigin());
        Logger.info("Removendo vizinho da tabela {}", message.getOrigin());
    }
}
