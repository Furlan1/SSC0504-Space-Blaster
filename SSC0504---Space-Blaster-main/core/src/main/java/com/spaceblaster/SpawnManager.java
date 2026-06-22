package com.spaceblaster;

import com.badlogic.gdx.math.MathUtils;

import java.util.List;

/*
  controla o spawn de asteroides e inimigos de acordo com o nível atual
 
  P2 — implementar esta classe.O stub atual já spawna asteroides com velocidade aleatória dentro dos limites do nível (usando
  LevelManager), conta a wave e inimigos por nível. Refinar contagem de wave, posicionamento dos inimigos, etc.
*/
public class SpawnManager {

    //estado
    private float timerSpawn;          // acumulador de tempo 
    private int asteroideSpawnados;    // quantos foram criados na wave atual
    private boolean inimigosCriados;   // inimigos só aparecem uma vez por nível

    //construtor
    // cria o SpawnManager no estado inicial
    public SpawnManager() {
        resetar(GameConfig.START_LEVEL);
    }

    //API para o GameScreen 
    /*
      atualiza o timer e spawna entidades quando necessário
     
      delta = delta-time em segundos (LibGDX padrão)
      lvl = gerenciador de nível, fornece velocidade e intervalo
      asteroides = lista de asteroides ativos, entidades são adicionadas aqui
      inimigos = lista de inimigos ativos, entidades são adicionadas aqui
    */
    public void update(float delta, LevelManager lvl,
                       List<Asteroid> asteroides, List<Enemy> inimigos,
                       int asteroidesDestruidos) {
        int totalWave = lvl.getTamanhoWave();

        //nas fases normais, os asteroides continuam aparecendo enquanto
        // o jogador ainda não atingiu o objetivo de asteroides destruídos.
        //na fase do boss, mantemos uma wave limitada de asteroides como pressão extra
        boolean deveSpawnarAsteroides = lvl.ehNivelBoss()
                        ? asteroideSpawnados < totalWave
                        : asteroidesDestruidos < lvl.getObjetivoAsteroides();
                        
        //spawn de asteroides 
        if (deveSpawnarAsteroides) {
            timerSpawn -= delta;
            if (timerSpawn <= 0f) {
                timerSpawn = lvl.getIntervaloSpawn();

                float velocidade = MathUtils.random(
                        lvl.getVelMinAsteroide(),
                        lvl.getVelMaxAsteroide());

                //posição X aleatória com margem nas bordas
                float margem = 30f;
                float posX = MathUtils.random(margem,
                        GameConfig.WINDOW_WIDTH - margem - 32f);
                //aparece acima do topo da tela
                asteroides.add(new Asteroid(posX,
                        GameConfig.WINDOW_HEIGHT + 10f, velocidade));

                asteroideSpawnados++;
            }
        }
        // spawn de inimigos (uma vez por nível, ao atingir 50 % da wave)
        if (lvl.temInimigos() && !inimigosCriados
                && asteroideSpawnados >= totalWave / 2) {
            criarFormacaoInimigos(inimigos);
            inimigosCriados = true;
        }
    }

    /*
      indica se todos os asteroides da wave já foram criados
      o GameScreen usa isso para saber quando a wave está completa (além de verificar que as listas de entidades estão vazias)
      lvl = nível atual
      retorna true se a cota de asteroides do nível foi completamente spawnada
    */
    public boolean waveCompleta(LevelManager lvl) {
        return asteroideSpawnados >= lvl.getTamanhoWave();
    }

    /*
      reinicia o estado do SpawnManager para um novo nível 
      nivelAtual = número do nível que está começando (1 a 4)
    */
    public void resetar(int nivelAtual) {
        timerSpawn          = 0.5f; // pequeno delay inicial antes do primeiro spawn
        asteroideSpawnados  = 0;
        inimigosCriados     = false;
    }

    //privado
    //cria uma fileira de inimigos no topo da tela
    private void criarFormacaoInimigos(List<Enemy> inimigos) {
        int quantidade = 3; // TODO P2: variar por nível
        float espacamento = GameConfig.WINDOW_WIDTH / (float)(quantidade + 1);
        float posY = GameConfig.WINDOW_HEIGHT - 80f;
        for (int i = 1; i <= quantidade; i++) {
            float posX = espacamento * i - Enemy.LARGURA / 2f;
            // alterna direção para os inimigos não se moverem todos para o mesmo lado
            float vel = (i % 2 == 0) ? 90f : -90f;
            inimigos.add(new Enemy(posX, posY, vel));
        }
    }
}
