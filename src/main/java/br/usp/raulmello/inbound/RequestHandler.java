package br.usp.raulmello.inbound;

import br.usp.raulmello.Node;
import br.usp.raulmello.ui.Logger;
import br.usp.raulmello.utils.Message;
import br.usp.raulmello.utils.Operation;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private final Socket clientSocket;
    private final Node context;

    public RequestHandler(final Socket clientSocket, final Node context) {
        this.clientSocket = clientSocket;
        this.context = context;
    }

    @Override
    public void run() {
        try {
            final ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
            final ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            final String messageString = (String) in.readObject();
            final Message message = Message.fromString(messageString);


            if (message == null) {
                Logger.debug("Message is null");
                return;
            }

            if (message.getOperation().equals(Operation.HELLO)) {
                Logger.info("Mensagem recebida: \"{}\"", messageString);
                if (context.getNeighbors().contains(message.getOrigin())) {
                    Logger.info("Vizinho ja esta na tabela: {}", message.getOrigin());
                } else {
                    context.getNeighbors().add(message.getOrigin());
                    Logger.info("Adicionando vizinho na tabela: {}", message.getOrigin());
                }

                out.writeObject("HELLO_OK");
            }

            if (message.getOperation().equals(Operation.SEARCH)) {

            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
