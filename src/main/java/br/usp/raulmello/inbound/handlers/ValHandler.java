package br.usp.raulmello.inbound.handlers;

import br.usp.raulmello.Node;
import br.usp.raulmello.ui.Logger;
import br.usp.raulmello.utils.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ValHandler extends AbstractHandler {
    public ValHandler(final Socket client, final Node context, final Message message) {
        super(client, context, message);
    }

    @Override
    public void run() {
        try {
            final ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
            out.writeObject("VAL_OK");

        } catch (IOException e) {
            Logger.debug("Got exception: {}", e.getMessage());
        }

        Logger.info("Mensagem recebida: \"{}\"", message.toString().replace("\n", ""));
        Logger.info("\tValor encontrado!");
        Logger.info("\t\tchave: {} valor: {}", message.getArgs().get(1), message.getArgs().get(2));
        trackMessage(message.getOrigin(), message.getSequenceNumber());

        nodeContext.getNodeStats().incrementSearchHopAmount(message.getArgs().get(0), Integer.parseInt(message.getArgs().get(3).replaceAll("\n", "")));
    }
}
