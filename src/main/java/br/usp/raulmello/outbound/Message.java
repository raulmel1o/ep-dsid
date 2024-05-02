package br.usp.raulmello.outbound;

import br.usp.raulmello.utils.Address;
import br.usp.raulmello.utils.Operation;
import lombok.Builder;

import java.util.List;

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
}
