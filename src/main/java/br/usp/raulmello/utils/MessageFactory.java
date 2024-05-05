package br.usp.raulmello.utils;

import java.util.List;

import static br.usp.raulmello.utils.Operation.*;

public class MessageFactory {

    private MessageFactory() {}

    public static Message createHelloMessage(final Address origin, final int sequenceNumber) {
        return Message.builder()
                .origin(origin).sequenceNumber(sequenceNumber)
                .ttl(1).operation(HELLO)
                .build();
    }

    public static Message createSearchFloodingMessage(final String key, final Address origin, final int sequenceNumber, final int ttl) {
        return createSearchMessage(origin, sequenceNumber, ttl)
                .args(List.of("FL", Integer.toString(origin.getPort()), key, Integer.toString(1)))
                .build();
    }

    public static Message createSearchRandomWalkMessage(final String key, final Address origin, final int sequenceNumber, final int ttl) {
        return createSearchMessage(origin, sequenceNumber, ttl)
                .args(List.of("RW", Integer.toString(origin.getPort()), key, Integer.toString(1)))
                .build();
    }

    public static Message createValMessage(final Address origin, final int sequenceNumber, final int ttl, final String searchMode, final String key, final String value, final int hopCount) {
        return Message.builder()
                .origin(origin).sequenceNumber(sequenceNumber)
                .ttl(ttl).operation(VAL)
                .args(List.of(searchMode, key, value, Integer.toString(hopCount)))
                .build();
    }

    private static Message.MessageBuilder createSearchMessage(final Address origin, final int sequenceNumber, final int ttl) {
        return Message.builder()
                .origin(origin).sequenceNumber(sequenceNumber)
                .ttl(ttl).operation(SEARCH);
    }
}
