package com.spaceblaster;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MenuScreen implements Screen {
    private Main jogo;
    private SpriteBatch batch;
    private BitmapFont font;
    private BitmapFont selectedFont;

    // opcao selecionada: 0 = Start, 1 = High Scores, 2 = Exit
    private int opcaoSelecionada = 0;

    private String[] opcoes = {"Start Game", "High Scores", "Exit"};

    public MenuScreen(Main jogo) {
        this.jogo = jogo;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        // fonte normal (branca)
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2f);

        // fonte selecionada (amarela e maior)
        selectedFont = new BitmapFont();
        selectedFont.setColor(Color.YELLOW);
        selectedFont.getData().setScale(2.5f);
    }

    @Override
    public void render(float delta) {
        // limpa a tela com cor preta
        ScreenUtils.clear(0, 0, 0, 1);

        batch.begin();

        // titulo do jogo
        selectedFont.draw(batch, "SPACE BLASTER", 220, 400);

        // desenha as opcoes do menu
        for (int i = 0; i < opcoes.length; i++) {
            if (i == opcaoSelecionada) {
                // opcao selecionada aparece em amarelo com seta
                selectedFont.draw(batch, "> " + opcoes[i], 240, 280 - (i * 60));
            } else {
                font.draw(batch, opcoes[i], 260, 280 - (i * 60));
            }
        }

        batch.end();

        // controles do teclado
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            opcaoSelecionada = (opcaoSelecionada + 1) % opcoes.length;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            opcaoSelecionada = (opcaoSelecionada - 1 + opcoes.length) % opcoes.length;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (opcaoSelecionada == 0) {
                jogo.setScreen(new InstructionsScreen(jogo));
            } else if (opcaoSelecionada == 1) {
                jogo.setScreen(new HighScoresScreen(jogo));
            } else if (opcaoSelecionada == 2) {
                Gdx.app.exit();
            }
        }
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        selectedFont.dispose();
    }
}
