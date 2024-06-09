package br.usp.raulmello.inbound.handlers;

import br.usp.raulmello.DfsContext;
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

public class DepthFirstSearchHandler extends AbstractHandler {

    private final Random random = new Random();

    public DepthFirstSearchHandler(final Socket client, final Node context, final Message message) {
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

        final String key = message.getArgs().get(2);
        final int hopCount = extractHopCountFromMessage(message);
        if (nodeContext.getValues().containsKey(key)) {
            Logger.info("Chave encontrada!");

            final Message responseMessage = createValMessage(nodeContext.getHostAddress(),
                    nodeContext.getSequenceNumber(), nodeContext.getTtl(), "BP",
                    key, nodeContext.getValues().get(key), hopCount);

            Outbox.sendMessage(responseMessage, message.getOrigin());

            return;
        }

        decrementMessageTtl(message);
        if (message.getTtl() < 1) {
            Logger.info("TTL igual a zero, descartando mensagem");
            return;
        }

        final DfsContext dfsContext = nodeContext.getDfsContext();
        dfsContext.setPreviousNode(new Address(clientSocket.getInetAddress().getHostAddress(), Integer.parseInt(message.getArgs().get(1))));
        if (!verifyMessageAlreadyProcessed(message.getOrigin(), message.getSequenceNumber())  && !nodeContext.getHostAddress().equals(message.getOrigin())) {
            dfsContext.setMotherNode(message.getOrigin());
            final List<Address> eligibleNeighbors = nodeContext.getNeighbors();
            eligibleNeighbors.remove(dfsContext.getPreviousNode());
            dfsContext.setEligibleNeighbors(eligibleNeighbors);
        }
        trackMessage(message.getOrigin(), message.getSequenceNumber());

        if (dfsContext.getMotherNode().equals(nodeContext.getHostAddress()) && dfsContext.getActiveNeighbor().equals(dfsContext.getPreviousNode()) && dfsContext.getEligibleNeighbors().isEmpty()) {
            Logger.info("BP:Nao foi possivel localizar a chave {}", message.getArgs().get(2));
            return;
        }

        if (dfsContext.getActiveNeighbor() != null && !dfsContext.getActiveNeighbor().equals(dfsContext.getPreviousNode())) {
            Logger.info("BP: ciclo detectado, devolvendo a mensagem...");
            dfsContext.setNextNode(dfsContext.getPreviousNode());
        } else if (dfsContext.getEligibleNeighbors().isEmpty()) {
            Logger.info("BP: nenhum vizinho encontrou a chave, retrocedendo...");
            dfsContext.setNextNode(dfsContext.getPreviousNode()); // Should I get back to previous or mother
        } else {
            final Address nextNode = dfsContext.getEligibleNeighbors().get(random.nextInt(dfsContext.getEligibleNeighbors().size()));
            dfsContext.setNextNode(nextNode);
            dfsContext.setActiveNeighbor(nextNode);
            dfsContext.setEligibleNeighbors(dfsContext.getEligibleNeighbors().stream().filter(n -> n != nextNode).toList());
        }

        final List<String> oldArgs = message.getArgs();
        final int hostPort = nodeContext.getHostAddress().getPort();
        message.setArgs(List.of(oldArgs.get(0), Integer.toString(hostPort), oldArgs.get(2), Integer.toString(hopCount + 1)));

        Outbox.sendMessage(message, dfsContext.getNextNode());
    }
}
