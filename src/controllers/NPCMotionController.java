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
	
	public long nanosBetweenMovesForSprite(Sprite toMove){
		if(200000000L - (10000000L * toMove.speed) >= 10000000L){
			return 200000000L - (10000000L * toMove.speed);
		}
			
		return 10000000L;
	}
	
	public boolean timeToMove(Sprite sprite){
		return (System.nanoTime() - sprite.lastMoveTime >= nanosBetweenMovesForSprite(sprite));
	}
	
	public void moveSpriteDirection(Sprite sprite,Direction d){
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
			if(toTheLeft != null && toTheLeft.getType() == TileType.Grass && !toTheLeft.hasSprite)
				return true;
		}
		
		return false;
	}

	private boolean canSpriteMoveRight(Sprite sprite){
		if(timeToMove(sprite)){
			Tile toTheRight = fullMap.tileAt(sprite.position.y, sprite.position.x + 1);
			if(toTheRight != null && toTheRight.getType() == TileType.Grass && !toTheRight.hasSprite)
				return true;
		}
		
		return false;
	}

	private boolean canSpriteMoveUp(Sprite sprite){
		if(timeToMove(sprite)){
			Tile above = fullMap.tileAt(sprite.position.y - 1, sprite.position.x);
			if(above != null && above.getType() == TileType.Grass && !above.hasSprite)
				return true;
		}
		
		return false;
	}

	private boolean canSpriteMoveDown(Sprite sprite){
		if(timeToMove(sprite)){
			Tile below = fullMap.tileAt(sprite.position.y + 1, sprite.position.x);
			if(below != null && below.getType() == TileType.Grass && !below.hasSprite)
				return true;
		}
		
		return false;
	}

	private void moveSpriteLeft(Sprite sprite){
		if(canSpriteMoveLeft(sprite)){
			sprite.moveLeft(true);
			sprite.lastMoveTime = System.nanoTime();
		}
				
	}

	private void moveSpriteRight(Sprite sprite){
		if(canSpriteMoveRight(sprite)){
			sprite.moveRight(true);
			sprite.lastMoveTime = System.nanoTime();
		}
			
	}

	private void moveSpriteDown(Sprite sprite){
		if(canSpriteMoveDown(sprite)){
			sprite.moveDown(true);
			sprite.lastMoveTime = System.nanoTime();
		}
	}

	private void moveSpriteUp(Sprite sprite){
		if(canSpriteMoveUp(sprite)){
			sprite.moveDown(true);
			sprite.lastMoveTime = System.nanoTime();
		}
	}

}
