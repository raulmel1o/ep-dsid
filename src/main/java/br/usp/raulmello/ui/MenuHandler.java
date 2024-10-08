package br.usp.raulmello.ui;

import br.usp.raulmello.DfsContext;
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

    private boolean running = true;

    public MenuHandler(final Node nodeContext) {
        this.nodeContext = nodeContext;
    }

    public void handle() {
        while (running) {
            showInitialMenu();
            final String inputOption = scanner.nextLine();

            switch (inputOption) {
                case "0" -> showNeighbors(nodeContext.getNeighbors());
                case "1" -> handleHello();
                case "2" -> handleFloodingSearch();
                case "3" -> handleRandomWalkSearch();
                case "4" -> handleDepthFirstSearch();
                case "5" -> handleStats();
                case "6" -> handleUpdateDefaultTtl();
                case "9" -> handleExit();
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
            nodeContext.getNodeStats().incrementSentFloodingSearchMessageAmount();
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
            nodeContext.getNodeStats().incrementSentRandomWalkSearchMessageAmount();
        }
    }

    private void handleDepthFirstSearch() {
        showKeyInput();
        final String key = scanner.nextLine();

        if (nodeContext.getValues().containsKey(key)) {
            showKeyIsInLocalStorage(key, nodeContext.getValues().get(key));
        } else {
            // define DFS context
            final Address nextNode = nodeContext.getNeighbors().get(random.nextInt(nodeContext.getNeighbors().size()));

            final DfsContext dfsContext = nodeContext.getDfsContext();
            dfsContext.setMotherNode(nodeContext.getHostAddress());
            dfsContext.setNextNode(nextNode);
            dfsContext.setActiveNeighbor(nextNode);
            dfsContext.setEligibleNeighbors(nodeContext.getNeighbors().stream().filter(n -> n != nextNode).toList());

            final Message message = createSearchDepthFirstMessage(key, nodeContext.getHostAddress(), nodeContext.getSequenceNumber(), nodeContext.getTtl());

            Outbox.sendMessage(message, nextNode);
            nodeContext.setSequenceNumber(nodeContext.getSequenceNumber() + 1);
            nodeContext.getNodeStats().incrementSentDepthFirstSearchMessageAmount();
        }
    }

    private void handleStats() {
        Logger.info(nodeContext.getNodeStats().toString());
    }

    private void handleUpdateDefaultTtl() {
        showDefaultTtlInput();
        final String ttl = scanner.nextLine();

        nodeContext.setTtl(Integer.parseInt(ttl));
    }

    private void handleExit() {
        showExitText();

        final Message message = createByeMessage(nodeContext.getHostAddress(), nodeContext.getSequenceNumber());
        Outbox.sendMessage(message, nodeContext.getNeighbors());
        running = false;
    }
}
