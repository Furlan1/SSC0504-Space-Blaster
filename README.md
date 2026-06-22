# Space Blaster

**Space Blaster** é um jogo 2D de nave espacial desenvolvido em **Java** com **LibGDX/LWJGL3**.  
O objetivo é destruir asteroides, enfrentar inimigos e derrotar o boss para concluir as 4 fases.

## 🎮 Sobre o jogo

No comando da sua nave, você precisa sobreviver às ondas de asteroides, eliminar inimigos e acumular pontos.  
A dificuldade aumenta a cada nível e a última fase traz uma batalha contra um boss com duas fases de ataque.

## ✨ Funcionalidades

- 4 níveis de progressão
- Boss na fase final
- Sistema de vidas
- Pontuação por asteroides, inimigos e boss
- Tela de instruções
- Tela de game over
- Tela de vitória
- Placar local com top 5 scores
- Menu principal com navegação por teclado
- Efeitos sonoros e sprites do jogo

## 🕹️ Controles

### Menu
- **↑ / ↓**: navegar pelas opções
- **ENTER**: confirmar

### Durante a partida
- **← / →**: mover a nave
- **ESPAÇO**: atirar
- **ESC**: voltar ao menu principal

### Telas de fim de jogo
- **↑ / ↓**: navegar
- **ENTER**: confirmar

## 🧩 Regras básicas

- Destrua asteroides para ganhar pontos
- Evite colisões com asteroides, inimigos e tiros inimigos
- O jogador começa com **3 vidas**
- Nas fases 1, 2 e 3, é preciso destruir uma quantidade mínima de asteroides para concluir o nível
- Na fase 4, o objetivo é derrotar o boss

## 🛠️ Tecnologias utilizadas

- **Java 8**
- **LibGDX**
- **LWJGL3**
- **Gradle**

## 🚀 Como executar o projeto

### Pré-requisitos
- Java 8 ou superior
- Gradle Wrapper incluído no projeto

### Executando no desktop
No terminal, dentro da pasta do projeto, execute:

```bash
./gradlew lwjgl3:run
```

No Windows:

```bash
gradlew.bat lwjgl3:run
```

## 📁 Estrutura do projeto

- `core/` — lógica principal do jogo
- `lwjgl3/` — launcher desktop
- `assets/` — imagens, sons e demais recursos
- `scores.txt` — arquivo usado para salvar os melhores scores localmente

## 🏆 Sistema de pontuação

O jogo salva automaticamente os melhores resultados no arquivo `scores.txt`, mantendo o top 5 de pontuações.

## 👨‍💻 Créditos

- Caio Mendes Laprega   NUSP: 17018950
- Gustavo Furlan Lourenço   NUSP: 16871332
- Tainá Felinto de Araujo   NUSP: 16922294

Projeto desenvolvido em Java com LibGDX.

---

Se quiser, também posso adaptar este README para um estilo mais **profissional**, mais **acadêmico** ou mais **“GitHub bonito”** com badges e seções extras.
