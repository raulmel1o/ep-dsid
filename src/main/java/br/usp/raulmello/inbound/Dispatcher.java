package br.usp.raulmello.inbound;

import br.usp.raulmello.Node;
import br.usp.raulmello.ui.Logger;

import java.io.IOException;
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
        try {
            final ServerSocket serverSocket = new ServerSocket(port);
            Logger.debug("Listening for connections on port {}", port);

            final ExecutorService executor = Executors.newFixedThreadPool(maxThreads);

            while (true) {
                final Socket client = serverSocket.accept();
                Logger.debug("Accepted connection from {}", client.getRemoteSocketAddress());

                final Runnable handler = new RequestHandler(client, this.context);
                executor.execute(handler);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
