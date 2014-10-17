package controllers;

import models.Map;
import models.Player;
import models.Tile;
import models.TileType;
import models.Direction;

public class MotionController {

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
		this.player = new Player(1,10,1,10,mapCtrl.getCenterPoint());
		cameraMovesLeft = cameraMovesRight = cameraMovesUp = cameraMovesDown = 0;
		needsReCenter = false;
	}
	
	public long millisBetweenMoves(){
		if(200 - (10 * player.speed) >= 100)
			return 200 - (10 * player.speed);
		
		return 100;
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

		if(System.currentTimeMillis() - lastMoveTime >= millisBetweenMoves()){

			player.setDirection(Direction.Left);

			if(canMoveLeft()){
				
				lastMoveTime = System.currentTimeMillis();
				
				if((mapCtrl.isOnLeftBorder() && player.position.x - 1 >= 0) || (mapCtrl.isOnRightBorder() && mapCtrl.getCenterPoint().x < player.position.x))
					player.moveLeft(true);

				else
					return moveLeft();
			}
		}

		return map;

	}

	public Map movePlayerRight(){
		
		if(System.currentTimeMillis() - lastMoveTime >= millisBetweenMoves()){

			player.setDirection(Direction.Right);

			if(canMoveRight()){
				
				lastMoveTime = System.currentTimeMillis();

				if((mapCtrl.isOnRightBorder() &&  player.position.x + 1 <= 2 * mapCtrl.getDimensions().width) || (mapCtrl.isOnLeftBorder() && mapCtrl.getCenterPoint().x > player.position.x))
					player.moveRight(true);

				else
					return moveRight();
			}
		}

		return map;
	}

	public Map movePlayerDown(){

		if(System.currentTimeMillis() - lastMoveTime >= millisBetweenMoves()){

			player.setDirection(Direction.Down);

			if(canMoveDown()){
				
				lastMoveTime = System.currentTimeMillis();

				if((mapCtrl.isOnLowerBorder() && player.position.y + 1 <= 2 * mapCtrl.getDimensions().height) || (mapCtrl.isOnUpperBorder() && mapCtrl.getCenterPoint().y > player.position.y))
					player.moveDown(true);

				else
					return moveDown();
			}
		}

		return map;
	}

	public Map movePlayerUp(){
		if(System.currentTimeMillis() - lastMoveTime >= millisBetweenMoves()){

			player.setDirection(Direction.Up);

			if(canMoveUp()){

				lastMoveTime = System.currentTimeMillis();
				
				if((mapCtrl.isOnUpperBorder() && player.position.y - 1 >= 0) || (mapCtrl.isOnLowerBorder() && mapCtrl.getCenterPoint().y < player.position.y))
					player.moveUp(true);

				else
					return moveUp();
			}
		}

		return map;
	}

	public Map moveMapLeft(){
		if(!mapCtrl.isOnLeftBorder()){
			player.moveRight(false);
			cameraMovesLeft++;
			needsReCenter = true;
		}


		return moveLeft();
	}

	public Map moveMapRight(){
		if(!mapCtrl.isOnRightBorder()){
			player.moveLeft(false);
			cameraMovesRight++;
			needsReCenter = true;
		}


		return moveRight();
	}

	public Map moveMapUp(){
		if(!mapCtrl.isOnUpperBorder()){
			player.moveDown(false);
			cameraMovesUp++;
			needsReCenter = true;
		}


		return moveUp();
	}

	public Map moveMapDown(){
		if(!mapCtrl.isOnLowerBorder()){
			player.moveUp(false);
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
			player.moveLeft(false);
			cameraMovesLeft--;
		}
		while(cameraMovesRight > 0){
			moveLeft();
			player.moveRight(false);
			cameraMovesRight--;
		}
		while(cameraMovesUp > 0){
			moveDown();
			player.moveUp(false);
			cameraMovesUp--;
		}
		while(cameraMovesDown > 0){
			moveUp();
			player.moveDown(false);
			cameraMovesDown--;
		}
		needsReCenter = false;
	}
}
