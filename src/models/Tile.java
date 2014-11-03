package models;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Tile {
	
	private TileType type;
	private int rowInMap;
	private int colInMap;
	public boolean isNPCAnchor;
	public boolean isVisible;
	public boolean hasSprite;
	
	private BufferedImage image;
	
	public Tile(int rowInMap, int colInMap, TileType type, BufferedImage image){
		this.rowInMap = rowInMap;
		this.colInMap = colInMap;
		this.type = type;
		this.image = image;
		this.isNPCAnchor = false;
		this.isVisible = false;
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
	
	@Override
	public boolean equals(Object obj){
		       if (!(obj instanceof Tile))
		            return false;
		        if (obj == this)
		            return true;

		        Tile compare = (Tile)obj;
		        
		        if(this.getColInMap() == compare.getColInMap() && this.getRowInMap() == compare.getRowInMap())
		        	return true;
		        else return false;
	}
}
