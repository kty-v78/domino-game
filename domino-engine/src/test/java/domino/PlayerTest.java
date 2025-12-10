package domino;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для класса Player - игрока в домино.
 */
class PlayerTest {
    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("Тестовый Игрок");
    }

    @Test
    void testPlayerCreation() {
        assertEquals("Тестовый Игрок", player.getName());
        assertTrue(player.getHand().isEmpty());
        assertEquals(0, player.getHandSize());
        assertFalse(player.hasDominoes());
    }

    @Test
    void testAddAndRemoveDomino() {
        Domino domino = new Domino(3, 4);

        player.addDomino(domino);
        assertEquals(1, player.getHandSize());
        assertTrue(player.hasDominoes());

        assertTrue(player.removeDomino(domino));
        assertEquals(0, player.getHandSize());
        assertFalse(player.hasDominoes());
    }

    @Test
    void testHasPlayableDomino() {
        Domino playable = new Domino(3, 4);
        player.addDomino(playable);

        assertTrue(player.hasPlayerDomino(3, 5));
        assertTrue(player.hasPlayerDomino(2, 4));
        assertFalse(player.hasPlayerDomino(1, 2));
    }

    @Test
    void testGetHandValue() {
        Domino domino1 = new Domino(3, 4);
        Domino domino2 = new Domino(6, 6);

        player.addDomino(domino1);
        player.addDomino(domino2);

        assertEquals(19, player.getHandValue());
    }
}