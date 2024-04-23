package br.usp.raulmello;

import java.util.*;

public class FileReader {
    public static List<Peer> readNeighbourPeersFromFile(final String filename) {
        final List<Peer> peers = new ArrayList<>();

        final Scanner scanner = new Scanner(Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream(filename)));
        while (scanner.hasNextLine()) {
            peers.add(new Peer(scanner.nextLine()));
        }
        scanner.close();

        return peers;
    }

    public static Map<String, String> readDataFromValuesFile(final String filename) {
        final Map<String, String> values = new HashMap<>();

        final Scanner scanner = new Scanner(Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream(filename)));
        while (scanner.hasNextLine()) {
            final String[] line = scanner.nextLine().split(" ");
            values.put(line[0], line[1]);
        }

        scanner.close();
        return values;
    }
}
