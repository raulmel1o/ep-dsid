package br.usp.raulmello.ui;

import br.usp.raulmello.Main;

import java.util.*;

import static java.util.Objects.requireNonNull;

public class FileReader {

    private FileReader() {}

    public static List<String> readNeighboursFromFile(final String filename) {
        final List<String> peers = new ArrayList<>();

        try (final Scanner scanner = new Scanner(requireNonNull(Main.class.getClassLoader().getResourceAsStream(filename)))) {
            while (scanner.hasNextLine()) {
                peers.add(scanner.nextLine());
            }
        }

        return peers;
    }

    public static Map<String, String> readDataFromValuesFile(final String filename) {
        final Map<String, String> values = new HashMap<>();

        final Scanner scanner = new Scanner(requireNonNull(Main.class.getClassLoader().getResourceAsStream(filename)));
        while (scanner.hasNextLine()) {
            final String[] line = scanner.nextLine().split(" ");
            values.put(line[0], line[1]);
        }

        scanner.close();
        return values;
    }
}
