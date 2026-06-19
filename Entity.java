package com.spaceblaster;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

/*
  classe base abstrata de todas as entidades do Space Blaster
 
  implementa GameEntity e fornece os campos de posição, tamanho e estado de vida compartilhados por todas as entidades
  subclasses só precisam implementar #update e #renderShape
 
  hierarquia de herança:
 
   Entity  (P1)
     Player    (P2)
     Bullet    (P2)
     Asteroid  (P2)
     Enemy     (P2)
         Boss  (P2)
  
  sistema de coordenadas LibGDX:Y=0 ta na base da tela, Y=GameConfig#WINDOW_HEIGHT está no topo
  asteroides nascem no topo e caem (Y diminui), balas do player sobem (Y cresce)  
*/
public abstract class Entity implements GameEntity {
    //posição e tamanho
    //coordenada X em pixels do canto inferior esquerdo da entidade 
    protected float x;
    //coordenada Y em pixels do canto inferior-esquerdo da entidade 
    protected float y;
    //largura em pixels
    protected float width;
    //altura em pixels
    protected float height;

    //estado
    //true enquanto a entidade estiver ativa no jogo
    protected boolean alive;

    //construtor
    /*
      cria uma entidade na posição e tamanho indicados
     
      x = posição X inicial (canto inferior esquerdo, pixels)
      y = posição Y inicial (canto inferior esquerdo, pixels)
      width = largura em pixels
      height = altura em pixels
    */
    protected Entity(float x, float y, float width, float height) {
        this.x      = x;
        this.y      = y;
        this.width  = width;
        this.height = height;
        this.alive  = true;
    }

    //contrato abstrato
    @Override
    public abstract void update(float delta);

    @Override
    public abstract void renderShape(ShapeRenderer renderer);

    //GameEntity padrão
    /*
      retorna um retângulo AABB com a posição e tamanho atual
      subclasses podem sobrescrever para "hitboxes" mais justas
    */
    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    //retorna true se a entidade estiver viva, false caso contrário
    @Override
    public boolean isAlive() {
        return alive;
    }

    //metodos utilitários
    /*
      marca a entidade como destruída
      O GameScreen vai remover ela das listas no próximo frame
    */
    public void destroy() {
        alive = false;
    }
    /*
      aplica dano a entidade
      implementação padrão: qualquer dano destrói a entidade
      subclasses com HP (tipo Boss) devem sobrescrever
     
      amount = quantidade de dano recebido
    */
    public void takeDamage(int amount) {
        destroy();
    }
    //Getters ; Setters

    // retornacoordenada X atual (canto inferior esquerdo) 
    public float getX() { return x; }

    //retorna coordenada Y atual (canto inferior esquerdo) 
    public float getY() { return y; }

    //retorna largura em pixels 
    public float getWidth()  { return width; }

    //retorna altura em pixels 
    public float getHeight() { return height; }

    //reposiciona a entidade para (x,y)
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    //retorna centro X da entidade 
    public float getCenterX() { return x + width  / 2f; }

    //retorna centro Y da entidade 
    public float getCenterY() { return y + height / 2f; }

    //object 
    @Override
    public String toString() {
        return String.format("%s[x=%.0f y=%.0f w=%.0f h=%.0f alive=%b]",
                getClass().getSimpleName(), x, y, width, height, alive);
    }
}
