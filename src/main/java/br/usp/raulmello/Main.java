package br.usp.raulmello;

import br.usp.raulmello.ui.ParametersFileReader;
import br.usp.raulmello.ui.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(final String[] args) {
        final String hostAddress = args[0];
        final int hostPort = Integer.parseInt(args[1]);
        final Map<String, String> values = getValuesFromArgs(args);
        final List<String> neighborsList = ParametersFileReader.readNeighboursFromFile(args[2]);

        Logger.debug("Initializing node with host: {} port: {} neighbors: {} values: {}"
                ,hostAddress, hostPort, neighborsList, values);
        final Node node = Node.initNode(hostAddress, hostPort, values, neighborsList);
        Logger.debug("Starting node");
        node.startNode();
    }

    private static Map<String, String> getValuesFromArgs(final String[] args) {
        if (args.length > 3) {
            return ParametersFileReader.readDataFromValuesFile(args[3]);
        }

        return Collections.emptyMap();
    }
}