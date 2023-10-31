import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable {

    public static final int WIDTH = 1600;
    public static final int HEIGHT = 800;

    private Thread thread;
    private boolean running;
    private double averageFPS;

    private Graphics2D g;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocus();
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

            // calculates the fps for the last maxFrameCount frames
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
        drawToImage();
    }

    /*
     * public void gameRender() {
     * g.setColor(Color.WHITE);
     * g.fillRect(0, 0, WIDTH, HEIGHT);
     * }
     */

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Graphics g2 = this.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.drawImage(image, 0, 0, null);
        g.dispose();
    }

    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

    public void drawToImage() {
        Graphics2D g = image.createGraphics();

        g.clearRect(0, 0, WIDTH, HEIGHT);

        fpsCounter(g);

        g.drawImage(image, 0, 0, null);
        g.dispose();
    }

    // draws the fps in the top right corner of the screen
    private void fpsCounter(Graphics2D g) {
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.setColor(Color.GREEN);
        String fps = String.format("FPS: %.2f", averageFPS);
        // draws a string with the fps in the top right corner of the screen with a 5
        // pixel margin
        g.drawString(fps, WIDTH - ((fps.length() * 6) + 5), 12);
    }
}
