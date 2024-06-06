package br.usp.raulmello;

import br.usp.raulmello.inbound.Dispatcher;
import br.usp.raulmello.ui.Logger;
import br.usp.raulmello.ui.MenuHandler;
import br.usp.raulmello.utils.Message;
import br.usp.raulmello.outbound.Outbox;
import br.usp.raulmello.utils.Address;
import br.usp.raulmello.utils.NodeStats;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static br.usp.raulmello.utils.MessageFactory.createHelloMessage;

@Getter
@Setter
public class Node {
    private Address hostAddress;
    private List<Address> neighbors;
    private Map<String, String> values;
    private Map<Address, Integer> sequenceNumberTracker;
    private NodeStats nodeStats;
    private int sequenceNumber;
    private int ttl;

    private Node(final String hostAddress, final int hostPort, final Map<String, String> values) {
        this.hostAddress = new Address(hostAddress, hostPort);
        this.neighbors = new ArrayList<>();
        this.values = values;
        this.sequenceNumberTracker = new HashMap<>();
        this.nodeStats = new NodeStats();
        this.sequenceNumber = 1;
        this.ttl = 100;
    }

    public static Node initNode(final String hostAddress, final int hostPort, final Map<String, String> values, final List<String> neighbors) {
        final Node node = new Node(hostAddress, hostPort, values);
        Logger.info("Servidor criado: {}:{}\n", hostAddress, hostPort);

        neighbors.forEach(neighbor -> {
            Logger.info("Tentando adicionar vizinho {}", neighbor);
            final Address destAddress = new Address(neighbor);
            final Message message = createHelloMessage(node.getHostAddress(), node.getSequenceNumber());
            final boolean success = Outbox.sendMessage(message, destAddress);
            node.setSequenceNumber(node.getSequenceNumber() + 1);

            if (success) {
                node.neighbors.add(new Address(neighbor));
                Logger.debug("Neighbor added: {}", neighbor);
            }
        });

        values.forEach((key, value) -> Logger.info("Adicionando par ({}, {}) na tabela local\n", key, value));

        Logger.debug("Node initialized");
        return node;
    }

    public void startNode() {
        Logger.debug("Starting node");

        final Thread dispatcherThread = new Thread(new Dispatcher(hostAddress.getPort(), 100, this));
        dispatcherThread.start();

        final MenuHandler menuHandler = new MenuHandler(this);
        menuHandler.handle();
    }
}
