package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
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
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import models.Direction;


public class Game extends JFrame implements MouseWheelListener, MouseListener {
	private static final long serialVersionUID = 1L;
	final int TARGET_FPS = 60;
	final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;  


	private Level curLevel;
	private BorderLayout layoutManager;
	private int fps;
	private long lastFpsTime;
	private boolean gameRunning;
	private boolean paused;
	JLabel pausedLbl = new JLabel("PAUSED");

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
				if(!paused){
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
					}
				}
				
				//these keys should be picked up even if the game is paused
				if(keyCode == KeyEvent.VK_P){
					if(up != null)
						up.interrupt();
					if(down != null)
						down.interrupt();
					if(left != null)
						left.interrupt();
					if(right != null)
						right.interrupt();
					
					pause();
				}
					
				if(keyCode == KeyEvent.VK_ESCAPE)
					System.exit(0);

			}
			@Override
			public void keyReleased(KeyEvent e) {

				if(!paused){
					int keyCode = e.getKeyCode();

					switch( keyCode ) { 

					case KeyEvent.VK_W:
						if(up != null)
							up.interrupt();
						break;
					case KeyEvent.VK_A:
						if(left != null)
							left.interrupt();
						break;
					case KeyEvent.VK_S:
						if(down != null)
							down.interrupt();
						break;
					case KeyEvent.VK_D:
						if(right != null)
							right.interrupt();
						break;

					default:
						break;
					}
				}
			}

		});

		addMouseWheelListener(this);
		addMouseListener(this);

		gameRunning = true;
		paused = false;
		
		pausedLbl = new JLabel("PAUSED", SwingConstants.CENTER);
		
		pack();
	}

	public static void main(String args[]){
		final Game game = new Game();
		game.setLocationRelativeTo(null);
		game.requestFocus();
		game.setVisible(true);
		Thread loop = new Thread()
	      {
	         public void run()
	         {
	            game.startGameLoop();
	         }
	      };
	      loop.start();
	}

	public void startGameLoop(){

		long lastLoopTime = System.nanoTime();

		while (gameRunning)
		{
			if(!paused){
				// work out how long its been since the last update, this
				// will be used to calculate how far the entities should
				// move this loop
				long now = System.nanoTime();
				long updateLength = now - lastLoopTime;
				lastLoopTime = now;
				//used to update physics, should any be added
				@SuppressWarnings("unused")
				double delta = updateLength / ((double)OPTIMAL_TIME);

				// update the frame counter
				lastFpsTime += updateLength;
				fps++;

				// update our FPS counter if a second has passed since
				// we last recorded
				if (lastFpsTime >= 1000000000)
				{
					setTitle("Awesome Game ( " + fps + " ) fps");
					lastFpsTime = 0;
					fps = 0;
				}



				// draw everything
				render();

				// we want each frame to take 10 milliseconds, to do this
				// we've recorded when we started the frame. We add 10 milliseconds
				// to this and then factor in the current time to give 
				// us our final value to wait for
				// remember this is in ms, whereas our lastLoopTime etc. vars are in ns.
				try{Thread.sleep( (lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000 );}
				catch(Exception e){
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	public void pause(){
		if(paused){
			paused = false;
			curLevel.remove(pausedLbl);
		}
		else{
			paused = true;
			
			pausedLbl.setBounds(curLevel.getWidth()/2 - 50, curLevel.getHeight()/2 - 50, 100, 100);
			
			Font font = new Font("TIMES NEW ROMAN", Font.BOLD, 20);
			pausedLbl.setFont(font);
			
			pausedLbl.setForeground(Color.white);
			
			curLevel.add(pausedLbl);
			repaint();
		}
	}
	
	public void render(){
		curLevel.repaint();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {

		if(!paused){
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
	}

	@Override
	public void mouseClicked(MouseEvent click) {

		if(!paused){
			views.Level.AutoMover autoMover = curLevel.new AutoMover();

			Point clickedPoint = click.getPoint();
			Point tileLocRelativeToViewableScreen = new Point( clickedPoint.x / curLevel.tileSize.width, clickedPoint.y / curLevel.tileSize.height);

			LinkedList<Direction> path = autoMover.getPlayerPathToTileLocation(curLevel.getTileLocInFullMap(tileLocRelativeToViewableScreen));

			curLevel.moveAlongPath(path);
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

}
