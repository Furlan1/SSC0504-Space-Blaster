package com.spaceblaster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;

/*
  inimigo normal que aparece a partir do nível 2

  P2 — implementar e expandir esta classe. O stub atual move o inimigo horizontalmente (ricocheteando nas bordas) e atira para baixo a cada
  intervalo
  P3 - adiciona o sprite quando tiver os assets
*/
public class Enemy extends Entity {
    //constantes
    protected static final float LARGURA         = 36f;
    protected static final float ALTURA          = 24f;
    private   static final float VELOCIDADE_PADRAO = 90f;     // px/s horizontal
    private   static final float VEL_BALA        = -220f;     // px/s (negativo = desce)
    private   static final float INTERVALO_TIRO  = 2.5f;      // segundos entre tiros
    //estado
    protected float velocidadeX;
    private float timerTiro;
    private final List<Bullet> balasPendentes = new ArrayList<>();
    //sprite do inimigo
    private final Texture textura;

    //construtor
    /*
       x = posição X inicial
       y = posição Y inicial
       velocidadeX = velocidade horizontal (pode ser negativa para ir pra esquerda)
    */
    public Enemy(float x, float y, float velocidadeX) {
        super(x, y, LARGURA, ALTURA);
        this.velocidadeX = velocidadeX;
        this.timerTiro = (float)(Math.random() * INTERVALO_TIRO);
        this.textura = new Texture(Gdx.files.internal("images/ufoRed.png"));
    }
    //construtor interno para subclasses (Boss) que não usam o sprite do Enemy
    protected Enemy(float x, float y, float velocidadeX, boolean carregarSprite) {
        super(x, y, LARGURA, ALTURA);
        this.velocidadeX = velocidadeX;
        this.timerTiro   = (float)(Math.random() * INTERVALO_TIRO);
        this.textura     = carregarSprite
            ? new Texture(Gdx.files.internal("images/ufoRed.png"))
            : null;
    }
    //construtor com velocidade padrão
    public Enemy(float x, float y) {
        this(x, y, VELOCIDADE_PADRAO);
    }

    //GameEntity
    @Override
    public void update(float delta) {
        //movimentação horizontal com rebote nas bordas
        x += velocidadeX * delta;
        if (x < 0f) {
            x = 0f;
            velocidadeX = Math.abs(velocidadeX);
        } else if (x + width > GameConfig.WINDOW_WIDTH) {
            x = GameConfig.WINDOW_WIDTH - width;
            velocidadeX = -Math.abs(velocidadeX);
        }
        //tiro para baixo
        timerTiro -= delta;
        if (timerTiro <= 0f) {
            timerTiro = INTERVALO_TIRO;
            balasPendentes.add(new Bullet(getCenterX(), y, VEL_BALA, false));
            // P3 - som de tiro inimigo
        }
        //P2 - padrões de movimento mais complexos (diagonal, etc.)
    }
    @Override
    public void renderShape(ShapeRenderer renderer) {
        // vazio — sprite substitui o losango provisório
    }

    @Override
    public void renderSprite(SpriteBatch batch) {
        if (!alive || textura == null) return;
        batch.draw(textura, x, y, width, height);
    }

    //libera textura — chamar quando o inimigo for removido
    public void dispose() {
        if (textura != null) textura.dispose();
    }

    //API para o GameScreen
    /*
      retorna e esvazia a lista de balas disparadas pelo inimigo nesse frame

      retorna lista (talvez vazia) de novos Bullets
    */
    public List<Bullet> coletarBalasFiras() {
        List<Bullet> copia = new ArrayList<>(balasPendentes);
        balasPendentes.clear();
        return copia;
    }
}
