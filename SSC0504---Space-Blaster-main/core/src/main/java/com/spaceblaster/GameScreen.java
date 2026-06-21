package com.spaceblaster;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
  tela principal da partida : gerencia entidades, colisões, vidas e progressão de nível

  coordenadas (LibGDX padrão)
  Y = 0 na base da tela; Y = GameConfig#WINDOW_HEIGHT no topo
  asteroides nascem acima do topo e caem (Y diminui)
  balas do player sobem (Y cresce), balas de inimigos descem (Y diminui)
*/
public class GameScreen implements Screen {
    //constantes
    //duração da invencibilidade após receber dano (segundos)
    private static final float DURACAO_INVENCIVEL = 2.0f;

    //dependências; contexto
    private final Main jogo;
    //score com que esta sessão começou (0 para jogo novo; >0 ao vir de LevelComplete)
    private final int scoreInicial;
    //nível com que esta sessão começou
    private final int nivelInicial;

    //gráficos
    private SpriteBatch   batch;
    private BitmapFont    fonte;        // score centralizado, destaque principal
    private BitmapFont    fonteHud;    // texto menor pros cantos (nível, vidas)
    private BitmapFont    fonteBoss;   // nome/fase do boss, bem grande pra intimidar
    private GlyphLayout   layout;      // usado pra medir texto e centralizar na tela
    private ShapeRenderer shapeRenderer;

    //gerenciadores
    private LevelManager levelManager;
    private SpawnManager spawnManager;

    //estado da partida
    private int     score;
    private int     lives;
    //timer que cresce enquanto o player está invencível (segundos)
    private float   timerInvencivel;
    private boolean invencivel;
    //impede disparar a transição de nível mais de uma vez
    private boolean nivelCompleto;
    private boolean bossDestruido;

    //conta quantos asteroides foram destruidos pelo jogador no nivel atual
    //essa contagem sera usada para impedir que a fase acabe por desviar de todos os asteroides.
    private int asteroidesDestruidos;

    //listas de entidades
    private Player         player;
    private List<Bullet>   balasPlayer;
    private List<Asteroid> asteroides;
    private List<Enemy>    inimigos;
    private List<Bullet>   balasInimigos;
    private Boss           boss;          // null nos níveis 1-3

    //construtores
    /*
      usado ao continuar do LevelCompleteScreen, mantém score e começa no próximo nível

      jogo = referência central
      scoreInicial = score acumulado até aqui
      nivelInicial = nível a iniciar
    */
    public GameScreen(Main jogo, int scoreInicial, int nivelInicial) {
        this.jogo         = jogo;
        this.scoreInicial = scoreInicial;
        this.nivelInicial = nivelInicial;
    }
    //novo jogo do zero (score = 0, nível = GameConfig#START_LEVEL)
    public GameScreen(Main jogo) {
        this(jogo, 0, GameConfig.START_LEVEL);
    }
    //screen - ciclo de vida
    @Override
    public void show() {
        //gráficos
        batch         = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        layout        = new GlyphLayout();

        // fonte principal — score fica centralizado e em destaque
        fonte = new BitmapFont();
        fonte.getData().setScale(2f);
        fonte.setColor(Color.WHITE);

        // fonte menor pra não poluir os cantos com texto gigante
        fonteHud = new BitmapFont();
        fonteHud.getData().setScale(1.4f);
        fonteHud.setColor(Color.LIGHT_GRAY);

        // fonte do boss maior que o resto pra deixar claro que é uma ameaça
        fonteBoss = new BitmapFont();
        fonteBoss.getData().setScale(1.6f);
        fonteBoss.setColor(Color.RED);

        //estado
        score = scoreInicial;
        lives = GameConfig.INITIAL_LIVES;

        //gerenciadores
        levelManager = new LevelManager(nivelInicial);
        spawnManager = new SpawnManager();
        spawnManager.resetar(nivelInicial);

        //entidades
        iniciarEntidades();
    }

    //(re)inicializa todas as listas de entidades para o nível atual
    private void iniciarEntidades() {
        float centroX = GameConfig.WINDOW_WIDTH / 2f;

        player        = new Player(centroX - 20f, 50f);
        balasPlayer   = new ArrayList<>();
        asteroides    = new ArrayList<>();
        inimigos      = new ArrayList<>();
        balasInimigos = new ArrayList<>();

        boss = levelManager.temBoss()
            ? new Boss(centroX - 36f, GameConfig.WINDOW_HEIGHT - 110f)
            : null;

        invencivel      = false;
        timerInvencivel = 0f;
        nivelCompleto   = false;
        bossDestruido   = false;

        // reinicia a contagem de asteroides destruidos ao começar um novo nível
        asteroidesDestruidos = 0;
    }

