package br.usp.raulmello.inbound;

import br.usp.raulmello.utils.Message;
import br.usp.raulmello.utils.Operation;
import lombok.extern.log4j.Log4j;

import java.io.*;
import java.net.Socket;

@Log4j
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

            final Message message = Message.fromString((String) in.readObject());

            if (message == null) {
                log.error("Message is null");
                return;
            }

            if (message.getOperation().equals(Operation.HELLO)) {
                out.writeObject("HELLO_OK");
            }

            if (message.getOperation().equals(Operation.SEARCH)) {

            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
