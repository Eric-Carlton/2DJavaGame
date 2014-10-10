package views;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;

import controllers.MotionController;
import controllers.ViewableMapController;
import models.BadDimensionException;
import models.Base;
import models.LoadedMap;
import models.Map;
import models.Tile;

public class Level extends JPanel implements MouseListener, MouseWheelListener {
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
		motionController = new MotionController(mapController);

		this.map = mapController.getViewableArea();
		setPreferredSize(new Dimension(map.getCols() * map.getTileSize().width,map.getRows() * map.getTileSize().height));

		addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();
				switch( keyCode ) { 
				case KeyEvent.VK_UP:
					map = motionController.moveUp();
					repaint();
					break;
				case KeyEvent.VK_DOWN:
					map = motionController.moveDown();
					repaint();
					break;
				case KeyEvent.VK_LEFT:
					map = motionController.moveLeft();
					repaint();
					break;
				case KeyEvent.VK_RIGHT :
					map = motionController.moveRight();
					repaint();
					break;
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {

			}
		});

		addMouseListener(this);
		addMouseWheelListener(this);
	}

	@Override
	protected void paintComponent(Graphics g) {
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
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		
		System.out.println("Point: " + e.getPoint());
		
		Point pointInFullMap = new Point(e.getPoint().x + (int)(mapController.getPointInMapForTopLeftOfViewable().x * map.tileSize.width),
				e.getPoint().y + (int)(mapController.getPointInMapForTopLeftOfViewable().y * map.tileSize.height));
		
		System.out.println("Point In Full Map: " + pointInFullMap);
		
		Base b = map.getBaseAtPoint(pointInFullMap);
		
		if(b != null){
			System.out.println("\nGot a base!\n");
			b.dump();
		}
		else{
			System.out.println("\nNo base here!\n");
		}
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

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		
		int units = e.getWheelRotation();
		
		if (e.isShiftDown()) {
            if(units > 0){
            	map = motionController.moveRight();
            }else{
            	map = motionController.moveLeft();
            }
        } else {
            if(units > 0){
            	map = motionController.moveDown();
            }else{
            	map = motionController.moveUp();
            }
        }
		repaint();
	}
	

}
