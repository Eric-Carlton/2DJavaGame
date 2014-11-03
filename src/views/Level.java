package views;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JPanel;

import controllers.AutoMoveController;
import controllers.NPCMotionController;
import controllers.PlayerMotionController;
import controllers.ViewableMapController;
import models.BadDimensionException;
import models.Base;
import models.Direction;
import models.LoadedMap;
import models.Map;
import models.Player;
import models.Soldier;
import models.Tile;

public class Level extends JPanel {
	private static final long serialVersionUID = 1L;

	Map map;
	private Player player;
	ArrayList<Soldier> soldiers;
	ViewableMapController mapController;
	PlayerMotionController playerMotionController;
	NPCMotionController npcMotionController;
	public final Dimension tileSize = new Dimension(40,40);
	private Thread autoMove;

	public Level(File mapFile, Player player){
		super();
		
		this.player = player;
		try {
			long started = System.currentTimeMillis();
			mapController = new ViewableMapController(new LoadedMap(mapFile, tileSize), new Dimension(10,8), new Point(0,0));
			long done = System.currentTimeMillis();
			System.out.printf("Time to load map: %d millis\n\n", done-started);
		} catch (BadDimensionException e) {
			e.printStackTrace();
		}

		this.map = mapController.getViewableArea();

		playerMotionController = new PlayerMotionController(mapController, player);
		npcMotionController = new NPCMotionController(mapController.getMap());
		soldiers = new ArrayList<Soldier>();

		setPreferredSize(new Dimension(map.getCols() * map.getTileSize().width,map.getRows() * map.getTileSize().height));
		}

	@Override
	public void paintComponent(Graphics g) {
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
		int playerX = (player.position.x * map.tileSize.width);
		int playerY = (player.position.y * map.tileSize.height);
		g.drawImage(player.getCurrentImage(), playerX, playerY, null);
		
		for(Soldier s : soldiers){
			Point soldierViewableTile = getTileLocFromFullMapLoc(s.position);
			
			int soldierX = (soldierViewableTile.x * map.tileSize.width);
			int soldierY = (soldierViewableTile.y * map.tileSize.width);
			
			g.drawOval(soldierX, soldierY, map.tileSize.width, map.tileSize.width);
		}
	}
	
	public Point getTileLocInFullMap(Point tileLoc){
		return new Point(tileLoc.x + mapController.getPointInMapForTopLeftOfViewable().x, tileLoc.y + mapController.getPointInMapForTopLeftOfViewable().y);
	}
	
	public Point getTileLocFromFullMapLoc(Point fullMapLoc){
		return new Point(fullMapLoc.x - mapController.getPointInMapForTopLeftOfViewable().x, fullMapLoc.y - mapController.getPointInMapForTopLeftOfViewable().y);
	}

	public void movePlayerUp(){
		try{
			while(!Thread.currentThread().isInterrupted()){
				map = playerMotionController.movePlayerUp();
				Thread.sleep(playerMotionController.millisBetweenMoves());
			}
		}catch(Exception e){
			Thread.currentThread().interrupt();
		}
	}

	public void movePlayerLeft(){
		try{
			while(!Thread.currentThread().isInterrupted()){
				map = playerMotionController.movePlayerLeft();
				Thread.sleep(playerMotionController.millisBetweenMoves());
			}
		}catch(Exception e){
			Thread.currentThread().interrupt();
		}
	}

	public void movePlayerDown(){
		try{
			while(!Thread.currentThread().isInterrupted()){
				map = playerMotionController.movePlayerDown();
				Thread.sleep(playerMotionController.millisBetweenMoves());
			}
		}catch(Exception e){
			Thread.currentThread().interrupt();
		}
	}

	public void movePlayerRight(){
		try{
			while(!Thread.currentThread().isInterrupted()){
				map = playerMotionController.movePlayerRight();
				Thread.sleep(playerMotionController.millisBetweenMoves());
			}
		}catch(Exception e){
			Thread.currentThread().interrupt();
		}
	}

	public void moveMapUp(){
		map = playerMotionController.moveMapUp();
	}

	public void moveMapLeft(){
		map = playerMotionController.moveMapLeft();
	}

	public void moveMapDown(){
		map = playerMotionController.moveMapDown();
	}

	public void moveMapRight(){
		map = playerMotionController.moveMapRight();
	}
	
	public void interruptAllMovementThreads(){
		if(autoMove != null){
			autoMove.interrupt();
		}
	}
	
