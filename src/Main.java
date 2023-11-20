import javax.swing.JFrame;

import java.awt.*;

public class Main {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Main");
        GameOfLife game = new GameOfLife();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        frame.add(new GamePanel(game), BorderLayout.CENTER);
        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);

    }
}
