package controllers;

import java.awt.Point;
import java.util.LinkedList;

import models.Direction;
import models.Map;
import models.Tile;
import models.TileType;

public class AutoMoveController {
	private boolean[][] beenToTile;
	private LinkedList<Direction> path;
	private Map map;

	public AutoMoveController(Map map){
		this.map = map;
		beenToTile = new boolean[map.getRows()][map.getCols()];			
		path = new LinkedList<Direction>();
	}
	
	public LinkedList<Direction> getSpritePathToTileLocation(Point spriteLoc, Point dest){
		
		for(int y = 0; y < map.getRows(); y++)
			for(int x = 0; x < map.getCols(); x++)
				beenToTile[y][x] = false;
		
		//player clicked on a tile that he/she cannot move to
		int border = 0;
		while(map.tileAt(dest.y, dest.x) == null || map.tileAt(dest.y, dest.x).getType() != TileType.Grass){
			border++;
			
			//look at surrounding tiles until one can be found to move to
			if(map.tileAt(dest.y + border, dest.x) != null && map.tileAt(dest.y + border, dest.x).getType() == TileType.Grass)
				dest = new Point(dest.x, dest.y + border);
			else if((map.tileAt(dest.y, dest.x - border) != null && map.tileAt(dest.y, dest.x - border).getType() == TileType.Grass))
				dest = new Point(dest.x - border, dest.y);
			else if((map.tileAt(dest.y - border, dest.x) != null && map.tileAt(dest.y - border, dest.x).getType() == TileType.Grass))
				dest = new Point(dest.x, dest.y - border);
			else if((map.tileAt(dest.y, dest.x + border) != null && map.tileAt(dest.y, dest.x + border).getType() == TileType.Grass))
				dest = new Point(dest.x + border, dest.y);
		}
		
		getPathFromTileToTile(spriteLoc, dest);
		
		return path;
	}

