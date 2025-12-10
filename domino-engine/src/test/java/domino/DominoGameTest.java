package domino;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для класса DominoGame.
 */
class DominoGameTest {
    private DominoGame game;

    @BeforeEach
    void setUp() {
        List<String> playerNames = Arrays.asList("Игрок 1", "Игрок 2");
        game = new DominoGame(playerNames);
    }

    @Test
    void testGameCreation() {
        // Проверяем что игра создалась правильно
        assertNotNull(game.getBoard());
        assertEquals(2, game.getPlayers().size());
        assertEquals("Игрок 1", game.getPlayers().get(0).getName());
        assertEquals("Игрок 2", game.getPlayers().get(1).getName());
    }

    @Test
    void testStartGame() {
        game.startGame();

        // Проверяем что игроки получили костяшки
        Player player1 = game.getPlayers().get(0);
        Player player2 = game.getPlayers().get(1);

        assertTrue(player1.getHandSize() > 0, "Игрок 1 должен иметь костяшки");
        assertTrue(player2.getHandSize() > 0, "Игрок 2 должен иметь костяшки");

        // Проверяем что есть текущий игрок
        assertNotNull(game.getCurrentPlayer());
    }

    @Test
    void testGameNotOverAtStart() {
        game.startGame();

        // Игра не должна быть завершена в начале
        assertFalse(game.isGameOver());
        assertNull(game.getWinner());
    }

    @Test
    void testRemainingDominoes() {
        game.startGame();

        // После раздачи по 7 костяшек двум игрокам
        // в базаре должно остаться: 28 - 14 = 14 костяшек
        assertEquals(14, game.getRemainingDominoes());
    }

    @Test
    void testMakeMoveWithNull() {
        game.startGame();

        // Пытаемся сделать ход null (взять из базара)
        boolean result = game.makeMove(null);

        // Должен смениться игрок
        assertNotNull(game.getCurrentPlayer());
    }
    @Test
    void testMakeMoveWithValidDomino() {
        game.startGame();
        Player currentPlayer = game.getCurrentPlayer();

        // Находим костяшку которую можно сыграть на текущую доску
        Domino playableDomino = currentPlayer.getHand().stream()
                .filter(d -> d.matches(game.getBoard().getLeftEnd()) ||
                        d.matches(game.getBoard().getRightEnd()))
                .findFirst()
                .orElse(null);

        if (playableDomino != null) {
            int initialHandSize = currentPlayer.getHandSize();
            boolean result = game.makeMove(playableDomino);

            assertTrue(result, "Ход должен быть успешным");
            assertEquals(initialHandSize - 1, currentPlayer.getHandSize(), "Костяшка должна быть удалена из руки");
        }
    }
    @Test
    void testMakeMoveWithInvalidDomino() {
        game.startGame();
        Player currentPlayer = game.getCurrentPlayer();
        Player initialPlayer = currentPlayer;

        // Находим костяшку которую нельзя сыграть
        Domino unplayableDomino = currentPlayer.getHand().stream()
                .filter(d -> !d.matches(game.getBoard().getLeftEnd()) &&
                        !d.matches(game.getBoard().getRightEnd()))
                .findFirst()
                .orElse(null);

        if (unplayableDomino != null) {
            boolean result = game.makeMove(unplayableDomino);

            assertFalse(result, "Ход должен быть неуспешным");
            assertEquals(initialPlayer, game.getCurrentPlayer(), "Игрок не должен меняться при неудачном ходе");
        }
    }

}