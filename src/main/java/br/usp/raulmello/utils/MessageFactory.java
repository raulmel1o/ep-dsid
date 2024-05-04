package br.usp.raulmello.utils;

import static br.usp.raulmello.utils.Operation.HELLO;
import static br.usp.raulmello.utils.Operation.SEARCH;

public class MessageFactory {

    private MessageFactory() {}

    public static Message createHelloMessage(final Address origin, final int sequenceNumber) {
        return Message.builder()
                .origin(origin).sequenceNumber(sequenceNumber)
                .ttl(1).operation(HELLO)
                .build();
    }

    public static Message createSearchFloodingMessage(final Address origin, final int sequenceNumber, final int ttl) {
        return Message.builder()
                .origin(origin).sequenceNumber(sequenceNumber)
                .ttl(ttl).operation(SEARCH)
                .build();
    }
}
