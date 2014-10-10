package models;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Tile {
	
	private TileType type;
	private int rowInMap;
	private int colInMap;
	
	private BufferedImage image;
	
	public Tile(int rowInMap, int colInMap, TileType type, BufferedImage image){
		this.rowInMap = rowInMap;
		this.colInMap = colInMap;
		this.type = type;
		this.image = image;
	}
	
	public int getRowInMap(){
		return this.rowInMap;
	}
	
	public int getColInMap(){
		return this.colInMap;
	}
	
	public BufferedImage getImage() throws IOException{
		return this.image;
	}
	
	public TileType getType(){
		return this.type;
	}
	
	public void dump(){
		System.out.printf("\nRow: %d\nCol: %d\nType: %s\n", rowInMap, colInMap, type.toString());
	}
}
