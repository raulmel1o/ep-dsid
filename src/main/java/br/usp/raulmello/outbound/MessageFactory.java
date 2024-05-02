package br.usp.raulmello.outbound;

import br.usp.raulmello.utils.Address;

import static br.usp.raulmello.utils.Operation.HELLO;

public class MessageFactory {

    private MessageFactory() {}

    public static Message createHelloMessage(final Address origin, final int sequenceNumber) {
        return Message.builder()
                .origin(origin).sequenceNumber(sequenceNumber)
                .ttl(1).operation(HELLO)
                .build();
    }
}
