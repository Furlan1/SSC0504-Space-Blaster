# Space Blaster

**Space Blaster** é um jogo 2D de nave espacial desenvolvido em **Java** com **LibGDX** e **LWJGL3**.  
O objetivo é sobreviver às ondas de asteroides, destruir inimigos, enfrentar o boss final e completar as 4 fases do jogo.

## Visão geral

No comando da sua nave, você precisa gerenciar movimentação, tiros e posicionamento para acumular pontos e avançar pelos níveis.  
A dificuldade cresce progressivamente: os asteroides ficam mais rápidos, surgem inimigos em fases intermediárias e, na fase final, o desafio é derrotar o boss.

## Principais recursos

- 4 níveis de progressão
- Fase final com boss
- Sistema de vidas
- Pontuação por asteroides, inimigos e boss
- Tela de instruções
- Tela de game over
- Tela de vitória
- Placar local com top 5 scores
- Menu principal com navegação por teclado
- Efeitos sonoros e sprites do jogo

## Controles

### Menu e telas de navegação
- **↑ / ↓**: navegar pelas opções
- **ENTER**: confirmar a seleção

### Durante a partida
- **← / →**: mover a nave
- **ESPAÇO**: atirar
- **ESC**: voltar ao menu principal

### Telas de fim de jogo
- **↑ / ↓**: navegar pelas opções
- **ENTER**: confirmar

## Regras do jogo

- Destrua asteroides para ganhar pontos
- Evite colisões com asteroides, inimigos e tiros inimigos
- O jogador começa com **3 vidas**
- Nas fases 1, 2 e 3, é preciso destruir uma quantidade mínima de asteroides para concluir o nível
- Na fase 4, o objetivo é derrotar o boss

## Progressão dos níveis

O jogo possui 4 fases, cada uma com parâmetros próprios de dificuldade:

| Nível | Velocidade dos asteroides | Intervalo de spawn | Inimigos | Boss | Bônus de conclusão |
|------:|---------------------------:|-------------------:|:--------:|:----:|-------------------:|
| 1 | 80–150 px/s | 1,8 s | Não | Não | 500 pts |
| 2 | 130–210 px/s | 1,3 s | Sim | Não | 1.000 pts |
| 3 | 180–270 px/s | 0,9 s | Sim | Não | 1.500 pts |
| 4 | 150–230 px/s | 1,4 s | Não | Sim | 2.000 pts |

## Pontuação

- **Asteroide destruído**: 10 pontos
- **Inimigo destruído**: 25 pontos
- **Boss derrotado**: 500 pontos
- **Bônus por concluir o nível**: varia conforme a fase

Os melhores resultados são salvos localmente no arquivo `scores.txt`, mantendo apenas o top 5.

## Tecnologias utilizadas

- **Java 8**
- **LibGDX 1.14.2**
- **LWJGL3 3.4.1**
- **Gradle**

## Como executar

### Pré-requisitos
- Java 8 ou superior
- Gradle Wrapper incluído no projeto

### Execução no desktop

Entre na pasta raiz do projeto e execute:

```bash
./gradlew lwjgl3:run
```

No Windows:

```bash
gradlew.bat lwjgl3:run
```

Se o projeto estiver dentro da pasta `SSC0504---Space-Blaster-main`, acesse essa pasta antes de rodar o comando.

## Estrutura do projeto

- `core/` — lógica principal do jogo
- `lwjgl3/` — launcher desktop
- `assets/` — imagens, sons e demais recursos
- `scores.txt` — arquivo usado para salvar os melhores scores localmente

## Arquitetura em alto nível

O projeto organiza a navegação entre telas em uma classe principal (`Main`) e separa a lógica do jogo em componentes como:

- `GameScreen` — partida principal
- `SpawnManager` — geração de asteroides e inimigos
- `LevelManager` — regras de progressão e dificuldade
- `ScoreManager` — persistência do ranking local

Essa divisão ajuda a manter o código mais limpo e facilita futuras expansões do jogo.

## Créditos

- Caio Mendes Laprega — NUSP: 17018950
- Gustavo Furlan Lourenço — NUSP: 16871332
- Tainá Felinto de Araujo — NUSP: 16922294

Projeto desenvolvido em Java com LibGDX.
