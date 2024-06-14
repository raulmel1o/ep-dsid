package br.usp.raulmello.utils;

import java.util.ArrayList;
import java.util.List;

public class NodeStats {
    private int receivedFloodingSearchMessageAmount;
    private int receivedRandomWalkSearchMessageAmount;
    private int receivedDepthFirstSearchMessageAmount;

    private int sentFloodingSearchMessageAmount;
    private int sentRandomWalkSearchMessageAmount;
    private int sentDepthFirstSearchMessageAmount;

    private final List<Integer> floodingSearchHopAmountList = new ArrayList<>();
    private final List<Integer> randomWalkSearchHopAmountList = new ArrayList<>();
    private final List<Integer> depthFirstSearchHopAmountList = new ArrayList<>();

    // TODO: Include std deviation
    @Override
    public String toString() {
        return "Estatisticas\n" +
                "\tTotal de mensagens de flooding vistas: " + receivedFloodingSearchMessageAmount + "\n" +
                "\tTotal de mensagens de random walk vistas: " + receivedRandomWalkSearchMessageAmount +  "\n" +
                "\tTotal de mensagens de busca em profundidade vistas: " + receivedDepthFirstSearchMessageAmount +  "\n" +
                "\tMedia de saltos ate encontrar destino por flooding: " + getAverageAmountOfHopsFlooding() + " (dp " + getStdDeviation(floodingSearchHopAmountList) + ")\n" +
                "\tMedia de saltos ate encontrar destino por random walk: " + getAverageAmountOfHopsRandomWalk() + " (dp " + getStdDeviation(randomWalkSearchHopAmountList) +  ")\n" +
                "\tMedia de saltos ate encontrar destino por busca em profundidade: " + getAverageAmountOfHopsDepthFirstSearch() + " (dp " + getStdDeviation(floodingSearchHopAmountList) + ")";
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
            case "FL": floodingSearchHopAmountList.add(amount);
            break;
            case "RW": randomWalkSearchHopAmountList.add(amount);
            break;
            case "BP": depthFirstSearchHopAmountList.add(amount);
            break;
            default:
                throw new IllegalStateException("Unexpected value: " + mode);
        }
    }

    private double getAverageAmountOfHopsFlooding() {
        if (sentFloodingSearchMessageAmount == 0) {
            return 0;
        }

        return (double) floodingSearchHopAmountList.stream().mapToInt(Integer::intValue).sum() / sentFloodingSearchMessageAmount;
    }

    private double getAverageAmountOfHopsRandomWalk() {
        if (sentFloodingSearchMessageAmount == 0) {
            return 0;
        }

        return (double) randomWalkSearchHopAmountList.stream().mapToInt(Integer::intValue).sum() / sentRandomWalkSearchMessageAmount;
    }

    private double getAverageAmountOfHopsDepthFirstSearch() {
        if (sentFloodingSearchMessageAmount == 0) {
            return 0;
        }

        return (double) depthFirstSearchHopAmountList.stream().mapToInt(Integer::intValue).sum() / sentDepthFirstSearchMessageAmount;
    }

    private double getStdDeviation(final List<Integer> values) {
        final int n = values.size();
        if (n == 0) {
            return 0;
        }
        final double sum = values.stream().mapToDouble(i -> i).sum();
        final double avg = sum / n;

        double avgSquaredDiffSum = 0.0;
        for (double num : values) {
            avgSquaredDiffSum += Math.pow(num - avg, 2);
        }

        return Math.sqrt( avgSquaredDiffSum / n);
    }
}
