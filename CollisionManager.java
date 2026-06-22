package com.spaceblaster;

import com.badlogic.gdx.math.Rectangle;

/*
  detecta colisões entre entidades usando AABB (Axis-Aligned Bounding Box).
 
  de acordo com a internet, AABB é o método padrão para jogos 2D: dois retângulos colidem se eles se sobrepõem simultaneamente nos eixos X e Y
  o LibGDX já fornece o "Rectangle#overlaps(Rectangle)" que faz exatamente isso, então essa classe serve de ponto central de chamada, garantindo que
  entidades mortas nunca sejam testadas
  
  uso no GameScreen:
  
  for (Bullet b : playerBullets) {
      for (Asteroid a : asteroids) {
          if (CollisionManager.check(b, a)) {
              b.destroy();
              a.destroy();
              score += LevelManager.PONTOS_ASTEROIDE;
          }
      }
  }

*/
public final class CollisionManager {

    //classe utilitária, não instanciar
    private CollisionManager() {}

    //API principal 
    /*
      verifica colisão AABB entre duas entidades do jogo
      retorna false imediatamente se qualquer uma das entidades já estiver destruída, evitando checagens desnecessárias
     
      a = primeira entidade
      b = segunda entidade
      retorna true se as bounding boxes se sobrepõem
    */
    public static boolean check(GameEntity a, GameEntity b) {
        if (!a.isAlive() || !b.isAlive()) return false;
        return a.getBounds().overlaps(b.getBounds());
    }

    /*
      versão direta que compara dois retângulos sem checar estado de vida, util quando já tem as bounding boxes calculadas
      
      r1 = primeiro retângulo
      r2 = segundo retângulo
      retorna true se os retângulos se sobrepõem
    */
    public static boolean check(Rectangle r1, Rectangle r2) {
        return r1.overlaps(r2);
    }
}
