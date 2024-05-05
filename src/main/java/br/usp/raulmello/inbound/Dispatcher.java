package br.usp.raulmello.inbound;

import br.usp.raulmello.Node;
import br.usp.raulmello.inbound.handlers.HelloHandler;
import br.usp.raulmello.inbound.handlers.SearchHandler;
import br.usp.raulmello.ui.Logger;
import br.usp.raulmello.utils.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Dispatcher implements Runnable {
    private final int port;
    private final int maxThreads;

    private final Node context;

    public Dispatcher(final int port, final int maxThreads, final Node context) {
        this.port = port;
        this.maxThreads = maxThreads;
        this.context = context;
    }

    @Override
    public void run() {
        Logger.debug("Dispatcher started");
        try (final ServerSocket serverSocket = new ServerSocket(port)) {
            Logger.debug("Listening for connections on port {}", port);

            final ExecutorService executor = Executors.newFixedThreadPool(maxThreads);

            while (true) {
                final Socket client = serverSocket.accept();
                Logger.debug("Accepted connection from {}", client.getRemoteSocketAddress());
                final ObjectInputStream in = new ObjectInputStream(client.getInputStream());
                final Message message = Message.fromString((String) in.readObject());

                if (message != null) {
                    switch (message.getOperation()) {
                        case HELLO -> executor.execute(new HelloHandler(client, context, message));
                        case SEARCH -> executor.execute(new SearchHandler(client, context, message));
                        default -> Logger.debug("Unkown operation {}", message.getOperation());
                    }
                }
            }
        } catch (final IOException | ClassNotFoundException e) {
            Logger.debug("Got exception {}", e.getMessage());
        }
    }
}
