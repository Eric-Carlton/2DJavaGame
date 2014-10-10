package models;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;

public class Map {
	
	protected Tile[][] tiles;
	
	protected int rows;
	protected int cols;
	
	public Dimension tileSize;
	
	protected ArrayList<Base> bases;
	
	public Map(){
		cols = 0;
		rows = 0;
		bases = new ArrayList<Base>();
		tiles = null;
	}
	
	public Map(MutableMap mMap){
		this.cols = mMap.cols;
		this.rows = mMap.rows;
		this.tiles = mMap.tiles;
		this.tileSize = mMap.tileSize;
		this.bases = mMap.bases;
	}

	public int getRows(){
		return rows;
	}
	
	public int getCols(){
		return cols;
	}
	
	public Dimension getTileSize(){
		return tileSize;
	}
	
	public Tile tileAt(int y, int x){
		if(y > rows - 1 || x> cols -1 || y < 0 || x < 0){
			return null;
		}
		else return tiles[y][x];
	}
	
	public void addBase(Base toAdd){
		bases.add(toAdd);
	}
	
	public ArrayList<Base> getBases(){
		return bases;
	}
	
	public Base getBaseAtPoint(Point p){
		int row = (int)p.y / tileSize.height;
		int col = (int)p.x / tileSize.height;
		for(Base b : bases){
			if(b.isTileInBase(row, col)){
				return b;
			}
		}
		return null;
	}
	
	public void dump(){
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < cols; j++){
				System.out.printf("%s", tiles[i][j].getType());
			}
			System.out.println("");
		}
	}
	
	
}
