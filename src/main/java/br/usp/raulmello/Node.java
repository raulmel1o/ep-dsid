package br.usp.raulmello;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static br.usp.raulmello.MessageFactory.createHelloMessage;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.emptyList;

@Getter
@Setter
public class Node {
    private String address;
    private int port;

    private List<String> neighborsAddress;
    private Map<String, String> values;

    public Node(final String address, final int port, final Map<String, String> values) {
        this.address = address;
        this.port = port;
        this.neighborsAddress = emptyList();
        this.values = values;
    }

    public static Node initialize(final String host, final int port, final List<String> addresses, final Map<String, String> values) {
        final Node node = new Node(host, port, values);

        int sequenceNumber = 0;

        addresses.forEach(destAddress -> {
            try {
                final String[] splitAddress = destAddress.split(":");
                final Socket socket = new Socket(splitAddress[0], Integer.parseInt(splitAddress[1]));
                final boolean success = contactNeighbors(socket, host, port, splitAddress[0], Integer.parseInt(splitAddress[1]), sequenceNumber);

                if (success) {
                    node.neighborsAddress.add(destAddress);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static boolean contactNeighbors(final Socket socket, final String hostAddress, final int hostPort, final String destAddress, final int destPort, int sequenceNumber) {
        System.out.println("Tentando adicionar vizinho " + destAddress + ":" + destPort);
        final String helloMessage = createHelloMessage(hostAddress + ":" + hostPort, sequenceNumber, 1, "HELLO", emptyList());
        System.out.println("Encaminhando mensagem \"" + helloMessage + "\" para " + destAddress + ":" + destPort);
        final String response = sendMessage(socket,helloMessage);

        if (response.isAck()) {
            System.out.println("Envio feito com sucesso: \"" + helloMessage + "\"");
            return true;
        }

        return false;
    }

    private static void sendHelloMessage(final Socket socket, final String helloMessage) {

    }

    private static void sendMessage(final Socket socket, final String message) {
        try {
            socket.getOutputStream().write(message.getBytes(UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
