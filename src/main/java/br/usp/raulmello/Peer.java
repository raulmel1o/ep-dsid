package br.usp.raulmello;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Peer {
    private String address;
    private int port;

    // FIXME: Include only valid neighbors on this list
    private List<Peer> neighbors;
    private Map<String, String> values;

    public Peer(final String line) {
        this.address = line.split(":")[0];
        this.port = Integer.parseInt(line.split(":")[1]);
    }

    public Peer(final String address, final int port) {
        this.address = address;
        this.port = port;
    }

    public Peer(final String address, final int port, final List<Peer> neighbors, final Map<String, String> values) {
        this.address = address;
        this.port = port;
        this.neighbors = neighbors;
        this.values = values;
    }

    public void startPeer() throws IOException {
        final Socket socket = new Socket(address, port);
        final MessageOutbox outbox = new MessageOutbox();

        neighbors.forEach(neighbor -> {
            System.out.println("Tentando adicionar vizinho " + neighbor.getAddress() + ":" + neighbor.getPort());

            final Message helloMessage = new Message(mergeAddressAndPort(address, port), );
            outbox.sendMessage(helloMessage);
        });
    }

    private String mergeAddressAndPort(final String address, final int port) {
        return address + ":" + port;
    }
}