	private void getPathFromTileToTile(Point origin, Point dest){
		
		System.out.printf("Finding a path from (%d,%d) to (%d,%d)...\n", origin.x, origin.y, dest.x, dest.y);
		
		long start = System.nanoTime();
		beenToTile[origin.y][origin.x] = true; 
		//haven't reach destination
		while(origin.x != dest.x || origin.y != dest.y){

			//need to move up
			while(origin.y > dest.y){

				//possible to move up, but haven't reached the right row
				while(origin.y > dest.y && possibleToMoveUpFromTileLocation(origin)){
					origin = new Point(origin.x, origin.y - 1);
					path.add(Direction.Up);
					beenToTile[origin.y][origin.x] = true;
				}

				//moving straight up worked, in the right row
				if(origin.y == dest.y){
					break;
				}
				
				//moving straight up didn't work, try to step around obstruction by going right
				LinkedList<Direction> horizontalPath = new LinkedList<Direction>();
				Point horizontalPoint = new Point(origin.x, origin.y);
				while(!possibleToMoveUpFromTileLocation(horizontalPoint) && possibleToMoveRightFromTileLocation(horizontalPoint)){
					horizontalPoint = new Point(horizontalPoint.x + 1, horizontalPoint.y);
					horizontalPath.add(Direction.Right);
					beenToTile[horizontalPoint.y][horizontalPoint.x] = true;
				}
				
				//can try moving up some more
				if(possibleToMoveUpFromTileLocation(horizontalPoint)){
					origin = horizontalPoint;
					while(!horizontalPath.isEmpty()){
						Direction d = horizontalPath.remove();
						path.add(d);
					}
				}
				
				//couldn't find a path up moving right, try going left
				horizontalPath = new LinkedList<Direction>();
				horizontalPoint = new Point(origin.x, origin.y);
				while(!possibleToMoveUpFromTileLocation(horizontalPoint) && possibleToMoveLeftFromTileLocation(horizontalPoint)){
					horizontalPoint = new Point(horizontalPoint.x - 1, horizontalPoint.y);
					horizontalPath.add(Direction.Left);
					beenToTile[horizontalPoint.y][horizontalPoint.x] = true;
				}
				
				//can try moving up some more
				if(possibleToMoveUpFromTileLocation(horizontalPoint)){
					origin = horizontalPoint;
					while(!horizontalPath.isEmpty()){
						Direction d = horizontalPath.remove();
						path.add(d);
					}
				}
				
				//can't make it to destination
				else return;
			}
			
			//need to move Right
			while(origin.x < dest.x){

				//possible to move right, but haven't reached the right column
				while(origin.x < dest.x && possibleToMoveRightFromTileLocation(origin)){
					origin = new Point(origin.x + 1, origin.y);
					path.add(Direction.Right);
					beenToTile[origin.y][origin.x] = true;
				}

				//moving straight right worked, in the right column
				if(origin.x == dest.x){
					break;
				}
				
				//moving straight right didn't work, try to step around obstruction by going up
				LinkedList<Direction> verticalPath = new LinkedList<Direction>();
				Point verticalPoint = new Point(origin.x, origin.y);
				while(!possibleToMoveRightFromTileLocation(verticalPoint) && possibleToMoveUpFromTileLocation(verticalPoint)){
					verticalPoint = new Point(verticalPoint.x, verticalPoint.y - 1);
					verticalPath.add(Direction.Up);
					beenToTile[verticalPoint.y][verticalPoint.x] = true;
				}
				
				//can try moving right some more
				if(possibleToMoveRightFromTileLocation(verticalPoint)){
					origin = verticalPoint;
					while(!verticalPath.isEmpty()){
						Direction d = verticalPath.remove();
						path.add(d);
					}
				}
				
				//couldn't find a path up moving up, try going down
				verticalPath = new LinkedList<Direction>();
				verticalPoint = new Point(origin.x, origin.y);
				while(!possibleToMoveRightFromTileLocation(verticalPoint) && possibleToMoveDownFromTileLocation(verticalPoint)){
					verticalPoint = new Point(verticalPoint.x, verticalPoint.y + 1);
					verticalPath.add(Direction.Down);
					beenToTile[verticalPoint.y][verticalPoint.x] = true;
				}
				
				//can try moving right some more
				if(possibleToMoveRightFromTileLocation(verticalPoint)){
					origin = verticalPoint;
					while(!verticalPath.isEmpty()){
						Direction d = verticalPath.remove();
						path.add(d);
					}
				}
				
				//can't make it to destination
				else return;
			}
			
			//need to move down
			while(origin.y < dest.y){

				//possible to move down, but haven't reached the right row
				while(origin.y < dest.y && possibleToMoveDownFromTileLocation(origin)){
					origin = new Point(origin.x, origin.y + 1);
					path.add(Direction.Down);
					beenToTile[origin.y][origin.x] = true;
				}

				//moving straight down worked, in the right row
				if(origin.y == dest.y){
					break;
				}
				
				//moving straight up didn't work, try to step around obstruction by going right
				LinkedList<Direction> horizontalPath = new LinkedList<Direction>();
				Point horizontalPoint = new Point(origin.x, origin.y);
				while(!possibleToMoveDownFromTileLocation(horizontalPoint) && possibleToMoveRightFromTileLocation(horizontalPoint)){
					horizontalPoint = new Point(horizontalPoint.x + 1, horizontalPoint.y);
					horizontalPath.add(Direction.Right);
					beenToTile[horizontalPoint.y][horizontalPoint.x] = true;
				}
				
				//can try moving down some more
				if(possibleToMoveDownFromTileLocation(horizontalPoint)){
					origin = horizontalPoint;
					while(!horizontalPath.isEmpty()){
						Direction d = horizontalPath.remove();
						path.add(d);
					}
				}
				
				//couldn't find a path up moving right, try going left
				horizontalPath = new LinkedList<Direction>();
				horizontalPoint = new Point(origin.x, origin.y);
				while(!possibleToMoveDownFromTileLocation(horizontalPoint) && possibleToMoveLeftFromTileLocation(horizontalPoint)){
					horizontalPoint = new Point(horizontalPoint.x - 1, horizontalPoint.y);
					horizontalPath.add(Direction.Left);
					beenToTile[horizontalPoint.y][horizontalPoint.x] = true;
				}
				
				//can try moving down some more
				if(possibleToMoveDownFromTileLocation(horizontalPoint)){
					origin = horizontalPoint;
					while(!horizontalPath.isEmpty()){
						Direction d = horizontalPath.remove();
						path.add(d);
					}
				}
				
				//can't make it to destination
				else return;
			}
			
			//need to move left
			while(origin.x > dest.x){

				//possible to move left, but haven't reached the right column
				while(origin.x > dest.x && possibleToMoveLeftFromTileLocation(origin)){
					origin = new Point(origin.x - 1, origin.y);
					path.add(Direction.Left);
					beenToTile[origin.y][origin.x] = true;
				}

				//moving straight right worked, in the right column
				if(origin.x == dest.x){
					break;
				}
				
				//moving straight left didn't work, try to step around obstruction by going up
				LinkedList<Direction> verticalPath = new LinkedList<Direction>();
				Point verticalPoint = new Point(origin.x, origin.y);
				while(!possibleToMoveLeftFromTileLocation(verticalPoint) && possibleToMoveUpFromTileLocation(verticalPoint)){
					verticalPoint = new Point(verticalPoint.x, verticalPoint.y - 1);
					verticalPath.add(Direction.Up);
					beenToTile[verticalPoint.y][verticalPoint.x] = true;
				}
				
				//can try moving left some more
				if(possibleToMoveLeftFromTileLocation(verticalPoint)){
					origin = verticalPoint;
					while(!verticalPath.isEmpty()){
						Direction d = verticalPath.remove();
						path.add(d);
					}
				}
				
				//couldn't find a path up moving up, try going down
				verticalPath = new LinkedList<Direction>();
				verticalPoint = new Point(origin.x, origin.y);
				while(!possibleToMoveLeftFromTileLocation(verticalPoint) && possibleToMoveDownFromTileLocation(verticalPoint)){
					verticalPoint = new Point(verticalPoint.x, verticalPoint.y + 1);
					verticalPath.add(Direction.Down);
					beenToTile[verticalPoint.y][verticalPoint.x] = true;
				}
				
				//can try moving right some more
				if(possibleToMoveLeftFromTileLocation(verticalPoint)){
					origin = verticalPoint;
					while(!verticalPath.isEmpty()){
						Direction d = verticalPath.remove();
						path.add(d);
					}
				}
				
				//can't make it to destination
				else return;
			}
		}
		long end = System.nanoTime();
		
		System.out.printf("Time to find path: %d ns ( %.3f ms)\n\n", end - start, ((double)end - (double)start) / 1000000.0);
	}

	private boolean possibleToMoveUpFromTileLocation(Point p){

		Tile curTile = map.tileAt(p.y - 1, p.x);
		if(curTile != null && curTile.getType() == TileType.Grass && !beenToTile[p.y - 1][p.x])
			return true;

		return false;
	}
	
	private boolean possibleToMoveDownFromTileLocation(Point p){

		Tile curTile = map.tileAt(p.y + 1, p.x);
		if(curTile != null && curTile.getType() == TileType.Grass && !beenToTile[p.y + 1][p.x])
			return true;

		return false;
	}

	private boolean possibleToMoveRightFromTileLocation(Point p){

		Tile curTile = map.tileAt(p.y, p.x + 1);
		if(curTile != null && curTile.getType() == TileType.Grass && !beenToTile[p.y][p.x + 1])
			return true;

		return false;
	}
	
	private boolean possibleToMoveLeftFromTileLocation(Point p){

		Tile curTile = map.tileAt(p.y, p.x - 1);
		if(curTile != null && curTile.getType() == TileType.Grass && !beenToTile[p.y][p.x - 1])
			return true;

		return false;
	}
}
