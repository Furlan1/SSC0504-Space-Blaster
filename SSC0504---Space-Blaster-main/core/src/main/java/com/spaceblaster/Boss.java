package com.spaceblaster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    //sprites e som
    private final Texture textura;          // enemyRed1.png
    private final Texture texturaExplosao;  // imagem de dano (reusa a do asteroide)
    private final Sound   somExplosao;      // 151022__bubaproducer__laser-shot-silenced
    //animação de explosão
    private static final float DURACAO_EXPLOSAO = 0.4f; // segundos (mais longa que a do asteroide)
    private boolean explodindo   = false;
    private float   timerExplosao = 0f;

    //construtor
    /*
      cria o Boss em cima do centro da tela.

      x = posição X inicial
      y = posição Y inicial
    */
    public Boss(float x, float y) {
        super(x, y, VEL_FASE1, false); // false = não carrega ufoRed.png
        this.width        = LARGURA_BOSS;
        this.height       = ALTURA_BOSS;
        this.hp           = HP_TOTAL;
        this.timerBoss    = INTERVALO_FASE1;
        this.velocidadeX  = VEL_FASE1;
        this.textura         = new Texture(Gdx.files.internal("images/enemyRed1.png"));
        this.texturaExplosao = new Texture(Gdx.files.internal("images/240_F_1323093761_Ojf8Ux7VcglewOISLNVot78t5RWAlhOP.jpg"));
        this.somExplosao     = Gdx.audio.newSound(Gdx.files.internal("sounds/151022__bubaproducer__laser-shot-silenced.wav"));
    }

    //GameEntity
    @Override
    public void update(float delta) {
        if (!alive) return;

        // congela movimento durante animação de explosão final
        if (explodindo) {
            timerExplosao -= delta;
            if (timerExplosao <= 0f) destroy();
            return;
        }

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
        // vazio — sprite substitui o retângulo vermelho
        // mantém só a barra de HP visual enquanto não explode
        if (!explodindo && alive) {
            float porcentagemHp = hp / (float) HP_TOTAL;
            renderer.setColor(Color.GREEN);
            renderer.rect(x, y + height + 4, width * porcentagemHp, 6);
            renderer.setColor(Color.DARK_GRAY);
            renderer.rect(x + width * porcentagemHp, y + height + 4, width * (1 - porcentagemHp), 6);
        }
    }

    @Override
    public void renderSprite(SpriteBatch batch) {
        if (!alive && !explodindo) return;
        Texture t = explodindo ? texturaExplosao : textura;
        batch.draw(t, x, y, width, height);
    }

    //libera recursos — chamar no dispose() do GameScreen
    public void dispose() {
        textura.dispose();
        texturaExplosao.dispose();
        somExplosao.dispose();
    }

    //Dano ; HP
    @Override
    public void takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            hp = 0;
            explodindo    = true;
            timerExplosao = DURACAO_EXPLOSAO;
            somExplosao.play(1.0f);
            destroy(); // marca alive=false para o GameScreen detectar bossDestruido
        }
    }

    public boolean isExplodindo() { return explodindo; }

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
