package com.spaceblaster;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

/*
  classe principal do jogo, ponto de entrada do LibGDX
 
  centraliza toda a navegação entre telas. Nenhuma tela deve instanciar outra diretamente, todas devem chamar um método deste Main
*/
public class Main extends Game {
    //ScoreManager compartilhado por todas as telas 
    private ScoreManager scoreManager;
    //ciclo de vida LibGDX 
    @Override
    public void create() {
        scoreManager = new ScoreManager();
        showMenu();
    }

    @Override
    public void dispose() {
        if (getScreen() != null) getScreen().dispose();
        super.dispose();
    }

    //navegação, métodos chamados pelas telas
    //vai para o menu principal
    public void showMenu() {
        trocarTela(new MenuScreen(this));
    }

    //vai para a tela de instruções (antes de começar a partida)
    public void showInstructions() {
        trocarTela(new InstructionsScreen(this));
    }

    /*
      inicia uma partida nova do zero
      score = 0, nível = GameConfig#START_LEVEL
    */
    public void startGame() {
        trocarTela(new GameScreen(this));
    }

    /*
      continua para o próximo nível mantendo o score
      chamado por LevelCompleteScreen quando o jogador pressiona ENTER
     
      scoreAtual = score acumulado (já inclui bônus do nível anterior)
      proximoNivel = número do próximo nível a ser iniciado
    */
    public void continuarProximoNivel(int scoreAtual, int proximoNivel) {
        trocarTela(new GameScreen(this, scoreAtual, proximoNivel));
    }

    /*
      exibe a tela de conclusão de nível
      chamado pelo GameScreen quando a wave( ou boss ) é derrotada
     
      nivelConcluido = número do nível que acabou de ser concluído
      scoreAtual = score total (já incluindo o bônus deste nível)
      bonusGanho = bônus de conclusão adicionado (mostrado na tela)
    */
    public void mostrarNivelCompleto(int nivelConcluido, int scoreAtual, int bonusGanho) {
        trocarTela(new LevelCompleteScreen(this, nivelConcluido, scoreAtual, bonusGanho));
    }
    //exibe a tela de game over com o score final
    public void showGameOver(int scoreFinal) {
        trocarTela(new GameOverScreen(this, scoreFinal, scoreManager));
    }
    //exibe a tela de high scores
    public void showHighScores() {
        trocarTela(new HighScoresScreen(this));
    }

    //acesso ao ScoreManager 
    /*
      retorna o ScoreManager compartilhado
      usado pelo GameScreen para salvar score ao final da partida

      retorna gerenciador de pontuações
    */
    public ScoreManager getScoreManager() {
        return scoreManager;
    }

    //privado
    //troca a tela atual pela nova e libera os recursos da anterior, evita vazamentos de memória   
    private void trocarTela(Screen novaTela) {
        Screen telaAtual = getScreen();
        setScreen(novaTela);
        if (telaAtual != null) telaAtual.dispose();
    }
}
