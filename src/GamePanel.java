import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable {

    public static final int WIDTH = 400;
    public static final int HEIGHT = 400;

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

            gameUpdate();
            gameRender();
            gameDraw();

            elapsedTimeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - elapsedTimeMillis;

            try {
                Thread.sleep(waitTime);
            } catch (Exception e) {
                e.printStackTrace();
            }

            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == maxFrameCount) {
                averageFPS = 1000.0 / ((totalTime / frameCount) / 1000000);
                frameCount = 0;
                totalTime = 0;
            }
        }
    }

    public void gameUpdate() {
    }

    public void gameRender() {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);
    }

    public void gameDraw() {
        Graphics g2 = this.getGraphics();
        g2.drawImage(image, WIDTH, HEIGHT, null);
        g2.dispose();
    }

    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

    public void drawToImage() {
        Graphics g = image.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
    }
}


