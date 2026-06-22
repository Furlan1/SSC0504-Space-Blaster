package com.spaceblaster;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

// Tela principal da partida.
// Por enquanto contem uma nave temporaria e atalhos de teste.
// Depois, esta tela deve receber as entidades reais: Player, Asteroid, Bullet, Enemy etc.
public class GameScreen implements Screen {

    private Main jogo;

    private SpriteBatch batch;
    private BitmapFont fonte;
    private ShapeRenderer shapeRenderer;

    // Estado basico da partida.
    // Esses valores serao usados pela logica do jogo e pela UI/HUD.
    private int level;
    private int score;
    private int lives;

    // Nave temporária apenas para validar o loop principal do jogo.
    // Futuramente deve ser substituida/integrada com uma classe Player.
    private float naveX;
    private float naveY;
    private float velocidadeNave = 300f;

    public GameScreen(Main jogo) {
        this.jogo = jogo;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        fonte = new BitmapFont();
        fonte.setColor(Color.WHITE);
        fonte.getData().setScale(1.5f);

        shapeRenderer = new ShapeRenderer();

        level = GameConfig.START_LEVEL;
        score = 0;
        lives = GameConfig.INITIAL_LIVES;

        naveX = GameConfig.WINDOW_WIDTH / 2f;
        naveY = 60f;
    }

    @Override
    public void render(float delta) {
        atualizar(delta);

        // Se o update trocou para outra tela, esta tela ja pode ter sido descartada.
        // Nesse caso, nao devemos desenhar mais nada aqui.
        if (jogo.getScreen() != this) {
            return;
        }

        desenhar();
    }

    // Atualiza o estado da partida.    
    // Nesta primeira versao, os comandos simulam eventos do jogo para testar o nucleo.
    private void atualizar(float delta) {
        // Controles temporários para validar o núcleo.
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            naveX -= velocidadeNave * delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            naveX += velocidadeNave * delta;
        }

        // Limita a nave dentro da janela.
        if (naveX < 25f) {
            naveX = 25f;
        }

        if (naveX > GameConfig.WINDOW_WIDTH - 25f) {
            naveX = GameConfig.WINDOW_WIDTH - 25f;
        }

        // Teste temporário de pontuação.
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            score += 10;
        }

        // Teste temporário de avanço de nível.
        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            level++;
            score += 100;
        }

        // Teste temporário de dano.
        if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
            lives--;

            if (lives <= 0) {
                jogo.showGameOver(score);
                return;
            }
        }

        // Voltar ao menu.
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            jogo.showMenu();
        }
    }

    // Desenha a versao provisoria da tela de jogo.
    // A UI final, sprites e HUD podem substituir esses desenhos depois.
    private void desenhar() {
        ScreenUtils.clear(0, 0, 0, 1);

        // Desenha uma nave temporária.
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.CYAN);
        shapeRenderer.triangle(
            naveX, naveY + 25f,
            naveX - 20f, naveY - 20f,
            naveX + 20f, naveY - 20f
        );
        shapeRenderer.end();

        batch.begin();

        fonte.draw(batch, "Level: " + level, 20, 455);
        fonte.draw(batch, "Score: " + score, 20, 425);
        fonte.draw(batch, "Lives: " + lives, 20, 395);

        fonte.draw(batch, "Setas: mover nave", 20, 120);
        fonte.draw(batch, "Espaco: simular tiro/ponto", 20, 95);
        fonte.draw(batch, "L: simular proximo nivel", 20, 70);
        fonte.draw(batch, "H: simular dano", 20, 45);
        fonte.draw(batch, "ESC: voltar ao menu", 20, 20);

        batch.end();
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
        fonte.dispose();
        shapeRenderer.dispose();
    }
}