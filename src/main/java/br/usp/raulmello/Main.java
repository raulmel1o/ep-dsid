package br.usp.raulmello;

import br.usp.raulmello.ui.FileReader;
import lombok.extern.log4j.Log4j;

import java.util.List;
import java.util.Map;

@Log4j
public class Main {
    public static void main(final String[] args) {
        final String hostAddress = args[0];
        final int hostPort = Integer.parseInt(args[1]);
        final Map<String, String> values = FileReader.readDataFromValuesFile(args[3]);
        final List<String> neighborsList = FileReader.readNeighboursFromFile(args[2]);

        log.debug("Initializing node with host: " + hostAddress + ", port: " + hostPort + ", neighbors: " + neighborsList + ", values: " + values);
        final Node node = Node.initNode(hostAddress, hostPort, values, neighborsList);
        log.debug("Starting node");
        node.startNode();
    }
}