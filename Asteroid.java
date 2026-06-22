package com.spaceblaster;

import com.badlogic.gdx.graphics.Color;
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

    //construtor

    /*
      cria um asteroide na posição informada
     
       x = posição X inicial
       y = posição Y inicial (deve ser ≥ WINDOW_HEIGHT para aparecer pelo topo)
       velocidade = px/s de queda (valor positivo)
    */
    public Asteroid(float x, float y, float velocidade) {
        super(x, y, TAMANHO, TAMANHO);
        this.velocidade = velocidade;
    }

    //GameEntity

    @Override
    public void update(float delta) {
        y -= velocidade * delta; // cai (Y decresce no LibGDX, pois y=0 é a base)

        //saiu pela base da tela → o GameScreen vai detectar isso e tirar uma vida
        //(não chamamos destroy() aqui, o GameScreen é quem decide)
        //P2 - rotação do asteroide, variações de tamanho, etc.
    }

    @Override
    public void renderShape(ShapeRenderer renderer) {
        //retângulo cinza provisório — para o P3 substituir por sprite com animação
        renderer.setColor(Color.GRAY);
        renderer.rect(x, y, width, height);
        //borda mais clara para dar profundidade
        renderer.setColor(Color.LIGHT_GRAY);
        renderer.rect(x + 4, y + 4, width - 8, height - 8);
    }
    //consulta
    
    //retorna true se o asteroide já passou completamente pela base da tela e deve ser removido (com penalidade de vida para o player)
    public boolean saioPelaBase() {
        return y + height < 0f;
    }
}
