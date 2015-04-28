package game;
import java.awt.Frame;






public class Main {
    public static void main(String args[]) {
        Game game = new Game();
        Frame frame = new Frame();
        frame.setSize(game.windowSize, game.windowSize);
        frame.add(game);
        frame.setVisible(true);
        frame.setResizable(false);
        
      }
}
