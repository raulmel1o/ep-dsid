package br.usp.raulmello;

import java.io.IOException;
import java.util.*;

import static br.usp.raulmello.FileReader.readDataFromValuesFile;
import static br.usp.raulmello.FileReader.readNeighbourPeersFromFile;

public class Main {
    public static void main(final String[] args) throws IOException {
        final String address = args[0];
        final String port = args[1];

        List<Peer> peers = List.of();
        Map<String, String> values = Map.of();

        if (args.length > 2) {
            peers = readNeighbourPeersFromFile(args[2]);
        }

        if (args.length > 3) {
            values = readDataFromValuesFile(args[3]);
        }

        final Peer peer = new Peer(address, Integer.parseInt(port), peers, values);
        peer.startPeer();
    }
}