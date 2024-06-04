package br.usp.raulmello.utils;

import lombok.Getter;

import java.util.Objects;

@Getter
public class Address {
    private final String domain;
    private final int port;

    public Address(final String address) {
        final String[] parts = address.split(":");
        this.domain = parts[0];
        this.port = Integer.parseInt(parts[1]);
    }

    public Address(final String domain, final int port) {
        this.domain = domain;
        this.port = port;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o instanceof Address address) {
            return port == address.port && domain.equals(address.domain);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain, port);
    }

    @Override
    public String toString() {
        return domain + ":" + port;
    }
}
