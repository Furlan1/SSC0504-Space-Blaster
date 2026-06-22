package com.spaceblaster;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreManager {

    // caminho do arquivo onde os scores são salvos
    private static final String ARQUIVO = "scores.txt";

    // lista que guarda os scores na memória
    private List<int[]> scores;
    // cada int[] tem 2 posições: [0] = pontos, [1] não usado
    // vamos usar String[] na verdade:
    private List<String[]> listaScores;

    public ScoreManager() {
        listaScores = new ArrayList<>();
        carregarScores();
    }

    // lê o arquivo e coloca os scores na lista
    public void carregarScores() {
        listaScores.clear();
        try {
            BufferedReader leitor = new BufferedReader(new FileReader(ARQUIVO));
            String linha;
            while ((linha = leitor.readLine()) != null) {
                String[] partes = linha.split(",");
                if (partes.length == 2) {
                    listaScores.add(partes);
                }
            }
            leitor.close();
        } catch (IOException e) {
            // arquivo ainda não existe, tudo bem
            System.out.println("Nenhum score salvo ainda.");
        }
    }

    // salva a lista no arquivo
    public void salvarScores() {
        try {
            FileWriter escritor = new FileWriter(ARQUIVO);
            for (String[] score : listaScores) {
                escritor.write(score[0] + "," + score[1] + "\n");
            }
            escritor.close();
        } catch (IOException e) {
            System.out.println("Erro ao salvar scores: " + e.getMessage());
        }
    }

    // adiciona um novo score e mantém só os top-5
    public void adicionarScore(String nome, int pontos) {
        listaScores.add(new String[]{nome, String.valueOf(pontos)});

        // ordena do maior para o menor
        listaScores.sort((a, b) ->
            Integer.parseInt(b[1]) - Integer.parseInt(a[1])
        );

        // mantém só os 5 primeiros
        if (listaScores.size() > 5) {
            listaScores = listaScores.subList(0, 5);
        }

        salvarScores();
    }

    // verifica se um score entra no top-5
    public boolean isHighScore(int pontos) {
        if (listaScores.size() < 5) return true;
        int menorScore = Integer.parseInt(listaScores.get(listaScores.size() - 1)[1]);
        return pontos > menorScore;
    }

    // retorna a lista de scores para exibir na tela
    public List<String[]> getScores() {
        return listaScores;
    }
}
