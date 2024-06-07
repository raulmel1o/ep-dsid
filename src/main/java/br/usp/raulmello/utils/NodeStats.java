package br.usp.raulmello.utils;

public class NodeStats {
    private int receivedFloodingSearchMessageAmount;
    private int receivedRandomWalkSearchMessageAmount;
    private int receivedDepthFirstSearchMessageAmount;

    private int sentFloodingSearchMessageAmount;
    private int sentRandomWalkSearchMessageAmount;
    private int sentDepthFirstSearchMessageAmount;

    private int floodingSearchHopAmount;
    private int randomWalkSearchHopAmount;
    private int depthFirstSearchHopAmount;

    // TODO: Include std deviation
    @Override
    public String toString() {
        return "Estatisticas" +
                "\tTotal de mensagens de flooding vistas:" + receivedFloodingSearchMessageAmount + "\n" +
                "\tTotal de mensagens de random walk vistas:" + receivedRandomWalkSearchMessageAmount +  "\n" +
                "\tTotal de mensagens de busca em profundidade vistas:" + receivedDepthFirstSearchMessageAmount +  "\n" +
                "\tMedia de saltos ate encontrar destino por flooding:" + getAverageAmountOfHopsFlooding() +  "\n" +
                "\tMedia de saltos ate encontrar destino por random walk:" + getAverageAmountOfHopsRandomWalk() +  "\n" +
                "\tMedia de saltos ate encontrar destino por busca em profundidade:" + getAverageAmountOfHopsDepthFirstSearch();
    }

    public void incrementReceivedFloodingSearchMessageAmount() {
        receivedFloodingSearchMessageAmount++;
    }

    public void incrementReceivedRandomWalkSearchMessageAmount() {
        receivedRandomWalkSearchMessageAmount++;
    }

    public void incrementReceivedDepthFirstSearchMessageAmount() {
        receivedDepthFirstSearchMessageAmount++;
    }

    public void incrementSentFloodingSearchMessageAmount() {
        sentFloodingSearchMessageAmount++;
    }

    public void incrementSentRandomWalkSearchMessageAmount() {
        sentRandomWalkSearchMessageAmount++;
    }

    public void incrementSentDepthFirstSearchMessageAmount() {
        sentDepthFirstSearchMessageAmount++;
    }

    public void incrementSearchHopAmount(final String mode, final int amount) {
        switch (mode) {
            case "FL": floodingSearchHopAmount = floodingSearchHopAmount + amount;
            break;
            case "RW": randomWalkSearchHopAmount = randomWalkSearchHopAmount + amount;
            break;
            case "BP": depthFirstSearchHopAmount = depthFirstSearchHopAmount + amount;
            break;
            default:
                throw new IllegalStateException("Unexpected value: " + mode);
        }
    }

    private double getAverageAmountOfHopsFlooding() {
        return (double) floodingSearchHopAmount / sentFloodingSearchMessageAmount;
    }

    private double getAverageAmountOfHopsRandomWalk() {
        return (double) randomWalkSearchHopAmount / sentRandomWalkSearchMessageAmount;
    }

    private double getAverageAmountOfHopsDepthFirstSearch() {
        return (double) depthFirstSearchHopAmount / sentDepthFirstSearchMessageAmount;
    }
}
