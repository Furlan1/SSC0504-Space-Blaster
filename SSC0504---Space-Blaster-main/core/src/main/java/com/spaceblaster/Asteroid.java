package com.spaceblaster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/*
  Asteroide que cai do topo da tela.

  P2 — implementar/expandir esta classe. O stub atual já cai com a velocidade dada e se destrói ao sair pela base
  P3 - substitui o retângulo cinza por sprite PNG + animação de explosão
*/
public class Asteroid extends Entity {

    //constantes
    private static final float TAMANHO = 32f; // largura e altura (placeholder)

    //estado
    //velocidade de queda em px/s (sempre positiva, aplicada como -delta no y)
    private final float velocidade;
    //sprites
    private final Texture textura;         // meteorGrey_big1.png
    private final Texture texturaExplosao; // 240_F_1323093761_Ojf8Ux7VcglewOISLNVot78t5RWAlhOP.jpg
    //animação de explosão
    private static final float DURACAO_EXPLOSAO = 0.18f; // segundos exibindo o dano
    private boolean explodindo = false;
    private float   timerExplosao = 0f;

    //construtor

    /*
      cria um asteroide na posição informada

       x = posição X inicial
       y = posição Y inicial (deve ser ≥ WINDOW_HEIGHT para aparecer pelo topo)
       velocidade = px/s de queda (valor positivo)
    */
    public Asteroid(float x, float y, float velocidade) {
        super(x, y, TAMANHO, TAMANHO);
        this.velocidade      = velocidade;
        this.textura         = new Texture(Gdx.files.internal("images/meteorGrey_big1.png"));
        this.texturaExplosao = new Texture(Gdx.files.internal("images/240_F_1323093761_Ojf8Ux7VcglewOISLNVot78t5RWAlhOP.jpg"));
    }

    //GameEntity

    @Override
    public void update(float delta) {
        if (explodindo) {
            timerExplosao -= delta;
            if (timerExplosao <= 0f) destroy(); // some após o flash
            return; // congela posição durante explosão
        }
        y -= velocidade * delta;
    }

    // chamado pelo GameScreen quando a bala acerta o asteroide
    public void explodir() {
        explodindo    = true;
        timerExplosao = DURACAO_EXPLOSAO;
    }

    public boolean isExplodindo() { return explodindo; }

    @Override
    public void renderShape(ShapeRenderer renderer) {
        // vazio — sprite substitui o retângulo cinza
    }

    @Override
    public void renderSprite(SpriteBatch batch) {
        if (!alive && !explodindo) return;
        Texture t = explodindo ? texturaExplosao : textura;
        batch.draw(t, x, y, width, height);
    }

    //consulta
    //retorna true se o asteroide já passou completamente pela base da tela
    public boolean saioPelaBase() {
        return !explodindo && y + height < 0f;
    }

    //libera texturas — chamar quando o asteroide for removido
    public void dispose() {
        textura.dispose();
        texturaExplosao.dispose();
    }
}
