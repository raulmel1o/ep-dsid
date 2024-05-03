package br.usp.raulmello.outbound;

import br.usp.raulmello.utils.Address;
import lombok.extern.log4j.Log4j;

import java.io.*;
import java.net.Socket;

@Log4j
public class Outbox {

    private Outbox() {}

    public static boolean sendMessage(final Message message, final Address destination) {
        try {
            log.debug("Creating client socket for destination: " + destination);
            final Socket clientSocket = new Socket(destination.getDomain(), destination.getPort());
            log.debug("Client socket created for destination: " + destination);

            final ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
            final ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            log.info("Encaminhando mensagem: \"" + message.toString() + "\" para " + destination);
            out.writeObject(message.toString());

            final String response = (String) in.readObject();
            log.debug("Got response: " + response + " from " + destination);

            final boolean success = response.equalsIgnoreCase(message.getOperation() + "_OK");

            if (success) {
                log.info("Envio feito com sucesso: \"" + message + "\"");
            }

            return success;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
