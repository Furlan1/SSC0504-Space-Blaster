package com.spaceblaster;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class InstructionsScreen implements Screen {

    private Main jogo;
    private SpriteBatch batch;
    private BitmapFont fonteTitulo;
    private BitmapFont fonteNormal;
    private BitmapFont fonteDestaque;

    public InstructionsScreen(Main jogo) {
        this.jogo = jogo;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        fonteTitulo = new BitmapFont();
        fonteTitulo.setColor(Color.YELLOW);
        fonteTitulo.getData().setScale(2.5f);

        fonteNormal = new BitmapFont();
        fonteNormal.setColor(Color.WHITE);
        fonteNormal.getData().setScale(1.8f);

        fonteDestaque = new BitmapFont();
        fonteDestaque.setColor(Color.GREEN);
        fonteDestaque.getData().setScale(1.8f);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        batch.begin();

        // titulo
        fonteTitulo.draw(batch, "COMO JOGAR", 230, 430);

        // controles
        fonteNormal.draw(batch, "CONTROLES:", 80, 360);
        fonteDestaque.draw(batch, "Seta Esquerda / Direita", 80, 320);
        fonteNormal.draw(batch, "Mover a nave", 460, 320);

        fonteDestaque.draw(batch, "Barra de Espaco", 80, 275);
        fonteNormal.draw(batch, "Atirar", 460, 275);

        // linha divisoria
        fonteNormal.draw(batch, "--------------------------------", 80, 245);

        // regras
        fonteNormal.draw(batch, "REGRAS:", 80, 210);
        fonteNormal.draw(batch, "- Destrua os asteroides para ganhar pontos", 80, 175);
        fonteNormal.draw(batch, "- Evite colisoes com asteroides e inimigos", 80, 145);
        fonteNormal.draw(batch, "- Voce tem 3 vidas", 80, 115);
        fonteNormal.draw(batch, "- O jogo fica mais dificil a cada nivel", 80, 85);

        // instrucao para comecar
        fonteDestaque.draw(batch, "Pressione ENTER para comecar!", 130, 45);

        batch.end();

        // ENTER inicia a partida.
        // ESC volta para o menu principal.
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            jogo.startGame();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            jogo.showMenu();
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
        fonteDestaque.dispose();
    }
}
