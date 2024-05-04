package br.usp.raulmello.ui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ParametersFileReader {

    private ParametersFileReader() {}

    public static List<String> readNeighboursFromFile(final String filename) {
        final List<String> peers = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                peers.add(line);
            }
        } catch (final IOException e) {
            Logger.debug("Error reading parameters file: {}", e.getMessage());
        }

        return peers;
    }

    public static Map<String, String> readDataFromValuesFile(final String filename) {
        final Map<String, String> values = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                final String[] splitLine = line.split(" ");
                values.put(splitLine[0], splitLine[1]);
            }
        } catch (final IOException e) {
            Logger.debug("Error reading parameters file: {}", e.getMessage());
        }

        return values;
    }
}
