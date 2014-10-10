package models;

import java.awt.Dimension;
import java.util.ArrayList;

public class MutableMap extends Map {
	
	private int addCol;
	private int addRow;
	
	public MutableMap(int cols, int rows, Dimension tileSize, ArrayList<Base> bases){
		this.cols = cols;
		this.rows = rows;
		this.tiles = new Tile[rows][cols];
		this.tileSize = tileSize;
		this.bases = bases;
		addCol = 0;
		addRow = 0;
	}
	
	public boolean addTile(Tile toAdd){
		if(addRow > rows - 1 ){
			return false;
		}
		
		tiles[addRow][addCol] = toAdd;
		
		if(addCol > cols - 2){
			addCol = 0;
			addRow++;
		}
		else{
			addCol++;
		}
		
		return true;
	}
}
