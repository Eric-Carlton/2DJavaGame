package views;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.util.LinkedList;

import javax.swing.JFrame;

import models.Direction;


public class Game extends JFrame implements MouseWheelListener, MouseListener {
	private static final long serialVersionUID = 1L;
	private static final int desiredFPS = 60;


	private Level curLevel;
	private BorderLayout layoutManager;
	private static long lastTick;
	
	Game(){
		super("Awesome Game!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		curLevel = new Level(new File("Levels/Level1.txt"));

		layoutManager = new BorderLayout();
		setLayout(layoutManager);


		setResizable(false);
		add(curLevel);

		addKeyListener(new KeyListener() {

			Thread up;
			Thread down;
			Thread left;
			Thread right;

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {

				int keyCode = e.getKeyCode();

				switch( keyCode ) { 
				case KeyEvent.VK_W:

					up = new Thread(){
						public void run(){
							curLevel.movePlayerUp();
						}
					};
					up.start();

					break;
				case KeyEvent.VK_A:

					left = new Thread(){
						public void run(){
							curLevel.movePlayerLeft();
						}
					};
					left.start();

					break;
				case KeyEvent.VK_S:

					down = new Thread(){
						public void run(){
							curLevel.movePlayerDown();
						}
					};
					down.start();

					break;
				case KeyEvent.VK_D:

					right = new Thread(){
						public void run(){
							curLevel.movePlayerRight();
						}
					};
					right.start();
					break;

				case KeyEvent.VK_UP:
					curLevel.moveMapUp();
					break;
				case KeyEvent.VK_DOWN:
					curLevel.moveMapDown();
					break;
				case KeyEvent.VK_LEFT:
					curLevel.moveMapLeft();
					break;
				case KeyEvent.VK_RIGHT :
					curLevel.moveMapRight();
					break;
				case KeyEvent.VK_ESCAPE : 
					System.exit(0);
					break;
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				int keyCode = e.getKeyCode();

				switch( keyCode ) { 

				case KeyEvent.VK_W:
					up.interrupt();
					break;
				case KeyEvent.VK_A:
					left.interrupt();
					break;
				case KeyEvent.VK_S:
					down.interrupt();
					break;
				case KeyEvent.VK_D:
					right.interrupt();
					break;

				default:
					break;
				}
			}
		});
		
		addMouseWheelListener(this);
		addMouseListener(this);

		pack();
	}

	public static void main(String args[]){
		Game game = new Game();
		game.setLocationRelativeTo(null);
		game.setVisible(true);
		game.requestFocus();
		game.startTicking();
		while(true){
			game.tick();
		}
	}

	public void startTicking(){
		lastTick = System.currentTimeMillis();
	}

	public void tick(){
		
		long curTime = System.currentTimeMillis();
		
		if(curTime - lastTick >= 1000/desiredFPS){
			curLevel.repaint();
			lastTick = curTime;
		}
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {

		int units = e.getWheelRotation();

		if (e.isShiftDown()) {
			if(units > 0){
				curLevel.moveMapRight();
			}else{
				curLevel.moveMapLeft();
			}
		} else {
			if(units > 0){
				curLevel.moveMapDown();
			}else{
				curLevel.moveMapUp();
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent click) {
		
		views.Level.AutoMover autoMover = curLevel.new AutoMover();
		
		Point clickedPoint = click.getPoint();
		Point tileLocRelativeToViewableScreen = new Point( clickedPoint.x / curLevel.tileSize.width, clickedPoint.y / curLevel.tileSize.height);

		LinkedList<Direction> path = autoMover.getPlayerPathToTileLocation(curLevel.getTileLocInFullMap(tileLocRelativeToViewableScreen));
		
		curLevel.moveAlongPath(path);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
