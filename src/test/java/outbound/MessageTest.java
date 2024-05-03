package outbound;

import br.usp.raulmello.utils.Address;
import br.usp.raulmello.outbound.Message;
import org.junit.jupiter.api.Test;

import java.util.List;

import static br.usp.raulmello.utils.Operation.HELLO;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageTest {

    @Test
    void GivenHelloMessageWithNullArgs_WhenToString_ThenShouldNotPrintNull() {
        final Message message = Message.builder()
                .origin(new Address("127.0.1.1:1234")).sequenceNumber(100).ttl(1).operation(HELLO).build();

        assertEquals("127.0.1.1:1234 100 1 HELLO\n", message.toString());
    }

    @Test
    void GivenHelloMessageWithEmptyArgsList_WhenToString_ThenShouldNotPrintNull() {
        final Message message = Message.builder()
                .origin(new Address("127.0.1.1:1234")).sequenceNumber(100).ttl(1).operation(HELLO).args(emptyList()).build();

        assertEquals("127.0.1.1:1234 100 1 HELLO\n", message.toString());
    }

    @Test
    void GivenHelloMessageWithExplicitNullArgs_WhenToString_ThenShouldNotPrintNull() {
        final Message message = Message.builder()
                .origin(new Address("127.0.1.1:1234")).sequenceNumber(100).ttl(1).operation(HELLO).args(null).build();

        assertEquals("127.0.1.1:1234 100 1 HELLO\n", message.toString());
    }

    @Test
    void GivenHelloMessageWithArgs_WhenToString_ThenShouldPrintArgs() {
        final Message message = Message.builder()
                .origin(new Address("127.0.1.1:1234")).sequenceNumber(100).ttl(1).operation(HELLO)
                .args(List.of("first-arg", "second-arg")).build();

        assertEquals("127.0.1.1:1234 100 1 HELLO first-arg second-arg\n", message.toString());
    }

    @Test
    void GivenHelloString_WhenFromString_ThenShouldParseMessage() {
        final Message message = Message.fromString("localhost:8080 3 1 HELLO");

        assertEquals("localhost", message.getOrigin().getDomain());
        assertEquals(8080, message.getOrigin().getPort());
        assertEquals(3, message.getSequenceNumber());
        assertEquals(1, message.getTtl());
        assertEquals(HELLO, message.getOperation());
    }
}
