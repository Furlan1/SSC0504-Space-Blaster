package com.spaceblaster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;

/*
  nave do jogador
  P2 — implementar essa classe. O stub atual já tem movimento horizontal, colisão com bordas, disparo com ESPAÇO e desenho provisório com triângulo
  P3 - substitua o triângulo por sprite PNG quando os assets estiverem prontos
*/
public class Player extends Entity {
    //constantes
    private static final float LARGURA      = 40f;
    private static final float ALTURA       = 30f;
    private static final float VELOCIDADE   = 280f;  // px/s
    private static final float VEL_BALA    = 420f;  // px/s (sobe)
    private static final float COOLDOWN_TIRO = 0.25f; // mínimo entre tiros (s)
    //estado
    //fila de balas disparadas nesse frame, esvaziada pelo GameScreen
    private final List<Bullet> balasPendentes = new ArrayList<>();
    //acumulador do cooldown entre tiros
    private float timerTiro = 0f;
    //som de tiro — carregado uma vez, liberado no dispose()
    private final Sound somTiro;
    //sprite da nave
    private final Texture textura;

    //construtor
    /*
      Cria o player na posição informada.

      x = posição X inicial (canto inferior esquerdo)
      y posição Y inicial (canto inferior esquerdo)
    */
    public Player(float x, float y) {
        super(x, y, LARGURA, ALTURA);
        somTiro = Gdx.audio.newSound(Gdx.files.internal("sounds/124906__greencouch__beeps-7.wav"));
        textura = new Texture(Gdx.files.internal("images/playerShip3_blue.png"));
    }

    //GameEntity
    @Override
    public void update(float delta) {
        //movimentação
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))  x -= VELOCIDADE * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) x += VELOCIDADE * delta;
        // limita dentro da tela
        x = Math.max(0f, Math.min(x, GameConfig.WINDOW_WIDTH - width));
        //cooldown do tiro
        if (timerTiro > 0f) timerTiro -= delta;
        // disparo
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && timerTiro <= 0f) {
            balasPendentes.add(new Bullet(getCenterX(), y + height, VEL_BALA, true));
            timerTiro = COOLDOWN_TIRO;
            somTiro.play(0.6f); // volume: 0.0 (mudo) até 1.0 (máximo)
        }
    }

    @Override
    public void renderShape(ShapeRenderer renderer) {
        // vazio — sprite substitui o triângulo provisório
    }

    @Override
    public void renderSprite(SpriteBatch batch) {
        if (!alive) return;
        batch.draw(textura, x, y, width, height);
    }

    //API para o GameScreen
    /*
      retorna e esvazia a lista de balas disparadas desde a última chamada
      o GameScreen chama isso a cada frame para adicionar as balas à sua lista

      retorna lista (talvez vazia) de novos Bullets
    */
    public List<Bullet> coletarBalasFiras() {
        List<Bullet> copia = new ArrayList<>(balasPendentes);
        balasPendentes.clear();
        return copia;
    }

    /*
      reposiciona a nave embaixo do centro da tela
      chamado pelo GameScreen quando o player perde uma vida
    */
    public void resetarPosicao() {
        x = GameConfig.WINDOW_WIDTH / 2f - width / 2f;
        y = 50f;
        timerTiro = 0f;
    }

    /*
      libera o recurso de áudio — chamar no dispose() do GameScreen
    */
    public void dispose() {
        somTiro.dispose();
        textura.dispose();
    }
}
