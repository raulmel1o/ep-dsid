package br.usp.raulmello.outbound;

import br.usp.raulmello.ui.Logger;
import br.usp.raulmello.utils.Address;
import br.usp.raulmello.utils.Message;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;

public class Outbox {

    private Outbox() {}

    public static void sendMessage(final Message message, final List<Address> destinations) {
        // Question: how to handle failures on searches?
        destinations.forEach(address -> sendMessage(message, address));
    }

    public static boolean sendMessage(final Message message, final Address destination) {
        try (final Socket clientSocket = new Socket()) {
            Logger.debug("Connecting to {}", destination);
            clientSocket.connect(new InetSocketAddress(destination.getDomain(), destination.getPort()), 1000);
            Logger.debug("Connection established with destination: {}", destination);

            final ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
            final ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            Logger.info("Encaminhando mensagem: \"{}\" para {}", message, destination);
            out.writeObject(message.toString());

            final String response = (String) in.readObject();
            Logger.debug("Got response: {} from {}", response, destination);

            final boolean success = response.equalsIgnoreCase(message.getOperation() + "_OK");
            if (success) {
                Logger.info("Envio feito com sucesso: \"{}\"", message);
            }

            return success;

        } catch (final SocketTimeoutException e) {
            Logger.debug("Connection timed out when connecting to {}", destination);
            return false;
        } catch (final ClassNotFoundException e) {
            Logger.debug("Could not parse response. Reason: {}", e.getMessage());
            return false;
        } catch (final IOException e) {
            Logger.debug("IO Exception when creating client socket: {}", e.getMessage());
            return false;
        }
    }
}
