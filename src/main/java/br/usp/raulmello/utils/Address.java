package br.usp.raulmello.utils;

import lombok.Getter;

@Getter
public class Address {
    private final String domain;
    private final int port;

    public Address(final String address) {
        final String[] parts = address.split(":");
        this.domain = parts[0];
        this.port = Integer.parseInt(parts[1]);
    }

    @Override
    public String toString() {
        return domain + ":" + port;
    }
}
