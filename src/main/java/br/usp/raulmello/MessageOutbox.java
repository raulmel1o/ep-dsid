package br.usp.raulmello;

public class MessageOutbox {
    public void sendMessage(final Message message) {
        switch (message.getOperation()) {
            case "HELLO":
                sayHello();
        }
    }

    private void sayHello() {
        System.out.println("Hello World!");

    }
}
