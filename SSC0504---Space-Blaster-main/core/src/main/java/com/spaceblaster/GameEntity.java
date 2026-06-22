package com.spaceblaster;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

/*
  toda entidade do Space Blaster deve seguir esse contrato
 
  garante que qualquer objeto no jogo possa ser atualizado, desenhado e ter sua hitbox consultada de forma uniforme, sem que o chamador precise conhecer o tipo concreto da entidade
  
  há dois métodos de desenho separados por fase:
  
  #renderShape — chamado com o ShapeRenderer ativo. Usado agora, com formas geométricas simples como placeholder
  #renderSprite} — chamado com o SpriteBatch ativo. Inicialmente vazio; P3 sobrescreve quando adicionar sprites PNG
*/
public interface GameEntity {
    /*
      atualiza a lógica da entidade para o frame atual
    
      delta = tempo em segundos desde o último frame (LibGDX padrão)
    */
    void update(float delta);

    /*
      desenha a entidade usando formas geometricas do ShapeRenderer
      chamar somente enquanto o ShapeRenderer estiver ativo (entre begin e end)
     
      renderer = ShapeRenderer ativo do GameScreen
    */
    void renderShape(ShapeRenderer renderer);

    /*
      desenha a entidade usando sprites e texturas do SpriteBatch
      implementação padrão vazia — P3 sobrescreve com code batch.draw(texture…)
      chamar so enquanto o SpriteBatch estiver ativo (entre begin e end)
     
      batch = SpriteBatch ativo do GameScreen
    */
    default void renderSprite(SpriteBatch batch) {
        // sem sprite por enquanto — P3 substitui quando tiver os assets PNG
    }

    /*
      retorna a bounding box (hitbox) atual dessa entidade
      usado pelo CollisionManager para detecção AABB
     
      retorna retângulo que envolve a entidade (posição + tamanho)
    */
    Rectangle getBounds();

    /*
      indica se a entidade ainda está ativa no jogo
      o GameScreen remove entidades com code isAlive() == false
     
      retorna true enquanto a entidade estiver viva/ativa
    */
    boolean isAlive();
}
