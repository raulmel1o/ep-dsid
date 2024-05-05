package br.usp.raulmello.inbound.handlers;

import br.usp.raulmello.Node;
import br.usp.raulmello.ui.Logger;
import br.usp.raulmello.utils.Message;

import java.net.Socket;

public class ValHandler extends AbstractHandler {
    public ValHandler(final Socket client, final Node context, final Message message) {
        super(client, context, message);
    }

    @Override
    public void run() {
        Logger.info("Valor encontrado! Chave: {} valor: {}", message.getArgs().get(1), message.getArgs().get(2));
        trackMessage(message.getOrigin(), message.getSequenceNumber());
    }
}
