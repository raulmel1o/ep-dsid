package br.usp.raulmello.inbound.handlers;

import br.usp.raulmello.Node;
import br.usp.raulmello.ui.Logger;
import br.usp.raulmello.utils.Message;

public class ByeHandler extends AbstractHandler {
    public ByeHandler(final Node nodeContext, final Message message) {
        super(null, nodeContext, message);
    }

    @Override
    public void run() {
        Logger.info("Mensagem recebida: \"{}\"", message);
        nodeContext.getNeighbors().remove(message.getOrigin());
        Logger.info("Removendo vizinho da tabela {}", message.getOrigin());
    }
}
