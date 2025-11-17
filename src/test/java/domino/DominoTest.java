package domino;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DominoTest {
    @Test
    void testDominoCreation() {
        Domino domino = new Domino(3, 4);

        assertEquals(3, domino.getLeft());
        assertEquals(4, domino.getRight());
    }

    @Test
    void testDominoInvalidValues() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Domino(7, 2);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Domino(2, -1);
        });
    }

    @Test
    void testIsDouble() {
        Domino doubleDomino = new Domino(6, 6);
        Domino regularDomino = new Domino(3, 4);

        assertTrue(doubleDomino.isDouble());
        assertFalse(regularDomino.isDouble());
    }

    @Test
    void testGetValue() {
        Domino domino = new Domino(2, 5);

        assertEquals(7, domino.getValue());
    }

    @Test
    void testFlip() {
        Domino domino = new Domino(2, 5);
        Domino flipped = domino.flip();

        assertEquals(5, flipped.getLeft());
        assertEquals(2, flipped.getRight());
    }

    @Test
    void testMatches() {
        Domino domino = new Domino(3, 4);

        assertTrue(domino.matches(3));
        assertTrue(domino.matches(4));
        assertFalse(domino.matches(5));
    }

    @Test
    void testToString() {
        Domino domino = new Domino(1, 6);

        assertEquals("[1|6]", domino.toString());
    }
}
