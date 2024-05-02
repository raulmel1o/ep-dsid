package br.usp.raulmello;

import br.usp.raulmello.inbound.Dispatcher;
import br.usp.raulmello.outbound.Message;
import br.usp.raulmello.outbound.MessageFactory;
import br.usp.raulmello.outbound.Outbox;
import br.usp.raulmello.ui.UiHandler;
import br.usp.raulmello.utils.Address;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Log4j
@Getter
@Setter
public class Node {
    private String hostAddress;
    private int hostPort;

    private List<Address> neighbors;
    private Map<String, String> values;

    private int sequenceNumber;

    private Node(final String hostAddress, final int hostPort, final Map<String, String> values) {
        this.hostAddress = hostAddress;
        this.hostPort = hostPort;
        this.neighbors = new ArrayList<>();
        this.values = values;
    }

    public static Node initNode(final String hostAddress, final int hostPort, final Map<String, String> values, final List<String> neighbors) {
        final Node node = new Node(hostAddress, hostPort, values);

        neighbors.forEach(neighbor -> {
            log.debug("Trying to HELLO neighbor: " + neighbor);
            final Address address = new Address(neighbor);

            final Message message = MessageFactory.createHelloMessage(address, node.getSequenceNumber());
            final boolean success = Outbox.sendMessage(message, address);

            if (success) {
                neighbors.add(neighbor);
                log.debug("Neighbor added: " + neighbor);
            }
        });

        log.debug("Node initialized");
        return node;
    }

    public void startNode() {
        log.debug("Starting node");
        final Thread dispatcherThread = new Thread(new Dispatcher(hostPort, 100));
        dispatcherThread.start();

        final UiHandler uiHandler = new UiHandler();
        uiHandler.showMenu();
    }
}
