package com.spaceblaster;

/*
  gerencia a progressão dos 4 níveis do Space Blaster e fornece os parâmetros de dificuldade de cada nível ao GameScreen e ao SpawnManager
  
  Tabela de dificuldade
  
  nível │ vel. mín │ vel. máx │ intervalo │ inimigos │ boss │ bônus conclusão
 
    1      80 px/s   150 px/s    2,0 s        Não       Não        500 pts
    2     130 px/s   210 px/s    1,5 s        Sim       Não      1 000 pts
    3     180 px/s   270 px/s    1,0 s       Mais       Não      1 500 pts
    4     150 px/s   230 px/s    1,5 s        Não       Sim      2 000 pts
 
  nível 4 = Boss Stage: o Boss é a ameaça principal, asteroides continuam caindo. O nível termina quando o Boss é derrotado 
*/
public class LevelManager {

    //constantes públicas usadas pelo GameScreen

    //número total de níveis do jogo
    public static final int TOTAL_NIVEIS = 4;

    //número do nível do Boss
    public static final int NIVEL_BOSS   = 4;

    // pontos por asteroide destruído
    public static final int PONTOS_ASTEROIDE = 10;

    //pontos por inimigo destruído
    public static final int PONTOS_INIMIGO   = 25;

    //pontos por derrotar o Boss (bônus extra)
    public static final int PONTOS_BOSS      = 500;

    //tabela de dificuldade (índice = nível - 1) 

    private static final float[] VEL_MIN_ASTEROIDE = { 80f, 130f, 180f, 150f };
    private static final float[] VEL_MAX_ASTEROIDE = {150f, 210f, 270f, 230f };

    //intervalo em segundos entre spawns de asteroides. 
    private static final float[] INTERVALO_SPAWN   = { 2.0f, 1.5f, 1.0f, 1.5f };

    //tamanho da wave (total de asteroides que vão spawnar no nível)
    private static final int[]   TAMANHO_WAVE      = {  20,   30,  40,   15 };

    // Quantidade mínima de asteroides que o jogador precisa destruir para concluir cada nível.
    // Nas fases 1, 2 e 3, isso impede que o jogador vença apenas desviando dos asteroides.
    // Na fase 4, o objetivo principal é derrotar o Boss, então o valor fica 0.
    private static final int[] OBJETIVO_ASTEROIDES = {10, 16, 22, 0};

    //se inimigos regulares aparecem no nível
    private static final boolean[] TEM_INIMIGOS    = { false, true, true, false };

    //se o nível tem Boss (só o nível 4)
    private static final boolean[] TEM_BOSS        = { false, false, false, true };

    //bônus de pontos ao concluir o nível
    private static final int[] BONUS_CONCLUSAO     = { 500, 1000, 1500, 2000 };

    //estado
    private int nivelAtual;

    //construtores
    /*
      cria o gerenciador iniciando no nível informado 
      nivelInicial = nível de partida (1 a #TOTAL_NIVEIS)
    */
    public LevelManager(int nivelInicial) {
        this.nivelAtual = nivelInicial;
    }

    //cria o gerenciador iniciando no nível padrão (GameConfig#START_LEVEL)
    public LevelManager() {
        this(GameConfig.START_LEVEL);
    }

    //navegação de nivel
    //avança para o próximo nível (não pode ultrapassar #TOTAL_NIVEIS)
    public void avancarNivel() {
        if (nivelAtual < TOTAL_NIVEIS) nivelAtual++;
    }
    // reinicia para o nível padrão de início
    public void resetar() {
        nivelAtual = GameConfig.START_LEVEL;
    }

    //consultas de estado
    //retorna número do nível atual (1–4) 
    public int getNivelAtual() { return nivelAtual; }

    //retorna true se ainda existe próximo nível após o atual 
    public boolean temProximoNivel() { return nivelAtual < TOTAL_NIVEIS; }

    //retorna true se o nível atual é o nível do Boss 
    public boolean ehNivelBoss() { return nivelAtual == NIVEL_BOSS; }

    //parâmetros de dificuldade do nível atual
    //retorna velocidade mínima dos asteroides (px/s)
    public float getVelMinAsteroide() { return VEL_MIN_ASTEROIDE[nivelAtual - 1]; }

    //retorna velocidade máxima dos asteroides (px/s)
    public float getVelMaxAsteroide() { return VEL_MAX_ASTEROIDE[nivelAtual - 1]; }

    //retorna intervalo entre spawns de asteroides (segundos)
    public float getIntervaloSpawn()  { return INTERVALO_SPAWN[nivelAtual - 1]; }

    //retorna tamanho da wave — total de asteroides para este nível
    public int getTamanhoWave()       { return TAMANHO_WAVE[nivelAtual - 1]; }

    // Retorna quantos asteroides precisam ser destruídos para concluir o nível atual.
    // No nível do Boss, esse valor é 0 porque a vitória depende de derrotar o Boss.
    public int getObjetivoAsteroides() {
        return OBJETIVO_ASTEROIDES[nivelAtual - 1];
    }

    //retorna true se inimigos regulares aparecem neste nível
    public boolean temInimigos()      { return TEM_INIMIGOS[nivelAtual - 1]; }

    //retorna true se este nível tem Boss
    public boolean temBoss()          { return TEM_BOSS[nivelAtual - 1]; }

    //retorna bônus de pontos ao concluir o nível atual
    public int getBonusConclusao()    { return BONUS_CONCLUSAO[nivelAtual - 1]; }
}
