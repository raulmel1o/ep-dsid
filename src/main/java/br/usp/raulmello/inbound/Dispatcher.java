package br.usp.raulmello.inbound;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Dispatcher implements Runnable {
    private final int port;
    private final int maxThreads;

    public Dispatcher(final int port, final int maxThreads) {
        this.port = port;
        this.maxThreads = maxThreads;
    }

    @Override
    public void run() {
        System.out.println("Dispatcher started");
        try {
            final ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Listening for connections on port " + port);

            final ExecutorService executor = Executors.newFixedThreadPool(maxThreads);

            while (true) {
                final Socket client = serverSocket.accept();
                System.out.println("Accepted connection from " + client.getRemoteSocketAddress());

                final Runnable handler = new RequestHandler(client);
                executor.execute(handler);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
