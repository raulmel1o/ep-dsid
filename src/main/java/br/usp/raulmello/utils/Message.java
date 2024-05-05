package br.usp.raulmello.utils;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@Builder
public class Message {
    private Address origin;
    private int sequenceNumber;
    private int ttl;
    private Operation operation;
    private List<String> args;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder()
                .append(origin).append(" ")
                .append(sequenceNumber).append(" ")
                .append(ttl).append(" ")
                .append(operation);

        if (args != null) {
            args.forEach(arg -> sb.append(" ").append(arg));
        }

        return sb.append("\n").toString();
    }

    public static Message fromString(final String message) {
        final String[] split = message.split(" ");

        return Message.builder()
                .origin(new Address(split[0]))
                .sequenceNumber(Integer.parseInt(split[1]))
                .ttl(Integer.parseInt(split[2]))
                .operation(Operation.valueOf(split[3]))
                .args(copyArgsToList(split))
                .build();
    }

    private static List<String> copyArgsToList(final String[] args) {
        if (args.length <= 4) {
            return Collections.emptyList();
        }

        return new ArrayList<>(Arrays.asList(args).subList(4, args.length));
    }
}
