package com.spaceblaster;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameOverScreen implements Screen {

    private Main jogo;
    private SpriteBatch batch;
    private BitmapFont fonteTitulo;
    private BitmapFont fonteNormal;
    private BitmapFont fonteSelecionada;

    private int scoreFinal;
    private boolean isNovoRecorde;
    private ScoreManager scoreManager;
    private Sound somGameOver; // toca uma vez quando a tela abre
    private Sound somTrocaOpcao; // toca ao navegar no menu

    // Controla qual opção do menu de Game Over está selecionada.
    // 0 = jogar novamente, 1 = voltar ao menu principal, 2 = sair do jogo.
    private int opcaoSelecionada = 0;

    // Lista de opções disponíveis na tela de Game Over.
    // Adicionamos "Main Menu" para permitir voltar ao menu e acessar os placares.
    private final String[] opcoes = {"Play Again", "Main Menu", "Exit"};

    public GameOverScreen(Main jogo, int scoreFinal, ScoreManager scoreManager) {
        this.jogo = jogo;
        this.scoreFinal = scoreFinal;
        this.scoreManager = scoreManager;
        this.isNovoRecorde = scoreManager.isHighScore(scoreFinal);

        // se for recorde, salva com nome generico por enquanto
        if (isNovoRecorde) {
            scoreManager.adicionarScore("Jogador", scoreFinal);
        }
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        fonteTitulo = new BitmapFont();
        fonteTitulo.setColor(Color.RED);
        fonteTitulo.getData().setScale(3f);

        fonteNormal = new BitmapFont();
        fonteNormal.setColor(Color.WHITE);
        fonteNormal.getData().setScale(2f);

        fonteSelecionada = new BitmapFont();
        fonteSelecionada.setColor(Color.YELLOW);
        fonteSelecionada.getData().setScale(2f);

        // carrega e toca o som assim que a tela aparece
        somGameOver = Gdx.audio.newSound(Gdx.files.internal("sounds/76376__deleted_user_877451__game_over.wav"));
        somGameOver.play(1.0f);
        somTrocaOpcao = Gdx.audio.newSound(Gdx.files.internal("sounds/menu-change.wav"));
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        batch.begin();

        // titulo GAME OVER em vermelho
        fonteTitulo.draw(batch, "GAME OVER", 200, 420);

        // score final
        fonteNormal.draw(batch, "Score: " + scoreFinal, 260, 340);

        // mensagem de novo recorde
        if (isNovoRecorde) {
            fonteSelecionada.draw(batch, "NOVO RECORDE!", 230, 300);
        }

        // opcoes
        String[] opcoes = {"Play Again", "Main Menu", "Exit"};
        for (int i = 0; i < opcoes.length; i++) {
            if (i == opcaoSelecionada) {
                fonteSelecionada.draw(batch, "> " + opcoes[i], 240, 230 - (i * 60));
            } else {
                fonteNormal.draw(batch, opcoes[i], 260, 230 - (i * 60));
            }
        }

        batch.end();

        // navegacao
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            opcaoSelecionada = (opcaoSelecionada + 1) % opcoes.length;
            somTrocaOpcao.play(0.5f);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            opcaoSelecionada = (opcaoSelecionada - 1 + opcoes.length) % opcoes.length;
            somTrocaOpcao.play(0.5f);
        }

        // Executa a ação da opção selecionada.
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (opcaoSelecionada == 0) {
                // Reinicia a partida usando o fluxo centralizado do Main.
                jogo.startGame();
            } else if (opcaoSelecionada == 1) {
                //Volta para o menu principal, onde o jogador pode acessar os placares e outras opções.
                jogo.showMenu();
            } else {
                Gdx.app.exit();
            }
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        fonteTitulo.dispose();
        fonteNormal.dispose();
        fonteSelecionada.dispose();
        if (somGameOver != null) somGameOver.dispose();
        if (somTrocaOpcao != null) somTrocaOpcao.dispose();
    }
}
