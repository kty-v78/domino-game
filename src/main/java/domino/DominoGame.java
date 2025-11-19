package domino;

import java.util.ArrayList;
import java.util.List;

/**
 * Основной класс игрового движка домино.
 * Управляет логикой игры, ходами игроков, состоянием игры и определением победителя.
 */
public class DominoGame {
    private DominoSet dominoSet;
    private DominoBoard board;
    private List<Player> players;
    private int currentPlayerIndex;
    private GameState gameState;
    private Player winner;

    /**
     * Конструктор игры в домино.
     * Создает новую игру с указанными игроками.
     *
     * @param playerNames список имен игроков для участия в игре
     * @throws IllegalArgumentException если передано меньше 2 игроков
     */
    public DominoGame(List<String> playerNames) {
        if (playerNames == null || playerNames.size() < 2) {
            throw new IllegalArgumentException("Минимум 2 игрока");
        }

        this.dominoSet = new DominoSet();
        this.board = new DominoBoard();
        this.players = new ArrayList<>();
        this.gameState = GameState.NOT_STARTED;

        for (String name : playerNames) {
            players.add(new Player(name));
        }
    }

    /**
     * Начинает новую игру домино.
     * Инициализирует игровые компоненты, перемешивает костяшки и раздает их игрокам.
     * Устанавливает начальное состояние игры и определяет первого игрока.
     */
    public void startGame() {
        dominoSet = new DominoSet();
        dominoSet.shuffle();
        board = new DominoBoard();
        gameState = GameState.IN_PROGRESS;
        winner = null;
        currentPlayerIndex = 0;

        // Раздача костяшек (по 7 каждому игроку)
        for (Player player : players) {
            List<Domino> hand = dominoSet.draw(7);
            for (Domino domino : hand) {
                player.addDomino(domino);
            }
        }

        // Находим игрока с самым старшим дублем для первого хода
        determineFirstPlayer();
    }

    /**
     * Определяет первого игрока на основе самого старшего дубля.
     */
    private void determineFirstPlayer() {
        Domino highestDouble = null;
        int playerWithHighestDouble = -1;

        // Ищем самого старшего дубля среди всех игроков
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            for (Domino domino : player.getHand()) {
                if (domino.isDouble()) {
                    if (highestDouble == null || domino.getValue() > highestDouble.getValue()) {
                        highestDouble = domino;
                        playerWithHighestDouble = i;
                    }
                }
            }
        }

        // Если нашли дубль, ставим его на доску и делаем этого игрока текущим
        if (highestDouble != null && playerWithHighestDouble != -1) {
            currentPlayerIndex = playerWithHighestDouble;
            Player startingPlayer = players.get(currentPlayerIndex);
            startingPlayer.removeDomino(highestDouble);
            board.playDomino(highestDouble);
            System.out.println("Первый ход: " + startingPlayer.getName() + " выкладывает " + highestDouble);
        } else {
            // Если дублей нет, ищем самую старшую костяшку
            determineFirstPlayerByHighestDomino();
        }

