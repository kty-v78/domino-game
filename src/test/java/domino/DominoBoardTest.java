package domino;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для класса DominoBoard - игровой доски домино.
 */
class DominoBoardTest {
    private DominoBoard board;

    @BeforeEach
    void setUp() {
        board = new DominoBoard();
    }

    @Test
    void testEmptyBoard() {
        assertTrue(board.isEmpty());
        assertEquals(-1, board.getLeftEnd());
        assertEquals(-1, board.getRightEnd());
        assertEquals("Доска пуста", board.toString());
    }

    @Test
    void testAddFirstDomino() {
        Domino domino = new Domino(3, 4);

        assertTrue(board.playDomino(domino));
        assertFalse(board.isEmpty());
        assertEquals(3, board.getLeftEnd());
        assertEquals(4, board.getRightEnd());
    }

    @Test
    void testAddToLeft() {
        // Первая костяшка
        Domino first = new Domino(3, 4);
        board.playDomino(first);

        // Добавляем слева
        Domino leftDomino = new Domino(2, 3);
        assertTrue(board.addToLeft(leftDomino));
        assertEquals(2, board.getLeftEnd());
        assertEquals(4, board.getRightEnd());
    }

    @Test
    void testAddToRight() {
        // Первая костяшка
        Domino first = new Domino(3, 4);
        board.playDomino(first);

        // Добавляем справа
        Domino rightDomino = new Domino(4, 5);
        assertTrue(board.addToRight(rightDomino));
        assertEquals(3, board.getLeftEnd());
        assertEquals(5, board.getRightEnd());
    }

    @Test
    void testAddInvalidDomino() {
        // Первая костяшка
        Domino first = new Domino(3, 4);
        board.playDomino(first);

        // добавить неподходящую костяшку
        Domino invalid = new Domino(1, 2);
        assertFalse(board.playDomino(invalid));
        assertFalse(board.addToLeft(invalid));
        assertFalse(board.addToRight(invalid));
    }

    @Test
    void testAutoFlipOnAdd() {
        // Первая костяшка
        Domino first = new Domino(3, 4);
        board.playDomino(first);

        Domino needsFlip = new Domino(5, 3);
        assertTrue(board.addToLeft(needsFlip));
        assertEquals(5, board.getLeftEnd());
    }

    @Test
    void testBoardState() {
        // Создаем последовательность: [2|3][3|4][4|5]
        board.playDomino(new Domino(3, 4));
        board.addToLeft(new Domino(2, 3));
        board.addToRight(new Domino(4, 5));

        assertFalse(board.isEmpty());
        assertEquals(2, board.getLeftEnd());
        assertEquals(5, board.getRightEnd());

        assertEquals(3, board.getBoard().size());
    }
}