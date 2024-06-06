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

public class FloodingSearchHandler extends AbstractHandler {
    public FloodingSearchHandler(final Socket clientSocket, final Node nodeContext, final Message message) {
        super(clientSocket, nodeContext, message);
    }

    @Override
    public void run() {
        try {
            final ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
            out.writeObject("SEARCH_OK");

        } catch (IOException e) {
            Logger.debug("Got exception: {}", e.getMessage());
        }

        if (verifyMessageAlreadyProcessed(message.getOrigin(), message.getSequenceNumber())) {
            Logger.info("Flooding: Mensagem repetida!");
            return;
        }

        trackMessage(message.getOrigin(), message.getSequenceNumber());
        nodeContext.getNodeStats().incrementFloodingSearchMessageAmount();

        final String key = message.getArgs().get(2);
        final int hopCount = Integer.parseInt(message.getArgs().get(3).replace("\n", ""));
        if (nodeContext.getValues().containsKey(key)) {
            Logger.info("Chave encontrada!");

            final Message responseMessage = createValMessage(nodeContext.getHostAddress(),
                    nodeContext.getSequenceNumber(), nodeContext.getTtl(), "FL",
                    key, nodeContext.getValues().get(key), hopCount);

            Outbox.sendMessage(responseMessage, message.getOrigin());

            return;
        }

        decrementMessageTtl(message);
        if (message.getTtl() < 1) {
            Logger.info("TTL igual a zero, descartando mensagem");
            return;
        }

        final List<String> oldArgs = message.getArgs();
        final int hostPort = nodeContext.getHostAddress().getPort();
        // LAST_HOP_PORT should contain value from the message sender or from the searcher?
        message.setArgs(List.of(oldArgs.get(0), Integer.toString(hostPort), oldArgs.get(2), Integer.toString(hopCount + 1)));
        Outbox.sendMessage(message, nodeContext.getNeighbors().stream()
                .filter(neighbor -> !neighbor.equals(message.getOrigin())).toList());
    }
}
