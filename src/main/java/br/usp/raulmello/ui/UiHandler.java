package br.usp.raulmello.ui;

public class UiHandler {
    public void showMenu() {
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
}
