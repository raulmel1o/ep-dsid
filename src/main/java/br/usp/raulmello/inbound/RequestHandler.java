package br.usp.raulmello.inbound;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private final Socket clientSocket;

    public RequestHandler(final Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            final ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
            final ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
