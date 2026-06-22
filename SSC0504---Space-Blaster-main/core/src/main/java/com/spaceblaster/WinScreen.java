package com.spaceblaster;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.audio.Sound;

public class WinScreen implements Screen {

    private Main jogo;
    private SpriteBatch batch;

    private BitmapFont fonteTitulo;
    private BitmapFont fonteNormal;
    private BitmapFont fonteSelecionada;

    private int scoreFinal;
    private boolean isNovoRecorde;
    private ScoreManager scoreManager;
    // Som tocado uma vez quando a tela de vitória é exibida.
    private Sound somVitoria;
    private Sound somTrocaOpcao; // toca ao navegar no menu

    // Controla qual opção da tela de vitória está selecionada.
    // 0 = jogar novamente, 1 = voltar ao menu principal, 2 = sair do jogo.
    private int opcaoSelecionada = 0;

    // Opções disponíveis após o jogador vencer o jogo.
    // O menu principal permite acessar os placares.
    private final String[] opcoes = {"Play Again", "Main Menu", "Exit"};

    public WinScreen(Main jogo, int scoreFinal, ScoreManager scoreManager) {
        this.jogo = jogo;
        this.scoreFinal = scoreFinal;
        this.scoreManager = scoreManager;

        // Verifica se o score final entra na lista de melhores pontuações.
        this.isNovoRecorde = scoreManager.isHighScore(scoreFinal);

        // Salva o score apenas se for novo recorde.
        // Por enquanto o nome é fixo porque o jogo ainda não possui entrada de nome do jogador.
        if (isNovoRecorde) {
            scoreManager.adicionarScore("Jogador", scoreFinal);
        }
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        fonteTitulo = new BitmapFont();
        fonteTitulo.setColor(Color.GREEN);
        fonteTitulo.getData().setScale(3f);

        fonteNormal = new BitmapFont();
        fonteNormal.setColor(Color.WHITE);
        fonteNormal.getData().setScale(2f);

        fonteSelecionada = new BitmapFont();
        fonteSelecionada.setColor(Color.YELLOW);
        fonteSelecionada.getData().setScale(2f);

        somVitoria = Gdx.audio.newSound(Gdx.files.internal("sounds/mixkit-video-game-win-2016.wav"));
        somVitoria.play(0.7f);
        somTrocaOpcao = Gdx.audio.newSound(Gdx.files.internal("sounds/menu-change.wav"));
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0.05f, 0.1f, 1);

        batch.begin();

        fonteTitulo.draw(batch, "VITORIA!", 230, 420);
        fonteNormal.draw(batch, "Score final: " + scoreFinal, 210, 340);

        if (isNovoRecorde) {
            fonteSelecionada.draw(batch, "NOVO RECORDE!", 220, 300);
        }

        // Desenha as opções da tela e destaca a opção selecionada.
        for (int i = 0; i < opcoes.length; i++) {
            if (i == opcaoSelecionada) {
                fonteSelecionada.draw(batch, "> " + opcoes[i], 240, 230 - (i * 60));
            } else {
                fonteNormal.draw(batch, opcoes[i], 260, 230 - (i * 60));
            }
        }

        batch.end();

        // Navega pelas opções usando o tamanho do vetor.
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            opcaoSelecionada = (opcaoSelecionada + 1) % opcoes.length;
            somTrocaOpcao.play(0.5f);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            opcaoSelecionada = (opcaoSelecionada - 1 + opcoes.length) % opcoes.length;
            somTrocaOpcao.play(0.5f);
        }

        // Executa a ação escolhida pelo jogador.
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (opcaoSelecionada == 0) {
                jogo.startGame();
            } else if (opcaoSelecionada == 1) {
                jogo.showMenu();
            } else {
                Gdx.app.exit();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        batch.dispose();
        fonteTitulo.dispose();
        fonteNormal.dispose();
        fonteSelecionada.dispose();

        if(somVitoria != null) {
            somVitoria.dispose();
        }
        if(somTrocaOpcao != null) {
            somTrocaOpcao.dispose();
        }
    }
}