import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameOfLifeTest {
    private GameOfLife game;

    @BeforeEach
    public void setUp() {
        game = new GameOfLife( 5, 5);
    }

    @Test
    public void testToggle() {
        game.toggle(1, 1);
        assertTrue(game.getGrid()[1][1]);
        game.toggle(1, 1);
        assertFalse(game.getGrid()[1][1]);
    }

    @Test
    public void testPopulate() {
        game.populate();
        boolean[][] grid = game.getGrid();
        for (boolean[] row : grid) {
            for (boolean cell : row) {
                assertTrue(cell || !cell);
            }
        }
    }

    @Test
    public void testClear() {
        game.clear();
        boolean[][] grid = game.getGrid();
        for (boolean[] row : grid) {
            for (boolean cell : row) {
                assertFalse(cell);
            }
        }
    }

    @Test
    public void testTick() {
        game.clear();
        game.toggle(1, 1);
        game.toggle(1, 2);
        game.toggle(1, 3);
        game.tick();
        assertFalse(game.getGrid()[1][1]);
        assertTrue(game.getGrid()[1][2]);
        assertFalse(game.getGrid()[1][3]);
        assertTrue(game.getGrid()[0][2]);
        assertTrue(game.getGrid()[2][2]);
    }
}