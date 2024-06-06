package br.usp.raulmello.inbound.handlers;

import br.usp.raulmello.Node;
import br.usp.raulmello.outbound.Outbox;
import br.usp.raulmello.ui.Logger;
import br.usp.raulmello.utils.Address;
import br.usp.raulmello.utils.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Random;

import static br.usp.raulmello.utils.MessageFactory.createValMessage;

public class RandomWalkSearchHandler extends AbstractHandler {
    private final Random random = new Random();

    public RandomWalkSearchHandler(final Socket client, final Node context, final Message message) {
        super(client, context, message);
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

        nodeContext.getNodeStats().incrementRandomWalkSearchMessageAmount();

        final String key = message.getArgs().get(2);
        final int hopCount = extractHopCountFromMessage(message);
        if (nodeContext.getValues().containsKey(key)) {
            Logger.info("Chave encontrada!");

            final Message responseMessage = createValMessage(nodeContext.getHostAddress(),
                    nodeContext.getSequenceNumber(), nodeContext.getTtl(), "RW",
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

        final Address selectedNeighbor = nodeContext.getNeighbors().get(random.nextInt(nodeContext.getNeighbors().size()));
        Outbox.sendMessage(message, selectedNeighbor);
    }
}
