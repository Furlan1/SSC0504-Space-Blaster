package com.spaceblaster;

// Centraliza configuracoes globais do jogo.
// Assim, largura da janela, vidas iniciais e FPS nao ficam espalhados pelo codigo.
public final class GameConfig {
    public static final String TITLE = "SpaceBlaster";

    public static final int WINDOW_WIDTH = 640;
    public static final int WINDOW_HEIGHT = 480;

    public static final int INITIAL_LIVES = 3;
    public static final int START_LEVEL = 1;
    public static final int TARGET_FPS = 60;

    // Impede criar objetos dessa classe, pois ela serve apenas para constantes.
    private GameConfig() {
    }
}