	public void updateModels(){
		for(int y = 0; y < mapController.getMap().getRows(); y++){
			for(int x = 0; x < mapController.getMap().getCols(); x++){
				Tile t = mapController.getMap().tileAt(y,x);
				t.hasSprite = false;
				if(t.isNPCAnchor && !t.isVisible){
					unloadNPCAnchoredAt(t);
				}
				if(t.isVisible){
					for(Base b : map.getBases()){
						if(b.isTileInBase(y, x)){
							if(!t.isNPCAnchor){
								loadNPCAnchoredAt(t);
							}
						}
					}
					for(Soldier s : soldiers){
						if(npcMotionController.timeToMove(s)){
							npcMotionController.moveSprite(s, s.getStep());
						}
						if(s.position.x == x && s.position.y == y){
							if(!t.hasSprite)
								t.hasSprite = true;
						}
					}
				}
			}
		}
	}
	
	public void render(){
		repaint();
	}
	
	public void attemptAutoMovePlayerToPoint(Point destTileLocRelativeToViewableScreen){
		AutoMoveController autoMover = new AutoMoveController(mapController.getMap());
		
		Point playerTile =  getTileLocInFullMap(player.position);
		Point destTile = getTileLocInFullMap(destTileLocRelativeToViewableScreen);
		
		LinkedList<Direction> path = autoMover.getSpritePathToTileLocation(playerTile,destTile);
		
		movePlayerAlongPath(path);
	}
	
	private void movePlayerAlongPath(final LinkedList<Direction> path){
		
		autoMove = new Thread(){
			@Override
			public void run(){
				try{
					while(!path.isEmpty()){
						Direction d = path.removeFirst();
						
						switch(d){
						case Up:
							map = playerMotionController.movePlayerUp();
							break;
						case Down:
							map = playerMotionController.movePlayerDown();
							break;
						case Left:
							map = playerMotionController.movePlayerLeft();
							break;
						case Right:
							map = playerMotionController.movePlayerRight();
							break;
						}
						
						Thread.sleep(playerMotionController.millisBetweenMoves());
					}
				}catch(Exception e){
					Thread.currentThread().interrupt();
				}
				
			}
		};
		
		autoMove.start();
	}
	
	private void loadNPCAnchoredAt(Tile t){
		
		LinkedList<Direction> leftPath = new LinkedList<Direction>();
		leftPath.add(Direction.Left);
		leftPath.add(Direction.Left);
		leftPath.add(Direction.Right);
		leftPath.add(Direction.Right);
		
		LinkedList<Direction> rightPath = new LinkedList<Direction>();
		rightPath.add(Direction.Right);
		rightPath.add(Direction.Right);
		rightPath.add(Direction.Left);
		rightPath.add(Direction.Left);
		
		switch(t.getType()){
		case CastleTopLeft:
			System.out.printf("\nLoading sprite anchored at (%d,%d)\n",t.getColInMap(), t.getRowInMap());
			Soldier tls = new Soldier(1,1,1,1, new Point(t.getColInMap(), t.getRowInMap() - 1), leftPath, t);
			soldiers.add(tls);
			t.isNPCAnchor = true;
			break;
		case CastleTopRight:
			System.out.printf("\nLoading sprite anchored at (%d,%d)\n",t.getColInMap(), t.getRowInMap());
			Soldier trs = new Soldier(1,1,1,1, new Point(t.getColInMap(), t.getRowInMap() - 1), rightPath, t);
			soldiers.add(trs);
			t.isNPCAnchor = true;
			break;
		case CastleBottomLeft:
			System.out.printf("\nLoading sprite anchored at (%d,%d)\n",t.getColInMap(), t.getRowInMap());
			Soldier bls = new Soldier(1,1,1,1, new Point(t.getColInMap(), t.getRowInMap() + 1), leftPath, t);
			soldiers.add(bls);
			t.isNPCAnchor = true;
			break;
		case CastleBottomRight:
			System.out.printf("\nLoading sprite anchored at (%d,%d)\n",t.getColInMap(), t.getRowInMap());
			Soldier brs = new Soldier(1,1,1,1, new Point(t.getColInMap(), t.getRowInMap() + 1), rightPath, t);
			soldiers.add(brs);
			t.isNPCAnchor = true;
			break;
		default:
			break;
		}
	}
	
	private void unloadNPCAnchoredAt(Tile t){
		for (Iterator<Soldier> iterator = soldiers.iterator(); iterator.hasNext();) {
			Soldier s = iterator.next();
			Tile anchor = s.getAnchor();
			if(anchor.equals(t)){
				System.out.printf("\nUnloading sprite anchored at (%d,%d)\n",t.getColInMap(), t.getRowInMap());
				iterator.remove();
				t.isNPCAnchor = false;
			}
		}
	}
}
