import javax.swing.JFrame;

import java.awt.*;


public class Main {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Main");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        frame.add(new GamePanel(), BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);

        int[][] arr = {{0,0,0,0},{0,1,1, 1},{0,1,0,0},{0,0,0,0}};
        GameOfLife game = new GameOfLife(arr);
        System.out.println(game);
        System.out.println("--------------------");
        game.tick();
        System.out.println(game);
        System.out.println("--------------------");
        game.tick();
        System.out.println(game);
        System.out.println("--------------------");
        game.tick();
        System.out.println(game);
    }
}
