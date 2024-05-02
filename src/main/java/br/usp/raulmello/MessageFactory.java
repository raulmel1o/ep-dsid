package br.usp.raulmello;

import java.util.List;

public class MessageFactory {

    private MessageFactory() {}

    public static String createHelloMessage(final String origin, final int sequenceNumber, final int ttl, final String operation, final List<String> args) {
        final StringBuilder stringBuilder = new StringBuilder().append(origin)
                .append(" ").append(sequenceNumber).append(" ").append(ttl)
                .append(" ").append(operation);

        args.forEach(arg -> stringBuilder.append(" ").append(arg));
        stringBuilder.append("\n");

        return stringBuilder.toString();
    }
}
