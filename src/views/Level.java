package views;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.IOException;


import javax.swing.JPanel;

import controllers.MotionController;
import controllers.ViewableMapController;
import models.BadDimensionException;
import models.LoadedMap;
import models.Map;
import models.Tile;

public class Level extends JPanel implements MouseWheelListener {
	private static final long serialVersionUID = 1L;

	Map map;
	private ViewableMapController mapController;
	private MotionController motionController;

	public Level(File mapFile){
		super();
		try {
			long started = System.currentTimeMillis();
			mapController = new ViewableMapController(new LoadedMap(mapFile, new Dimension(40,40)), new Dimension(10,8), new Point(0,0));
			long done = System.currentTimeMillis();
			System.out.printf("Time to load map: %d millis\n\n", done-started);
		} catch (BadDimensionException e) {
			e.printStackTrace();
		}

		this.map = mapController.getViewableArea();

		motionController = new MotionController(mapController);

		setPreferredSize(new Dimension(map.getCols() * map.getTileSize().width,map.getRows() * map.getTileSize().height));

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
							try{
								while(!isInterrupted()){
									map = motionController.movePlayerUp();
									repaint();
									Thread.sleep(MotionController.MILLIS_BETWEEN_MOVES);
								}
							}catch(Exception e){
								Thread.currentThread().interrupt();
							}
						}
					};
					up.start();
					
					break;
				case KeyEvent.VK_A:
					
					left = new Thread(){
						public void run(){
							try{
								while(!isInterrupted()){
									map = motionController.movePlayerLeft();
									repaint();
									Thread.sleep(MotionController.MILLIS_BETWEEN_MOVES);
								}
							}catch(Exception e){
								Thread.currentThread().interrupt();
							}
						}
					};
					left.start();
					
					break;
				case KeyEvent.VK_S:
					
					down = new Thread(){
						public void run(){
							try{
								while(!isInterrupted()){
									map = motionController.movePlayerDown();
									repaint();
									Thread.sleep(MotionController.MILLIS_BETWEEN_MOVES);
								}
							}catch(Exception e){
								Thread.currentThread().interrupt();
							}
						}
					};
					down.start();
					
					break;
				case KeyEvent.VK_D:
					
					right = new Thread(){
						public void run(){
							try{
								while(!isInterrupted()){
									map = motionController.movePlayerRight();
									repaint();
									Thread.sleep(MotionController.MILLIS_BETWEEN_MOVES);
								}
							}catch(Exception e){
								Thread.currentThread().interrupt();
							}
						}
					};
					right.start();
					
					break;
				case KeyEvent.VK_UP:
					map = motionController.moveMapUp();
					repaint();
					break;
				case KeyEvent.VK_DOWN:
					map = motionController.moveMapDown();
					repaint();
					break;
				case KeyEvent.VK_LEFT:
					map = motionController.moveMapLeft();
					repaint();
					break;
				case KeyEvent.VK_RIGHT :
					map = motionController.moveMapRight();
					repaint();
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
	}

	@Override
	public void paint(Graphics g) {
		super.paintComponent(g);
		for(int y = 0; y < map.getRows(); y++){
			for(int x = 0; x < map.getCols(); x++){
				Tile curTile = map.tileAt(y, x);
				try {
					g.drawImage(curTile.getImage(),x * (int)map.getTileSize().getWidth(),y * (int)map.getTileSize().getHeight(), null);
				} catch (IOException e) {
					e.printStackTrace();
				} catch(NullPointerException ne){
					map.dump();
					ne.printStackTrace();
				}
			}
		}
		int playerX = (motionController.player.position.x * map.tileSize.width) + (map.tileSize.width / 2);
		int playerY = (motionController.player.position.y * map.tileSize.height) + (map.tileSize.height / 2);
		drawCenteredCircle(g, playerX, playerY, 10);
		motionController.player.dump();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {

		int units = e.getWheelRotation();

		if (e.isShiftDown()) {
			if(units > 0){
				map = motionController.moveMapRight();
			}else{
				map = motionController.moveMapLeft();
			}
		} else {
			if(units > 0){
				map = motionController.moveMapDown();
			}else{
				map = motionController.moveMapUp();
			}
		}
		repaint();
	}

	private void drawCenteredCircle(Graphics g, int x, int y, int r) {
		x = x-(r/2);
		y = y-(r/2);
		g.fillOval(x,y,r,r);
	}
}
