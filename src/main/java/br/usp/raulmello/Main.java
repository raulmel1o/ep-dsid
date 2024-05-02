package br.usp.raulmello;

import java.util.*;

import static br.usp.raulmello.FileReader.readDataFromValuesFile;
import static br.usp.raulmello.FileReader.readNeighboursFromFile;

public class Main {
    public static void main(final String[] args) {
        final String address = args[0];
        final String port = args[1];

        List<String> peers = List.of();
        Map<String, String> values = Map.of();

        if (args.length > 2) {
            peers = readNeighboursFromFile(args[2]);
        }

        if (args.length > 3) {
            values = readDataFromValuesFile(args[3]);
        }
    }


}