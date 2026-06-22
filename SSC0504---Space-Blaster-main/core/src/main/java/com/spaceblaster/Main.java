package com.spaceblaster;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

// Classe principal do jogo.
// Ela controla o ciclo de vida do LibGDX e centraliza a troca entre telas.
public class Main extends Game {

    // Mantido aqui para que as telas usem o mesmo gerenciador de pontuacao.
    private ScoreManager scoreManager;

    public void create() {
        scoreManager = new ScoreManager();
        showMenu();
    }

    // Os metodos abaixo centralizam a navegacao.
    // Assim, as telas nao precisam criar outras telas diretamente com setScreen().
    public void showMenu() {
        changeScreen(new MenuScreen(this));
    }

    public void showInstructions() {
        changeScreen(new InstructionsScreen(this));
    }

    public void startGame() {
        changeScreen(new GameScreen(this));
    }

    public void showHighScores() {
        changeScreen(new HighScoresScreen(this));
    }

    public void showGameOver(int scoreFinal) {
        changeScreen(new GameOverScreen(this, scoreFinal, scoreManager));
    }

    public ScoreManager getScoreManager() {
        return scoreManager;
    }

    // Troca a tela atual pela nova e libera os recursos da tela anterior.
    // Isso evita deixar fontes, batches e outros objetos graficos abertos na memoria.
    private void changeScreen(Screen novaTela) {
        Screen telaAtual = getScreen();

        setScreen(novaTela);

        if (telaAtual != null) {
            telaAtual.dispose();
        }
    }

    // Libera os recursos da tela atual antes de encerrar o jogo.
    @Override
    public void dispose() {
        if (getScreen() != null) {
            getScreen().dispose();
        }

        super.dispose();
    }
}