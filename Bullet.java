package com.spaceblaster;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/*
  projétil disparado pelo player ou por inimigos.
 
  P2 — implementar esta classe. O stub atual já move a bala e desenha um retângulo simples pra teste
    
  Bala do player: velocidadeY positiva → sobe até sair por cima
  Bala inimiga: velocidadeY negativa → desce até sair por baixo
*/
public class Bullet extends Entity {
    //constantes
    private static final float LARGURA  = 5f;
    private static final float ALTURA   = 14f;

    //estado
    //velocidade vertical em px/s (positivo sobe, negativo desce)
    private final float velocidadeY;
    //true se disparada pelo player, false se por inimigo/boss
    private final boolean doPlayer;

    //construtor
    /*
      x = posição X inicial (centro horizontal da bala)
      y = posição Y inicial (base da bala)
      velocidadeY = px/s; positivo sobe (player), negativo desce (inimigo)
      doPlayer  = true se a bala é do player
    */
    public Bullet(float x, float y, float velocidadeY, boolean doPlayer) {
        super(x - LARGURA / 2f, y, LARGURA, ALTURA);
        this.velocidadeY = velocidadeY;
        this.doPlayer    = doPlayer;
    }

    //GameEntity 
    @Override
    public void update(float delta) {
        y += velocidadeY * delta;
        //P2 - adicionar efeito visual, som, etc.
    }
    @Override
    public void renderShape(ShapeRenderer renderer) {
        renderer.setColor(doPlayer ? Color.YELLOW : Color.RED);
        renderer.rect(x, y, width, height);
    }

    //consultas
    //retorna true se a bala é do player 
    public boolean isDoPlayer() { return doPlayer; }
}
