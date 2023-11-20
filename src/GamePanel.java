import javax.swing.*;

import org.junit.Ignore;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class GamePanel extends JPanel implements Runnable {

    /**
     * The width of the game window in pixels. The size of the window will be 2x
     * this pixel count.
     */
    public static final int WIDTH = 1600;
    /**
     * The height of the game window in pixels. The size of the window will be 2x
     * this pixel count.
     */
    public static final int HEIGHT = 800;

    /**
     * The size of a cell in pixels. The game board will be a grid of cells, each
     * with a size of CELL_SIZE x CELL_SIZE pixels.
     */
    public static final int CELL_SIZE = 25;

    /**
     * The width of the controls panel in pixels. The controls panel will be a
     * rectangle of WIDTH x CONTROLS_HEIGHT pixels on the bottom of the screen.
     */
    public static final int CONTROLS_HEIGHT = 40;

    // The number of columns in the game grid
    private int gridColumns = WIDTH / CELL_SIZE;
    // The number of rows in the game grid
    private int gridRows = (HEIGHT - CONTROLS_HEIGHT) / CELL_SIZE;
    private int borderWidth = Math.max(1, CELL_SIZE / 10);

    private Thread thread;
    private boolean running;
    private double averageFPS;
    private GameOfLife game;
    private boolean[][] gameBoard;
    private boolean toggleType;
    private Point lastToggledCell;

    private Graphics2D g;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocus();

        // Create a GameOfLife object to manage the game state
        game = createGameOfLife();

        // Add a key listener to the panel to allow the user to iterate the game using
        // the space bar
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_SPACE:
                        game.tick();
                        break;
                    case KeyEvent.VK_R:
                        game.populate();
                        break;
                    case KeyEvent.VK_C:
                        game.clear();
                        break;
                }
            }
        });

        // Toggle the state of the cell when the mouse is clicked on the cell
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // get the x and y coordinates of the mouse click and divide by the cell size to
                // get the cell coordinates
                int x = e.getX() / CELL_SIZE;
                int y = e.getY() / CELL_SIZE;
                toggleType = game.getCell(x, y);

                // Toggle the state of the cell
                game.toggle(x, y);
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getX() / CELL_SIZE;
                int y = e.getY() / CELL_SIZE;
                Point currentCell = new Point(x, y);

                if (!currentCell.equals(lastToggledCell) && game.getCell(x, y) == toggleType) {
                    game.toggle(x, y);
                    lastToggledCell = currentCell;
                }
            }
        });
    }

    // Override this method to create a custom GameOfLife object for testing or
    // other purposes
    protected GameOfLife createGameOfLife() {
        return new GameOfLife(gridColumns, gridRows);
    }

    public void addNotify() {
        super.addNotify();
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void run() {
        running = true;
        g = (Graphics2D) getGraphics();

        long startTime;
        long elapsedTimeMillis;
        long waitTime;
        long totalTime = 0;

        int frameCount = 0;
        int maxFrameCount = 60;

        long targetTime = 1000 / maxFrameCount;

        while (running) {
            startTime = System.nanoTime();

            tick();
            repaint();

            elapsedTimeMillis = (System.nanoTime() - startTime) / 1_000_000;
            waitTime = targetTime - elapsedTimeMillis;

            if (waitTime > 0) {
                try {
                    Thread.sleep(waitTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // calculates fps by counting to a maxFrameCount and then dividing by the time
            // taken
            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == maxFrameCount) {
                averageFPS = 1_000_000_000.0 * frameCount / totalTime;
                frameCount = 0;
                totalTime = 0;
            }
        }
    }

    public void tick() {
        repaint();
    }

    /*public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Graphics g2 = this.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.drawImage(image, 0, 0, null);
        g.dispose();
    } */

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Create an off-screen image to draw on
        Image offScreenImage = createImage(getWidth(), getHeight());
        Graphics offScreenGraphics = offScreenImage.getGraphics();

        drawToImage(offScreenGraphics);
        // Draw on the off-screen image
        // ...

        // Draw the off-screen image to the screen
        g.drawImage(offScreenImage, 0, 0, this);

        // Dispose of the off-screen graphics object
        offScreenGraphics.dispose();
    }

    public void drawToImage(Graphics offScreenGraphics) {
        Graphics g = offScreenGraphics;
        gameBoard = game.getGrid();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        // Draw the game board
        for (int x = 0; x < gameBoard.length; x++) {
            for (int y = 0; y < gameBoard[0].length; y++) {
                if (gameBoard[x][y]) {
                    // fill the cell, leaving borderWidth for the border
                    g.setColor(Color.GREEN); // alive cells are green
                } else {
                    // fill the cell, leaving borderWidth pixel for the border
                    g.setColor(Color.WHITE); // dead cells are yellow
                }
                g.fillRect(x * CELL_SIZE + borderWidth, y * CELL_SIZE + borderWidth, CELL_SIZE - (borderWidth * 2),
                        CELL_SIZE - (borderWidth * 2));
            }
        }

        fpsCounter(g);
    }

    // draws the fps in the top right corner of the screen
    private void fpsCounter(Graphics g) {
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.setColor(Color.GREEN);
        String fps = String.format("FPS: %.2f", averageFPS);
        // draws a string with the fps in the top right corner of the screen with a 5
        // pixel margin
        g.drawString(fps, WIDTH - ((fps.length() * 6) + 5), 12);
    }
}
