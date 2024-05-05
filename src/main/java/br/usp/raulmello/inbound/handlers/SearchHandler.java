package br.usp.raulmello.inbound.handlers;

import br.usp.raulmello.Node;
import br.usp.raulmello.outbound.Outbox;
import br.usp.raulmello.ui.Logger;
import br.usp.raulmello.utils.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import static br.usp.raulmello.utils.MessageFactory.createValMessage;

public class SearchHandler extends AbstractHandler {
    public SearchHandler(final Socket clientSocket, final Node nodeContext, final Message message) {
        super(clientSocket, nodeContext, message);
    }

    @Override
    public void run() {
        if (verifyMessageAlreadyProcessed(message.getOrigin(), message.getSequenceNumber())) {
            Logger.info("Flooding: Mensagem repetida!");
            return;
        }

        trackMessage(message.getOrigin(), message.getSequenceNumber());

        final String key = message.getArgs().get(2);
        final int hopCount = Integer.parseInt(message.getArgs().get(3));
        if (nodeContext.getValues().containsKey(key)) {
            Logger.info("Chave encontrada!");

            try {
                final ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                out.flush();
                final Message responseMessage = createValMessage(nodeContext.getHostAddress(),
                        nodeContext.getSequenceNumber(), nodeContext.getTtl(), "FL",
                        key, nodeContext.getValues().get(key), hopCount);

                out.writeObject(responseMessage.toString());
            } catch (IOException e) {
                Logger.debug("Got exception: {}", e.getMessage());
            }

            return;
        }

        decrementMessageTtl(message);
        if (message.getTtl() < 1) {
            Logger.info("TTL igual a zero, descartando mensagem");
            return;
        }

        final List<String> oldArgs = message.getArgs();
        message.setArgs(List.of(oldArgs.get(0), oldArgs.get(1), oldArgs.get(2), Integer.toString(hopCount + 1)));
        Outbox.sendMessage(message, nodeContext.getNeighbors().stream()
                .filter(neighbor -> !neighbor.equals(message.getOrigin())).toList());
    }
}
