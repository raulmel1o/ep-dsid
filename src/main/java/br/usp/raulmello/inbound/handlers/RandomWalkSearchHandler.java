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
        final String key = message.getArgs().get(2);
        final int hopCount = Integer.parseInt(message.getArgs().get(3));
        if (nodeContext.getValues().containsKey(key)) {
            Logger.info("Chave encontrada!");

            try {
                final ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                out.flush();
                final Message responseMessage = createValMessage(nodeContext.getHostAddress(),
                        nodeContext.getSequenceNumber(), nodeContext.getTtl(), "RW",
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
        final int hostPort = nodeContext.getHostAddress().getPort();
        // LAST_HOP_PORT should contain value from the message sender or from the searcher?
        message.setArgs(List.of(oldArgs.get(0), Integer.toString(hostPort), oldArgs.get(2), Integer.toString(hopCount + 1)));

        final Address selectedNeighbor = nodeContext.getNeighbors().get(random.nextInt(nodeContext.getNeighbors().size()));
        Outbox.sendMessage(message, selectedNeighbor);
    }
}
