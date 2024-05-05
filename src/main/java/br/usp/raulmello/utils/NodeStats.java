package br.usp.raulmello.utils;

public class NodeStats {
    private int floodingSearchMessageAmount;
    private int randomWalkSearchMessageAmount;
    private int depthFirstSearchMessageAmount;

    @Override
    public String toString() {
        return "Estatisticas" +
                "\tTotal de mensagens de flooding vistas:" + floodingSearchMessageAmount +
                "\tTotal de mensagens de random walk vistas:" + randomWalkSearchMessageAmount +
                "\tTotal de mensagens de busca em profundidade vistas:" + depthFirstSearchMessageAmount +
                "\tMedia de saltos ate encontrar destino por flooding:" + "TODO" +
                "\tMedia de saltos ate encontrar destino por random walk:" + "TODO" +
                "\tMedia de saltos ate encontrar destino por busca em profundidade:" + "TODO";
    }

    public void incrementFloodingSearchMessageAmount() {
        floodingSearchMessageAmount++;
    }

    public void incrementRandomWalkSearchMessageAmount() {
        randomWalkSearchMessageAmount++;
    }

    public void incrementDepthFirstSearchMessageAmount() {
        depthFirstSearchMessageAmount++;
    }
}
