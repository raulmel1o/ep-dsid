package br.usp.raulmello;

import lombok.Getter;

@Getter
public class Message {
    private String origin;
    private String sequenceNumber;
    private int ttl;
    // TODO: Convert to ENUM
    private String operation;
    private String[] args;

    public Message(final String origin, final String sequenceNumber, final int ttl, final String operation, final String[] args) {
        this.origin = origin;
        this.sequenceNumber = sequenceNumber;
        this.ttl = ttl;
        this.operation = operation;
        this.args = args;
    }
}