        nextPlayer();
    }

    /**
     * Определяет первого игрока по самой старшей костяшке (если нет дублей).
     */
    private void determineFirstPlayerByHighestDomino() {
        Domino highestDomino = null;
        int playerWithHighestDomino = -1;

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            for (Domino domino : player.getHand()) {
                if (highestDomino == null || domino.getValue() > highestDomino.getValue()) {
                    highestDomino = domino;
                    playerWithHighestDomino = i;
                }
            }
        }

        if (highestDomino != null && playerWithHighestDomino != -1) {
            currentPlayerIndex = playerWithHighestDomino;
            Player startingPlayer = players.get(currentPlayerIndex);
            startingPlayer.removeDomino(highestDomino);
            board.playDomino(highestDomino);
            System.out.println("Первый ход: " + startingPlayer.getName() + " выкладывает " + highestDomino);
        }
        nextPlayer();
    }

    /**
     * Выполняет ход текущего игрока.
     * Игрок может либо сыграть костяшку на доску, либо взять костяшку из базара.
     *
     * @param domino костяшка для хода, или null если игрок берет из базара
     * @return true если костяшка была успешно сыграна на доску, false если игрок взял из базара или не может сделать ход
     */
    public boolean makeMove(Domino domino) {
        if (gameState != GameState.IN_PROGRESS) {
            return false;
        }

        Player currentPlayer = players.get(currentPlayerIndex);

        // Если игрок передал костяшку, пытаемся сыграть ей
        if (domino != null) {
            if (board.playDomino(domino)) {
                currentPlayer.removeDomino(domino);
                checkRoundEnd();
                if (!isGameOver()) {  // Исправлено: было game.isGameOver()
                    nextPlayer();
                }
                return true;
            }
        }

        // Если не может сыграть выбранную костяшку, проверяем есть ли вообще возможные ходы
        boolean hasPlayableDomino = currentPlayer.hasPlayerDomino(board.getLeftEnd(), board.getRightEnd());  // Исправлено: было hasPlayerDomino

        if (!hasPlayableDomino) {
            // Нет подходящих костяшек - берем из базара
            if (!dominoSet.isEmpty()) {
                Domino drawn = dominoSet.draw();
                if (drawn != null) {
                    currentPlayer.addDomino(drawn);
                    System.out.println(currentPlayer.getName() + " берет костяшку из базара: " + drawn);

                    // Проверяем, можно ли сыграть только что взятую костяшку
                    boolean canPlayDrawn = drawn.matches(board.getLeftEnd()) || drawn.matches(board.getRightEnd());
                    if (canPlayDrawn) {
                        // Может сыграть взятую костяшку сразу
                        if (board.playDomino(drawn)) {
                            currentPlayer.removeDomino(drawn);
                            System.out.println(currentPlayer.getName() + " сразу играет взятую костяшку: " + drawn);
                            checkRoundEnd();
                            if (!isGameOver()) {  // Исправлено: было game.isGameOver()
                                nextPlayer();
                            }
                            return true;
                        }
                    }
                }
            }

            // После взятия из базара (независимо от того, сыграли или нет) переходим к следующему игроку
            nextPlayer();
            return false;
        } else {
            // У игрока есть подходящие костяшки, но он выбрал неподходящую или хочет взять из базара
            if (!dominoSet.isEmpty() && domino == null) {
                // Игрок сознательно выбрал "Взять из базара"
                Domino drawn = dominoSet.draw();
                if (drawn != null) {
                    currentPlayer.addDomino(drawn);
                    System.out.println(currentPlayer.getName() + " берет костяшку из базара: " + drawn);
                }
            }
            nextPlayer();
            return false;
        }
    }

    /**
     * Переходит к следующему игроку в очереди.
     * Использует циклический переход для поддержания порядка ходов.
     */
    private void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    /**
     * Проверяет условия завершения игры.
     * Игра завершается если игрок избавился от всех костяшек или если игра заблокирована.
     */
    private void checkRoundEnd() {
        // Если у кого-то 0 костяшек — он победил
        for (Player p : players) {
            if (p.getHand().isEmpty()) {
                gameState = GameState.GAME_OVER;
                winner = p;
                return;
            }
        }

        // Проверяем, может ли КТО-НИБУДЬ сыграть
        boolean someoneCanPlay = players.stream()
                .anyMatch(p -> p.hasPlayerDomino(board.getLeftEnd(), board.getRightEnd()));

        // Если никто не может ходить, но БАЗАР ЕЩЁ НЕ ПУСТ — игра продолжается
        if (!someoneCanPlay && !dominoSet.isEmpty()) {
            return;
        }

        // Только если никто не может ходить И базар пуст — конец игры
        if (!someoneCanPlay) {
            gameState = GameState.GAME_OVER;
            determineWinnerByPoints();
        }
    }


    /**
     * Определяет победителя по очкам когда игра заблокирована.
     * Победителем становится игрок с наименьшей суммой очков в руке.
     */
    private void determineWinnerByPoints() {
        winner = players.stream()
                .min((p1, p2) -> Integer.compare(p1.getHandValue(), p2.getHandValue()))
                .orElse(null);
    }


    public DominoBoard getBoard() { return board; }
    public List<Player> getPlayers() { return new ArrayList<>(players); }
    public Player getCurrentPlayer() { return players.get(currentPlayerIndex); }
    public GameState getGameState() { return gameState; }
    public Player getWinner() { return winner; }
    public boolean isGameOver() { return gameState == GameState.GAME_OVER; }
    public int getRemainingDominoes() { return dominoSet.size(); }
}