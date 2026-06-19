package com.spaceblaster;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/*
  tela exibida ao concluir um nível (exceto o último)

  mostra o número do nível concluído, o bônus ganho, o score acumulado e qual nível vem a seguir. O jogador pressiona ENTER para continuar

  O GameScreen passa os dados ao chamar Main#mostrarNivelCompleto(int, int, int)  
*/
public class LevelCompleteScreen implements Screen {
    //dependencias
    private final Main jogo;

    //gráficos
    private SpriteBatch batch;
    private BitmapFont  fonteTitulo;
    private BitmapFont  fonteNormal;
    private BitmapFont  fonteDestaque;

    //dados do nível recém concluído
    private final int nivelConcluido;
    private final int scoreAtual;
    private final int bonusGanho;

    //estado interno
    //impede que ENTER seja aceito imediatamente ao entrar na tela
    private float timerEntrada;
    private static final float BLOQUEIO_INICIAL = 0.5f; // segundos

    //construtor
    /*
      jogo = referência central do jogo (navegação)
       nivelConcluido = número do nível que acabou de ser concluído
       scoreAtual = score total já incluindo o bônus deste nível
       bonusGanho = bônus de conclusão (mostrado separadamente)
    */
    public LevelCompleteScreen(Main jogo, int nivelConcluido,
                               int scoreAtual, int bonusGanho) {
        this.jogo           = jogo;
        this.nivelConcluido = nivelConcluido;
        this.scoreAtual     = scoreAtual;
        this.bonusGanho     = bonusGanho;
    }

    //screen
    @Override
    public void show() {
        batch = new SpriteBatch();
        timerEntrada = BLOQUEIO_INICIAL;

        fonteTitulo = new BitmapFont();
        fonteTitulo.setColor(Color.GREEN);
        fonteTitulo.getData().setScale(2.8f);

        fonteNormal = new BitmapFont();
        fonteNormal.setColor(Color.WHITE);
        fonteNormal.getData().setScale(1.9f);

        fonteDestaque = new BitmapFont();
        fonteDestaque.setColor(Color.YELLOW);
        fonteDestaque.getData().setScale(1.9f);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0.05f, 0f, 1f); // fundo verde-escuro
        timerEntrada -= delta;

        batch.begin();

        //título
        fonteTitulo.draw(batch, "NIVEL " + nivelConcluido + " COMPLETO!", 120, 415);

        //bonus de conclusão
        fonteNormal.draw(batch,   "Bonus de conclusao:", 130, 330);
        fonteDestaque.draw(batch, "+ " + bonusGanho + " pts", 390, 330);

        //score total
        fonteNormal.draw(batch,   "Score total:", 130, 278);
        fonteDestaque.draw(batch, String.valueOf(scoreAtual), 390, 278);
        //próximo nível
        int prox = nivelConcluido + 1;
        fonteNormal.draw(batch, "Preparando nivel " + prox + "...", 190, 195);
        //instrução de continuar (bloqueada por meio segundo)
        if (timerEntrada <= 0f) {
            fonteDestaque.draw(batch, "Pressione ENTER para continuar", 100, 110);
        }

        batch.end();
        //aguarda soltar o ENTER que pode ter sido pressionado na tela anterior
        if (timerEntrada <= 0f
                && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            jogo.continuarProximoNivel(scoreAtual, prox);
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (fonteTitulo  != null) fonteTitulo.dispose();
        if (fonteNormal  != null) fonteNormal.dispose();
        if (fonteDestaque != null) fonteDestaque.dispose();
    }
}
