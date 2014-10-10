package views;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JFrame;


public class Game extends JFrame {
	private static final long serialVersionUID = 1L;

	
	private Level curLevel;
	private BorderLayout layoutManager;
	Game(){
		super("Awesome Game!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		curLevel = new Level(new File("Levels/Level1.txt"));
		
		layoutManager = new BorderLayout();
		setLayout(layoutManager);
		
		
		setResizable(false);
		add(curLevel);
		pack();
	}
	
	public static void main(String args[]){
		Game game = new Game();
		game.setLocationRelativeTo(null);
		game.setVisible(true);
		game.curLevel.requestFocus();
	}

}
