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

    public static final String CONNECTION_ERROR_MSG = "\tErro ao conectar!";

    private Outbox() {}

    public static void sendMessage(final Message message, final List<Address> destinations) {
        // Question: how to handle failures on searches?
        destinations.forEach(address -> sendMessage(message, address));
    }

    public static boolean sendMessage(final Message message, final Address destination) {
        Logger.info("Encaminhando mensagem: \"{}\" para {}", message.toString().replace("\n", ""), destination);
        try (final Socket clientSocket = new Socket()) {
            Logger.debug("Connecting to {}", destination);
            clientSocket.connect(new InetSocketAddress(destination.getDomain(), destination.getPort()), 1000);
            Logger.debug("Connection established with destination: {}", destination);

            final ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
            out.writeObject(message.toString());

            final ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            final String response = (String) in.readObject();
            Logger.debug("Got response: {} from {}", response, destination);

            final boolean success = response.equalsIgnoreCase(message.getOperation() + "_OK");
            if (success) {
                Logger.info("\tEnvio feito com sucesso: \"{}\"\n", message.toString().replace("\n", ""));
            }

            return success;

        } catch (final SocketTimeoutException e) {
            Logger.debug("Connection timed out when connecting to {}", destination);
            Logger.info(CONNECTION_ERROR_MSG);
            return false;
        } catch (final ClassNotFoundException e) {
            Logger.debug("Could not parse response. Reason: {}", e.getMessage());
            Logger.info(CONNECTION_ERROR_MSG);
            return false;
        } catch (final IOException e) {
            Logger.debug("IO Exception when creating client socket: {}", e.getMessage());
            Logger.info(CONNECTION_ERROR_MSG);
            return false;
        } finally {
            Logger.info("");
        }
    }
}
