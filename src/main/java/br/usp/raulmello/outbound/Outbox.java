package br.usp.raulmello.outbound;

import br.usp.raulmello.utils.Address;
import br.usp.raulmello.utils.Message;

import java.io.*;
import java.net.Socket;

public class Outbox {

    private Outbox() {}

    public static boolean sendMessage(final Message message, final Address destination) {
        try {
            System.out.println("Creating client socket for destination: " + destination);
            final Socket clientSocket = new Socket(destination.getDomain(), destination.getPort());
            System.out.println("Client socket created for destination: " + destination);

            final ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
            final ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            System.out.println("Encaminhando mensagem: \"" + message.toString() + "\" para " + destination);
            out.writeObject(message.toString());

            final String response = (String) in.readObject();
            System.out.println("Got response: " + response + " from " + destination);

            final boolean success = response.equalsIgnoreCase(message.getOperation() + "_OK");

            if (success) {
                System.out.println("Envio feito com sucesso: \"" + message + "\"");
            }

            return success;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
