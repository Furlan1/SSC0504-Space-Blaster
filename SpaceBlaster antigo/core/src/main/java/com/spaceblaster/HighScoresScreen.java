package com.spaceblaster;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.List;

public class HighScoresScreen implements Screen {

    private Main jogo;
    private SpriteBatch batch;
    private BitmapFont fonteTitulo;
    private BitmapFont fonteNormal;
    private BitmapFont fonteVoltar;
    private ScoreManager scoreManager;

    public HighScoresScreen(Main jogo) {
        this.jogo = jogo;
        this.scoreManager = new ScoreManager();
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        fonteTitulo = new BitmapFont();
        fonteTitulo.setColor(Color.YELLOW);
        fonteTitulo.getData().setScale(2.5f);

        fonteNormal = new BitmapFont();
        fonteNormal.setColor(Color.WHITE);
        fonteNormal.getData().setScale(2f);

        fonteVoltar = new BitmapFont();
        fonteVoltar.setColor(Color.GRAY);
        fonteVoltar.getData().setScale(1.8f);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        batch.begin();

        // titulo
        fonteTitulo.draw(batch, "HIGH SCORES", 210, 420);

        // lista os scores
        List<String[]> scores = scoreManager.getScores();

        if (scores.isEmpty()) {
            fonteNormal.draw(batch, "Nenhum score ainda!", 200, 330);
        } else {
            for (int i = 0; i < scores.size(); i++) {
                String nome = scores.get(i)[0];
                String pontos = scores.get(i)[1];
                String linha = (i + 1) + ".  " + nome + "  -  " + pontos;
                fonteNormal.draw(batch, linha, 180, 340 - (i * 55));
            }
        }

        // instrucao para voltar
        fonteVoltar.draw(batch, "Pressione ENTER para voltar", 160, 80);

        batch.end();

        // voltar ao menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) ||
            Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            jogo.setScreen(new MenuScreen(jogo));
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
        fonteVoltar.dispose();
    }
}
