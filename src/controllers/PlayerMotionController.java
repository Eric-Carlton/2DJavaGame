package controllers;

import models.Direction;
import models.Map;
import models.Player;
import models.Tile;
import models.TileType;

public class PlayerMotionController {
	private ViewableMapController mapCtrl;
	public Player player;
	private Map viewableMap;

	private int cameraMovesLeft;
	private int cameraMovesRight;
	private int cameraMovesUp;
	private int cameraMovesDown;

	private boolean needsReCenter;

	public PlayerMotionController(ViewableMapController mapCtrl, Player player){
		player.lastMoveTime = System.currentTimeMillis();
		this.mapCtrl = mapCtrl;
		this.viewableMap = mapCtrl.getViewableArea();
		this.player = player;
		player.setPosition(mapCtrl.getCenterPoint());
		cameraMovesLeft = cameraMovesRight = cameraMovesUp = cameraMovesDown = 0;
		needsReCenter = false;
	}

	public long millisBetweenMoves(){
		if(200 - (10 * player.speed) >= 100)
			return 200 - (10 * player.speed);

		return 100;
	}

	public boolean canMoveLeft(){

		if(System.currentTimeMillis() - player.lastMoveTime >= millisBetweenMoves()){
			if(needsReCenter)
				reCenterCamera();

			Tile toTheLeft = viewableMap.tileAt(player.position.y, player.position.x - 1);

			if(toTheLeft != null && toTheLeft.getType() == TileType.Grass && !toTheLeft.hasSprite){
				return true;
			}
		}

		return false;
	}

	public boolean canMoveRight(){

		if(System.currentTimeMillis() - player.lastMoveTime >= millisBetweenMoves()){
			if(needsReCenter)
				reCenterCamera();

			Tile toTheRight = viewableMap.tileAt(player.position.y, player.position.x + 1);

			if(toTheRight != null && toTheRight.getType() == TileType.Grass && !toTheRight.hasSprite){
				return true;
			}
		}

		return false;
	}

	public boolean canMoveUp(){

		if(System.currentTimeMillis() - player.lastMoveTime >= millisBetweenMoves()){
			if(needsReCenter)
				reCenterCamera();

			Tile above = viewableMap.tileAt(player.position.y - 1, player.position.x);

			if(above != null && above.getType() == TileType.Grass && !above.hasSprite){
				return true;
			}
		}



		return false;
	}

	public boolean canMoveDown(){

		if(System.currentTimeMillis() - player.lastMoveTime >= millisBetweenMoves()){

			if(needsReCenter)
				reCenterCamera();

			Tile below = viewableMap.tileAt(player.position.y + 1, player.position.x);

			if(below != null && below.getType() == TileType.Grass && !below.hasSprite){
				return true;
			}
		}

		return false;
	}

	public Map movePlayerLeft(){

		player.setDirection(Direction.Left);
		
		if(canMoveLeft()){
			
			player.lastMoveTime = System.currentTimeMillis();

			if((mapCtrl.isOnLeftBorder() && player.position.x - 1 >= 0) || (mapCtrl.isOnRightBorder() && mapCtrl.getCenterPoint().x < player.position.x)){
				player.moveLeft(true);
			}
			else{
				return moveLeft();
			}
				
		}

		return viewableMap;
	}

	public Map movePlayerRight(){
		
		player.setDirection(Direction.Right);

		if(canMoveRight()){

			player.lastMoveTime = System.currentTimeMillis();

			if((mapCtrl.isOnRightBorder() &&  player.position.x + 1 <= 2 * mapCtrl.getDimensions().width) || (mapCtrl.isOnLeftBorder() && mapCtrl.getCenterPoint().x > player.position.x))
				player.moveRight(true);

			else{
				return moveRight();
			}
				
		}
		
		return viewableMap;
	}

	public Map movePlayerDown(){
		
		player.setDirection(Direction.Down);

		if(canMoveDown()){

			player.lastMoveTime = System.currentTimeMillis();

			if((mapCtrl.isOnLowerBorder() && player.position.y + 1 <= 2 * mapCtrl.getDimensions().height) || (mapCtrl.isOnUpperBorder() && mapCtrl.getCenterPoint().y > player.position.y))
				player.moveDown(true);

			else{
				return moveDown();
			}
		}
		
		return viewableMap;
	}

	public Map movePlayerUp(){
		
		player.setDirection(Direction.Up);

		if(canMoveUp()){

			player.lastMoveTime = System.currentTimeMillis();

			if((mapCtrl.isOnUpperBorder() && player.position.y - 1 >= 0) || (mapCtrl.isOnLowerBorder() && mapCtrl.getCenterPoint().y < player.position.y))
				player.moveUp(true);

			else{
				return moveUp();
			}
				
		}
		
		return viewableMap;
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
		viewableMap = mapCtrl.getViewableArea();
		return viewableMap;
	}

	private Map moveRight(){
		mapCtrl.moveRight(1);
		viewableMap = mapCtrl.getViewableArea();
		return viewableMap;
	}

	private Map moveUp(){
		mapCtrl.moveUp(1);
		viewableMap = mapCtrl.getViewableArea();
		return viewableMap;
	}

	private Map moveDown(){
		mapCtrl.moveDown(1);
		viewableMap = mapCtrl.getViewableArea();
		return viewableMap;
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
