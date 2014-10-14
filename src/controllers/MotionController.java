package controllers;

import models.Map;
import models.Player;
import models.Tile;
import models.TileType;
import models.Direction;

public class MotionController {
	
	public static final int MILLIS_BETWEEN_MOVES = 100;

	private ViewableMapController mapCtrl;
	public Player player;
	private Map map;

	private int cameraMovesLeft;
	private int cameraMovesRight;
	private int cameraMovesUp;
	private int cameraMovesDown;

	private boolean needsReCenter;

	private long lastMoveTime;

	public MotionController(ViewableMapController mapCtrl){
		lastMoveTime = System.currentTimeMillis();
		this.mapCtrl = mapCtrl;
		this.map = mapCtrl.getViewableArea();
		this.player = new Player(mapCtrl.getCenterPoint());
		cameraMovesLeft = cameraMovesRight = cameraMovesUp = cameraMovesDown = 0;
		needsReCenter = false;
	}
	
	public boolean canMoveLeft(){
		
		if(needsReCenter)
			reCenterCamera();
		
		Tile toTheLeft = map.tileAt(player.position.y, player.position.x - 1);

		if(toTheLeft != null && toTheLeft.getType() == TileType.Grass){
			return true;
		}
		
		return false;
	}
	
	public boolean canMoveRight(){
		
		if(needsReCenter)
			reCenterCamera();
		
		Tile toTheRight = map.tileAt(player.position.y, player.position.x + 1);

		if(toTheRight != null && toTheRight.getType() == TileType.Grass){
			return true;
		}
		
		return false;
	}
	
	public boolean canMoveUp(){
		
		if(needsReCenter)
			reCenterCamera();
		
		Tile above = map.tileAt(player.position.y - 1, player.position.x);

		if(above != null && above.getType() == TileType.Grass){
			return true;
		}
		
		return false;
	}
	
	public boolean canMoveDown(){
		
		if(needsReCenter)
			reCenterCamera();
		
		Tile below = map.tileAt(player.position.y + 1, player.position.x);

		if(below != null && below.getType() == TileType.Grass){
			return true;
		}
		
		return false;
	}

	public Map movePlayerLeft(){

		if(System.currentTimeMillis() - lastMoveTime >= MILLIS_BETWEEN_MOVES){

			player.setDirection(Direction.Left);

			if(canMoveLeft()){
				
				lastMoveTime = System.currentTimeMillis();
				
				if((mapCtrl.isOnLeftBorder() && player.position.x - 1 >= 0) || (mapCtrl.isOnRightBorder() && mapCtrl.getCenterPoint().x < player.position.x))
					player.moveLeft();

				else if(player.position.x == mapCtrl.getCenterPoint().x)
					return moveLeft();
			}
		}

		return map;

	}

	public Map movePlayerRight(){
		
		if(System.currentTimeMillis() - lastMoveTime >= MILLIS_BETWEEN_MOVES){

			player.setDirection(Direction.Right);

			if(canMoveRight()){
				
				lastMoveTime = System.currentTimeMillis();

				if((mapCtrl.isOnRightBorder() &&  player.position.x + 1 <= 2 * mapCtrl.getDimensions().width) || (mapCtrl.isOnLeftBorder() && mapCtrl.getCenterPoint().x > player.position.x))
					player.moveRight();

				else if(player.position.x == mapCtrl.getCenterPoint().x)
					return moveRight();
			}
		}

		return map;
	}

	public Map movePlayerDown(){

		if(System.currentTimeMillis() - lastMoveTime >= MILLIS_BETWEEN_MOVES){

			player.setDirection(Direction.Down);

			if(canMoveDown()){
				
				lastMoveTime = System.currentTimeMillis();

				if((mapCtrl.isOnLowerBorder() && player.position.y + 1 <= 2 * mapCtrl.getDimensions().height) || (mapCtrl.isOnUpperBorder() && mapCtrl.getCenterPoint().y > player.position.y))
					player.moveDown();

				else if(player.position.y == mapCtrl.getCenterPoint().y)
					return moveDown();
			}
		}

		return map;
	}

	public Map movePlayerUp(){
		if(System.currentTimeMillis() - lastMoveTime >= MILLIS_BETWEEN_MOVES){

			player.setDirection(Direction.Up);

			if(canMoveUp()){

				lastMoveTime = System.currentTimeMillis();
				
				if((mapCtrl.isOnUpperBorder() && player.position.y - 1 >= 0) || (mapCtrl.isOnLowerBorder() && mapCtrl.getCenterPoint().y < player.position.y))
					player.moveUp();

				else if(player.position.y == mapCtrl.getCenterPoint().y)
					return moveUp();
			}
		}

		return map;
	}

	public Map moveMapLeft(){
		if(!mapCtrl.isOnLeftBorder()){
			player.moveRight();
			cameraMovesLeft++;
			needsReCenter = true;
		}


		return moveLeft();
	}

	public Map moveMapRight(){
		if(!mapCtrl.isOnRightBorder()){
			player.moveLeft();
			cameraMovesRight++;
			needsReCenter = true;
		}


		return moveRight();
	}

	public Map moveMapUp(){
		if(!mapCtrl.isOnUpperBorder()){
			player.moveDown();
			cameraMovesUp++;
			needsReCenter = true;
		}


		return moveUp();
	}

	public Map moveMapDown(){
		if(!mapCtrl.isOnLowerBorder()){
			player.moveUp();
			cameraMovesDown++;
			needsReCenter = true;
		}

		return moveDown();
	}
	
	private Map moveLeft(){
		mapCtrl.moveLeft(1);
		map = mapCtrl.getViewableArea();
		return map;
	}

	private Map moveRight(){
		mapCtrl.moveRight(1);
		map = mapCtrl.getViewableArea();
		return map;
	}

	private Map moveUp(){
		mapCtrl.moveUp(1);
		map = mapCtrl.getViewableArea();
		return map;
	}

	private Map moveDown(){
		mapCtrl.moveDown(1);
		map = mapCtrl.getViewableArea();
		return map;
	}

	public void reCenterCamera(){
		while(cameraMovesLeft > 0){
			moveRight();
			player.moveLeft();
			cameraMovesLeft--;
		}
		while(cameraMovesRight > 0){
			moveLeft();
			player.moveRight();
			cameraMovesRight--;
		}
		while(cameraMovesUp > 0){
			moveDown();
			player.moveUp();
			cameraMovesUp--;
		}
		while(cameraMovesDown > 0){
			moveUp();
			player.moveDown();
			cameraMovesDown--;
		}
		needsReCenter = false;
	}
}
