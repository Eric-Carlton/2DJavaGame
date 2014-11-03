package controllers;

import models.Direction;
import models.Map;
import models.Sprite;
import models.Tile;
import models.TileType;

public class NPCMotionController {
	
	private Map fullMap;
	
	public NPCMotionController(Map fullMap){
		this.fullMap = fullMap;
	}
	
	public long millisBetweenMovesForSprite(Sprite toMove){
		if(200 - (10 * toMove.speed) >= 100)
			return 200 - (10 * toMove.speed);
		
		return 100;
	}
	
	public boolean timeToMove(Sprite sprite){
		return (System.currentTimeMillis() - sprite.lastMoveTime >= millisBetweenMovesForSprite(sprite));
	}
	
	public void moveSprite(Sprite sprite,Direction d){
		switch(d){
		case Up:
			moveSpriteUp(sprite);
			break;
		case Down:
			moveSpriteDown(sprite);
			break;
		case Left:
			moveSpriteLeft(sprite);
			break;
		case Right:
			moveSpriteRight(sprite);
			break;
		}
	}
	
	private boolean canSpriteMoveLeft(Sprite sprite){
		
		if(timeToMove(sprite)){
			Tile toTheLeft = fullMap.tileAt(sprite.position.y, sprite.position.x - 1);

			if(toTheLeft != null && toTheLeft.getType() == TileType.Grass)
				return true;
		}
		
		return false;
	}

	private boolean canSpriteMoveRight(Sprite sprite){
		if(timeToMove(sprite)){
			Tile toTheRight = fullMap.tileAt(sprite.position.y, sprite.position.x + 1);

			if(toTheRight != null && toTheRight.getType() == TileType.Grass)
				return true;
		}
		
		return false;
	}

	private boolean canSpriteMoveUp(Sprite sprite){
		if(timeToMove(sprite)){
			Tile above = fullMap.tileAt(sprite.position.y - 1, sprite.position.x);

			if(above != null && above.getType() == TileType.Grass)
				return true;
		}
		
		return false;
	}

	private boolean canSpriteMoveDown(Sprite sprite){
		if(timeToMove(sprite)){
			Tile below = fullMap.tileAt(sprite.position.y + 1, sprite.position.x);

			if(below != null && below.getType() == TileType.Grass)
				return true;
		}
		
		return false;
	}

	private void moveSpriteLeft(Sprite sprite){
		if(canSpriteMoveLeft(sprite)){
			sprite.lastMoveTime = System.currentTimeMillis();
			sprite.moveLeft(true);
		}
				
	}

	private void moveSpriteRight(Sprite sprite){
		if(canSpriteMoveRight(sprite)){
			sprite.lastMoveTime = System.currentTimeMillis();
			sprite.moveRight(true);
		}
			
	}

	private void moveSpriteDown(Sprite sprite){
		if(canSpriteMoveDown(sprite)){
			sprite.lastMoveTime = System.currentTimeMillis();
			sprite.moveDown(true);
		}
	}

	private void moveSpriteUp(Sprite sprite){
		if(canSpriteMoveUp(sprite)){
			sprite.lastMoveTime = System.currentTimeMillis();
			sprite.moveDown(true);
		}
	}

}
