package domino;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DominoSetTest {

    @Test
    void testDominoSetCreation() {
        DominoSet set = new DominoSet();

        assertEquals(28, set.size());
        assertFalse(set.isEmpty());
    }

    @Test
    void testDrawSingleDomino() {
        DominoSet set = new DominoSet();
        int initialSize = set.size();

        Domino domino = set.draw();

        assertNotNull(domino);
        assertEquals(initialSize - 1, set.size());
    }

    @Test
    void testDrawMultipleDominoes() {
        DominoSet set = new DominoSet();

        List<Domino> dominoes = set.draw(7);

        assertEquals(7, dominoes.size());
        assertEquals(21, set.size());
    }

    @Test
    void testDrawFromEmptySet() {
        DominoSet set = new DominoSet();

        while (!set.isEmpty()) {
            set.draw();
        }

        assertNull(set.draw());
        assertTrue(set.isEmpty());
    }

    @Test
    void testShuffle() {
        DominoSet set1 = new DominoSet();
        DominoSet set2 = new DominoSet();

        assertEquals(set1.size(), set2.size());

        set2.shuffle();

        assertEquals(set1.size(), set2.size());
    }
}