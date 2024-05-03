package br.usp.raulmello.ui;

import br.usp.raulmello.utils.Address;

import java.util.List;

public class MenuWriter {

    private MenuWriter() {}

    public static void showInitialMenu() {
        final String menuString = """
                Escolha o comando
                \t[0] Listar vizinhos
                \t[1] HELLO
                \t[2] SEARCH (flooding)
                \t[3] SEARCH (random walk)
                \t[4] SEARCH (busca em profundidade)
                \t[5] Estatisticas
                \t[6] Alterar valor padrao de TTL
                \t[9] Sair
                """;

        System.out.println(menuString);
    }

    public static void showNeighbors(final List<Address> neighbors) {
        final StringBuilder sb = new StringBuilder().append("Ha ").append(neighbors.size())
                .append(" vizinhos na tabela: \n");

        for (int i = 0; i < neighbors.size(); i++) {
            sb.append("\t[").append(i).append("] ").append(neighbors.get(i)).append('\n');
        }

        System.out.println(sb);
    }
}
