package com.spaceblaster;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;

/*
  Boss do nível 4 — herda de Enemy

  Fase 1 (HP 20 até 11): se move horizontalmente e atira um único projétil para baixo a cada 1,5 segundos   
  Fase 2 (HP ≤ 10): velocidade aumenta e atira em leque de 3 projéteis (centro + diagonais) a cada 1,2 segundos
        
  P2 - implementar e expandir essa classe. O stub atual já controla o HP, detecta as fases e destrói o Boss quando HP chega a zero
  P3 - adiciona sprite maior e animação de explosão
*/
public class Boss extends Enemy {
    //constantes
    private static final int   HP_TOTAL        = 20;
    private static final int   HP_FASE2        = 10;        // HP em que entra fase 2
    private static final float LARGURA_BOSS    = 72f;
    private static final float ALTURA_BOSS     = 48f;
    private static final float VEL_FASE1       = 60f;       // px/s
    private static final float VEL_FASE2       = 110f;      // px/s
    private static final float INTERVALO_FASE1 = 1.5f;      // segundos entre tiros
    private static final float INTERVALO_FASE2 = 1.2f;
    private static final float VEL_BALA_BOSS   = -240f;     // desce
    private static final float ANGULO_LEQUE    = 0.35f;     // radianos (20°)

    //estado
    private int hp;
    private float timerBoss;
    private final List<Bullet> balasBoss = new ArrayList<>();

    //construtor
    /*
      cria o Boss em cima do centro da tela.
     
      x = posição X inicial
      y = posição Y inicial
    */
    public Boss(float x, float y) {
        super(x, y, VEL_FASE1);
        this.width  = LARGURA_BOSS;
        this.height = ALTURA_BOSS;
        this.hp     = HP_TOTAL;
        this.timerBoss = INTERVALO_FASE1;
        this.velocidadeX = VEL_FASE1;
    }

    //GameEntity 
    @Override
    public void update(float delta) {
        if (!alive) return;
        boolean fase2 = hp <= HP_FASE2;
        //atualiza velocidade ao entrar na fase 2
        if (fase2 && Math.abs(velocidadeX) < VEL_FASE2) {
            velocidadeX = Math.signum(velocidadeX) * VEL_FASE2;
        }

        //movimento horizontal (com "rebote" nas bordas) — herdado via campo velocidadeX
        x += velocidadeX * delta;
        if (x < 0f) {
            x = 0f;
            velocidadeX = Math.abs(velocidadeX);
        } else if (x + width > GameConfig.WINDOW_WIDTH) {
            x = GameConfig.WINDOW_WIDTH - width;
            velocidadeX = -Math.abs(velocidadeX);
        }
        //timer de tiro
        timerBoss -= delta;
        float intervalo = fase2 ? INTERVALO_FASE2 : INTERVALO_FASE1;
        if (timerBoss <= 0f) {
            timerBoss = intervalo;
            atirar(fase2);
        }
    }

    @Override
    public void renderShape(ShapeRenderer renderer) {
        //retângulo vermelho maior — P3 substitui por sprite
        Color cor = hp <= HP_FASE2 ? Color.ORANGE : Color.RED;
        renderer.setColor(cor);
        renderer.rect(x, y, width, height);
        //barra de HP visual
        float porcentagemHp = hp / (float) HP_TOTAL;
        renderer.setColor(Color.GREEN);
        renderer.rect(x, y + height + 4, width * porcentagemHp, 6);
        renderer.setColor(Color.DARK_GRAY);
        renderer.rect(x + width * porcentagemHp, y + height + 4, width * (1 - porcentagemHp), 6);             
    }

    //Dano ; HP 
    @Override
    public void takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            hp = 0;
            destroy();
            //P3 - animação de explosão do boss
        }
    }

    //retorna HP atual do Boss 
    public int getHp() { return hp; }

    //retorna true se o Boss está na fase 2 
    public boolean estaFase2() { return hp <= HP_FASE2; }

    //API para o GameScreen
    @Override
    public List<Bullet> coletarBalasFiras() {
        List<Bullet> copia = new ArrayList<>(balasBoss);
        balasBoss.clear();
        return copia;
    }

    //privado
    private void atirar(boolean fase2) {
        if (fase2) {
            //leque de 3 balas
            balasBoss.add(new Bullet(getCenterX(), y,  VEL_BALA_BOSS, false));
            //diagonais, P2 - ajustar com velocidade X real para leque
            balasBoss.add(new Bullet(getCenterX() - 20, y, VEL_BALA_BOSS, false));
            balasBoss.add(new Bullet(getCenterX() + 20, y, VEL_BALA_BOSS, false));
        } else {
            //tiro simples
            balasBoss.add(new Bullet(getCenterX(), y, VEL_BALA_BOSS, false));
        }
    }
}
