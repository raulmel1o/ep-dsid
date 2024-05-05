package br.usp.raulmello.ui;

import br.usp.raulmello.Node;
import br.usp.raulmello.outbound.Outbox;
import br.usp.raulmello.utils.Address;
import br.usp.raulmello.utils.Message;

import java.util.Random;
import java.util.Scanner;

import static br.usp.raulmello.ui.MenuWriter.*;
import static br.usp.raulmello.utils.MessageFactory.*;

public class MenuHandler {

    private final Node nodeContext;
    private final Scanner scanner = new Scanner(System.in);
    private final Random random = new Random();

    public MenuHandler(final Node nodeContext) {
        this.nodeContext = nodeContext;
    }

    public void handle() {
        boolean running = true;
        while (running) {
            showInitialMenu();
            final String inputOption = scanner.nextLine();

            switch (inputOption) {
                case "0" -> showNeighbors(nodeContext.getNeighbors());
                case "1" -> handleHello();
                case "2" -> handleFloodingSearch();
                case "3" -> handleRandomWalkSearch();
                case "9" -> running = false;
                default -> Logger.debug("Invalid option: {}", inputOption);
            }
        }
    }

    private void handleHello() {
        showNeighbors(nodeContext.getNeighbors());
        final Address neighbor = nodeContext.getNeighbors().get(Integer.parseInt(scanner.nextLine()));
        final Message message = createHelloMessage(nodeContext.getHostAddress(), nodeContext.getSequenceNumber());
        Outbox.sendMessage(message, neighbor);
        nodeContext.setSequenceNumber(nodeContext.getSequenceNumber() + 1);
    }

    private void handleFloodingSearch() {
        showKeyInput();
        final String key = scanner.nextLine();

        if (nodeContext.getValues().containsKey(key)) {
            showKeyIsInLocalStorage(key, nodeContext.getValues().get(key));
        } else {
            final Message message = createSearchFloodingMessage(key, nodeContext.getHostAddress(), nodeContext.getSequenceNumber(), nodeContext.getTtl());
            Outbox.sendMessage(message, nodeContext.getNeighbors());
            nodeContext.setSequenceNumber(nodeContext.getSequenceNumber() + 1);
        }
    }

    private void handleRandomWalkSearch() {
        showKeyInput();
        final String key = scanner.nextLine();

        if (nodeContext.getValues().containsKey(key)) {
            showKeyIsInLocalStorage(key, nodeContext.getValues().get(key));
        } else {
            final Message message = createSearchRandomWalkMessage(key, nodeContext.getHostAddress(), nodeContext.getSequenceNumber(), nodeContext.getTtl());
            final Address selectedNeighbor = nodeContext.getNeighbors().get(random.nextInt(nodeContext.getNeighbors().size()));
            Outbox.sendMessage(message, selectedNeighbor);
            nodeContext.setSequenceNumber(nodeContext.getSequenceNumber() + 1);
        }
    }
}
