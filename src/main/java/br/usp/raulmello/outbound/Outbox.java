package br.usp.raulmello.outbound;

import br.usp.raulmello.utils.Address;
import br.usp.raulmello.utils.Message;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Outbox {

    private Outbox() {}

    public static boolean sendMessage(final Message message, final Address destination) {
        try (final Socket clientSocket = new Socket()) {
            System.out.println("Connecting to " + destination);
            clientSocket.connect(new InetSocketAddress(destination.getDomain(), destination.getPort()), 1000);
            System.out.println("Connection established with destination: " + destination);

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

        } catch (final SocketTimeoutException e) {
            System.err.println("Connection timed out when connecting to " + destination);
            return false;
        } catch (final ClassNotFoundException e) {
            System.err.println("Could not parse response. Reason: " + e.getMessage());
            return false;
        } catch (final IOException e) {
            System.err.println("IO Exception when creating client socket: " + e.getMessage());
            return false;
        }
    }
}
