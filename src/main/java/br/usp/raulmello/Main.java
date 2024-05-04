package br.usp.raulmello;

import br.usp.raulmello.ui.FileReader;
import br.usp.raulmello.ui.Logger;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(final String[] args) {
        final String hostAddress = args[0];
        final int hostPort = Integer.parseInt(args[1]);
        final Map<String, String> values = FileReader.readDataFromValuesFile(args[3]);
        final List<String> neighborsList = FileReader.readNeighboursFromFile(args[2]);

        Logger.debug("Initializing node with host: {} port: {} neighbors: {} values: {}"
                ,hostAddress, hostPort, neighborsList, values);
        final Node node = Node.initNode(hostAddress, hostPort, values, neighborsList);
        Logger.debug("Starting node");
        node.startNode();
    }
}