    @Override
    public void render(float delta) {
        atualizar(delta);
        //se atualizar() trocou de tela, não desenhar mais esta
        if (jogo.getScreen() != this) return;
        desenhar();
    }

    //update
    private void atualizar(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            jogo.showMenu();
            return;
        }
        //congelado após detectar fim de nível (aguarda transição de tela)
        if (nivelCompleto) return;

        atualizarInvencibilidade(delta);
        atualizarPlayer(delta);
        atualizarBalasProprias(delta);
        atualizarAsteroides(delta);
        atualizarInimigos(delta);
        atualizarBoss(delta);
        tratarColisoes();
        //atualiza o spawn considerando quantos asteroides ja foram destruidos
        spawnManager.update(delta, levelManager, asteroides, inimigos, asteroidesDestruidos);
        checarConclusaoNivel();
    }

    private void atualizarInvencibilidade(float delta) {
        if (!invencivel) return;
        timerInvencivel += delta;
        if (timerInvencivel >= DURACAO_INVENCIVEL) {
            invencivel      = false;
            timerInvencivel = 0f;
        }
    }

    private void atualizarPlayer(float delta) {
        player.update(delta);
        balasPlayer.addAll(player.coletarBalasFiras());
    }

    private void atualizarBalasProprias(float delta) {
        //balas do player : remove ao sair pelo topo
        Iterator<Bullet> it = balasPlayer.iterator();
        while (it.hasNext()) {
            Bullet b = it.next();
            b.update(delta);
            if (!b.isAlive() || b.getY() > GameConfig.WINDOW_HEIGHT) {
                b.dispose();
                it.remove();
            }
        }
        //balas de inimigos : remove ao sair pela base
        Iterator<Bullet> itI = balasInimigos.iterator();
        while (itI.hasNext()) {
            Bullet b = itI.next();
            b.update(delta);
            if (!b.isAlive() || b.getY() + b.getHeight() < 0f) {
                b.dispose();
                itI.remove();
            }
        }
    }

    private void atualizarAsteroides(float delta) {
        Iterator<Asteroid> it = asteroides.iterator();
        while (it.hasNext()) {
            Asteroid a = it.next();
            a.update(delta);
            if (!a.isAlive()) {
                a.dispose();
                it.remove();
            } else if (a.saioPelaBase()) {
                a.dispose();
                it.remove();
            }
        }
    }

    private void atualizarInimigos(float delta) {
        Iterator<Enemy> it = inimigos.iterator();
        while (it.hasNext()) {
            Enemy e = it.next();
            e.update(delta);
            if (!e.isAlive()) {
                score += LevelManager.PONTOS_INIMIGO;
                e.dispose();
                it.remove();
            } else {
                balasInimigos.addAll(e.coletarBalasFiras());
            }
        }
    }

    private void atualizarBoss(float delta) {
        if (boss == null || bossDestruido) return;
        boss.update(delta);
        if (!boss.isExplodindo()) {
            balasInimigos.addAll(boss.coletarBalasFiras());
        }
        if (!boss.isAlive()) {
            bossDestruido = true;
            score += LevelManager.PONTOS_BOSS;
            boss.dispose();
        }
    }

    //detecção de colisão
    private void tratarColisoes() {
        checarBalasPlayerVsAsteroides();
        checarBalasPlayerVsInimigos();
        checarBalasPlayerVsBoss();

        if (!invencivel) {
            checarAsteroidesVsPlayer();
            checarBalasInimigosVsPlayer();
            checarInimigosVsPlayer();
        }
    }

    private void checarBalasPlayerVsAsteroides() {
        for (Bullet b : balasPlayer) {
            if (!b.isAlive()) continue;
            for (Asteroid a : asteroides) {
                if (!a.isAlive() || a.isExplodindo()) continue;
                if (CollisionManager.check(b, a)) {
                    b.destroy();
                    a.explodir();

                    //conta apenas asteroides destruídos pelo tiro do jogador.
                    asteroidesDestruidos++;

                    score += LevelManager.PONTOS_ASTEROIDE;
                }
            }
        }
        balasPlayer.removeIf(b -> { if (!b.isAlive()) b.dispose(); return !b.isAlive(); });
        asteroides.removeIf(a -> { if (!a.isAlive()) a.dispose(); return !a.isAlive(); });
    }

    private void checarBalasPlayerVsInimigos() {
        for (Bullet b : balasPlayer) {
            if (!b.isAlive()) continue;
            for (Enemy e : inimigos) {
                if (!e.isAlive()) continue;
                if (CollisionManager.check(b, e)) {
                    b.destroy();
                    e.takeDamage(1);
                }
            }
        }
        balasPlayer.removeIf(b -> { if (!b.isAlive()) b.dispose(); return !b.isAlive(); });
        inimigos.removeIf(e -> { if (!e.isAlive()) e.dispose(); return !e.isAlive(); });
    }

    private void checarBalasPlayerVsBoss() {
        if (boss == null || bossDestruido) return;
        for (Bullet b : balasPlayer) {
            if (!b.isAlive()) continue;
            if (CollisionManager.check(b, boss)) {
                b.destroy();
                boss.takeDamage(1);
            }
        }
        balasPlayer.removeIf(b -> { if (!b.isAlive()) b.dispose(); return !b.isAlive(); });
    }

    private void checarAsteroidesVsPlayer() {
        for (Asteroid a : asteroides) {
            if (!a.isAlive()) continue;
            if (CollisionManager.check(a, player)) {
                a.destroy();
                aplicarDanoPlayer();
                break; // só um hit por frame para não tirar várias vidas de uma vez
            }
        }
        asteroides.removeIf(a -> { if (!a.isAlive()) a.dispose(); return !a.isAlive(); });
    }

    private void checarBalasInimigosVsPlayer() {
        for (Bullet b : balasInimigos) {
            if (!b.isAlive()) continue;
            if (CollisionManager.check(b, player)) {
                b.destroy();
                aplicarDanoPlayer();
                break;
            }
        }
        balasInimigos.removeIf(b -> { if (!b.isAlive()) b.dispose(); return !b.isAlive(); });
    }

    private void checarInimigosVsPlayer() {
        for (Enemy e : inimigos) {
            if (!e.isAlive()) continue;
            if (CollisionManager.check(e, player)) {
                aplicarDanoPlayer();
                break;
            }
        }
    }

    //vidas; dano
    /*
      aplica um hit ao player
      se vidas chegam a zero: game over
      caso contrário: ativa invencibilidade e reseta posição da nave
    */
    private void aplicarDanoPlayer() {
        if (invencivel) return;
        lives--;
        if (lives <= 0) {
            jogo.showGameOver(score);
        } else {
            invencivel      = true;
            timerInvencivel = 0f;
            player.resetarPosicao();
        }
    }

    //conclusão de nivel
    private void checarConclusaoNivel() {
        if (nivelCompleto) return;

        boolean concluido;
        if (levelManager.ehNivelBoss()) {
            concluido = bossDestruido;
        } else {
            //nas fases normais, a fase só termina quando o jogador destrói
            //a quantidade mínima de asteroides definida pelo LevelManager, mesmo que a wave já tenha spawnado tudo.
            //isso impede que o jogador vença apenas desviando dos asteroides sem enfrentá-los.
            boolean objetivoAsteroidesAtingido = asteroidesDestruidos >= levelManager.getObjetivoAsteroides();

            //objetivo cumprido e tela limpa de inimigos.
            concluido = objetivoAsteroidesAtingido
                && asteroides.isEmpty()
                && inimigos.isEmpty();
        }

        if (!concluido) return;

        nivelCompleto = true;
        int bonus          = levelManager.getBonusConclusao();
        int nivelQueSaiu   = levelManager.getNivelAtual();
        score += bonus;

        if (levelManager.temProximoNivel()) {
            levelManager.avancarNivel();
            jogo.mostrarNivelCompleto(nivelQueSaiu, score, bonus);
        } else {
            //todos os 4 níveis concluídos : mostra a tela de vitória
            // salvamento de recorde centralizado na WinScreen.
            jogo.showWinScreen(score);
        }
    }

    //render
    private void desenhar() {
        ScreenUtils.clear(0f, 0f, 0f, 1f);

        // === fase 1: ShapeRenderer — barras da HUD + formas das entidades ===
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // faixa escura semitransparente no topo pra o texto da HUD não se perder no fundo do espaço
        shapeRenderer.setColor(0f, 0f, 0f, 0.55f);
        shapeRenderer.rect(0, GameConfig.WINDOW_HEIGHT - 50, GameConfig.WINDOW_WIDTH, 50);

        // barra de HP do boss — só aparece no nível 4, centralizada logo abaixo da faixa
        if (levelManager.ehNivelBoss() && boss != null && !bossDestruido) {
            float barW = GameConfig.WINDOW_WIDTH * 0.6f;
            float barX = (GameConfig.WINDOW_WIDTH - barW) / 2f;
            float barY = GameConfig.WINDOW_HEIGHT - 80;
            float pct  = boss.getHp() / 20f;

            // fundo cinza escuro pra mostrar o quanto de HP falta
            shapeRenderer.setColor(0.25f, 0.25f, 0.25f, 1f);
            shapeRenderer.rect(barX, barY, barW, 10);

            // cor muda conforme o HP cai: verde → amarelo → vermelho
            Color corHp = pct > 0.5f ? Color.GREEN : pct > 0.25f ? Color.YELLOW : Color.RED;
            shapeRenderer.setColor(corHp);
            shapeRenderer.rect(barX, barY, barW * pct, 10);
        }

        // entidades — shapes provisórios (ficam vazios quando sprite está ativo)
        for (Asteroid a : asteroides)    a.renderShape(shapeRenderer);
        for (Enemy    e : inimigos)      e.renderShape(shapeRenderer);
        if (boss != null && (!bossDestruido || boss.isExplodindo())) boss.renderShape(shapeRenderer);
        for (Bullet   b : balasPlayer)   b.renderShape(shapeRenderer);
        for (Bullet   b : balasInimigos) b.renderShape(shapeRenderer);
        //player pisca durante invencibilidade (8 Hz)
        if (!invencivel || ((int)(timerInvencivel * 8)) % 2 == 0) {
            player.renderShape(shapeRenderer);
        }

        shapeRenderer.end();

        // === fase 2: SpriteBatch — sprites das entidades + texto da HUD ===
        batch.begin();

        //sprites das entidades, P3 preenche os renderSprite() de cada classe
        for (Asteroid a : asteroides)    a.renderSprite(batch);
        for (Enemy    e : inimigos)      e.renderSprite(batch);
        if (boss != null && (!bossDestruido || boss.isExplodindo())) boss.renderSprite(batch);
        for (Bullet   b : balasPlayer)   b.renderSprite(batch);
        for (Bullet   b : balasInimigos) b.renderSprite(batch);
        if (!invencivel || ((int)(timerInvencivel * 8)) % 2 == 0) {
            player.renderSprite(batch);
        }

        // === HUD ===
        float topoHud = GameConfig.WINDOW_HEIGHT - 10; // linha base do texto, dentro da faixa escura

        // nível no canto esquerdo — discreto, não precisa chamar atenção
        fonteHud.setColor(Color.LIGHT_GRAY);
        fonteHud.draw(batch, "NIVEL  " + levelManager.getNivelAtual(), 14, topoHud);

        // score centralizado em destaque — é o que o jogador mais quer ver
        String textoScore = "SCORE  " + score;
        layout.setText(fonte, textoScore);
        fonte.setColor(Color.WHITE);
        fonte.draw(batch, textoScore, (GameConfig.WINDOW_WIDTH - layout.width) / 2f, topoHud);

        // corações de vida no canto direito
        // ♥ = vida restante, · = vida perdida
        StringBuilder vidas = new StringBuilder();
        for (int i = 0; i < GameConfig.INITIAL_LIVES; i++) {
            vidas.append(i < lives ? "♥ " : "· ");
        }
        layout.setText(fonteHud, vidas.toString());
        fonteHud.setColor(Color.RED);
        fonteHud.draw(batch, vidas.toString(), GameConfig.WINDOW_WIDTH - layout.width - 14, topoHud);

        // nome e fase do boss centralizado abaixo da barra de HP
        // fica laranja e com estrela na fase 2 pra deixar claro que ficou mais perigoso
        if (levelManager.ehNivelBoss() && boss != null && !bossDestruido) {
            String textoFase = boss.estaFase2() ? "★ BOSS — FASE 2: RAGE ★" : "BOSS — FASE 1";
            layout.setText(fonteBoss, textoFase);
            fonteBoss.setColor(boss.estaFase2() ? Color.ORANGE : Color.RED);
            fonteBoss.draw(batch, textoFase,
                (GameConfig.WINDOW_WIDTH - layout.width) / 2f,
                GameConfig.WINDOW_HEIGHT - 92);
        }

        batch.end();
    }

    //lifecycle
    @Override public void resize(int width, int height) {}
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}

    @Override
    public void dispose() {
        if (batch         != null) batch.dispose();
        if (fonte         != null) fonte.dispose();
        if (fonteHud      != null) fonteHud.dispose();
        if (fonteBoss     != null) fonteBoss.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
        if (player        != null) player.dispose(); // libera som de tiro e sprite
    }
}
