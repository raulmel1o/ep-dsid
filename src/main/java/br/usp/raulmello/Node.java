package br.usp.raulmello;

import br.usp.raulmello.inbound.Dispatcher;
import br.usp.raulmello.utils.Message;
import br.usp.raulmello.outbound.Outbox;
import br.usp.raulmello.utils.Address;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static br.usp.raulmello.ui.MenuWriter.*;
import static br.usp.raulmello.utils.MessageFactory.createHelloMessage;

@Log4j
@Getter
@Setter
public class Node {
    private Address hostAddress;

    private List<Address> neighbors;
    private Map<String, String> values;

    private int sequenceNumber;

    private Node(final String hostAddress, final int hostPort, final Map<String, String> values) {
        this.hostAddress = new Address(hostAddress, hostPort);
        this.neighbors = new ArrayList<>();
        this.values = values;
    }

    public static Node initNode(final String hostAddress, final int hostPort, final Map<String, String> values, final List<String> neighbors) {
        final Node node = new Node(hostAddress, hostPort, values);

        neighbors.forEach(neighbor -> {
            log.debug("Trying to HELLO neighbor: " + neighbor);
            final Address destAddress = new Address(neighbor);
            final Message message = createHelloMessage(node.getHostAddress(), node.getSequenceNumber());
            final boolean success = Outbox.sendMessage(message, destAddress);

            if (success) {
                node.neighbors.add(new Address(neighbor));
                log.debug("Neighbor added: " + neighbor);
            }
        });

        log.debug("Node initialized");
        return node;
    }

    public void startNode() {
        log.debug("Starting node");

        final Thread dispatcherThread = new Thread(new Dispatcher(hostAddress.getPort(), 100));
        dispatcherThread.start();

        handleUserInput();
    }

    private void handleUserInput() {
        final Scanner scanner = new Scanner(System.in);

        while (true) {
            showInitialMenu();
            final String inputOption = scanner.nextLine();

            if (inputOption.equals("0")) {
                showNeighbors(this.neighbors);
            }

            if (inputOption.equals("1")) {
                showNeighbors(this.neighbors);
                final Address neighbor = this.neighbors.get(Integer.parseInt(scanner.nextLine()));
                final Message message = createHelloMessage(this.getHostAddress(), this.sequenceNumber);
                Outbox.sendMessage(message, neighbor);
                this.sequenceNumber++;
            }

            if (inputOption.equals("2")) {
                showKeyInput();
                final String key = scanner.nextLine();

                if (this.values.containsKey(key)) {
                    showKeyIsInLocalStorage(key, this.values.get(key));
                } else {
                    // sendSearchMessage()
                }
            }

            if (inputOption.equals("9")) break;
        }
    }
}
