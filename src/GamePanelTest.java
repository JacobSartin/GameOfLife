import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GamePanelTest {
    private FakeGameOfLife game;
    private GamePanel gamePanel;

    @BeforeEach
    public void setUp() {
        game = new FakeGameOfLife(5, 5);
        gamePanel = new TestGamePanel();
    }

    @Test
    public void testKeyPressed() {
        KeyEvent spaceEvent = new KeyEvent(gamePanel, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,
                KeyEvent.VK_SPACE, ' ');
        gamePanel.getKeyListeners()[0].keyPressed(spaceEvent);
        assertTrue(game.tickCalled);

        KeyEvent rEvent = new KeyEvent(gamePanel, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_R,
                'R');
        gamePanel.getKeyListeners()[0].keyPressed(rEvent);
        assertTrue(game.populateCalled);

        KeyEvent cEvent = new KeyEvent(gamePanel, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_C,
                'C');
        gamePanel.getKeyListeners()[0].keyPressed(cEvent);
        assertTrue(game.clearCalled);
    }

    @Test
    public void testMouseClicked() {
        MouseEvent clickEvent = new MouseEvent(gamePanel, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 50,
                50, 1, false);
        gamePanel.getMouseListeners()[0].mousePressed(clickEvent);

        System.out.println(game);
        assertTrue(game.toggleCalled);
        assertEquals(2, game.toggleX);
        assertEquals(2, game.toggleY);
    }

    private static class FakeGameOfLife extends GameOfLife {
        boolean tickCalled = false;
        boolean populateCalled = false;
        boolean clearCalled = false;
        boolean toggleCalled = false;
        int toggleX = -1;
        int toggleY = -1;

        public FakeGameOfLife(int width, int height) {
            super(width, height);
        }

        @Override
        public void tick() {
            tickCalled = true;
        }

        @Override
        public void populate() {
            populateCalled = true;
        }

        @Override
        public void clear() {
            clearCalled = true;
        }

        @Override
        public void toggle(int x, int y) {
            toggleCalled = true;
            toggleX = x;
            toggleY = y;
        }
    }

    private class TestGamePanel extends GamePanel {
        @Override
        protected GameOfLife createGameOfLife() {
            return game;
        }
    }
}