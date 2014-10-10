package models;

import java.util.ArrayList;

public class Base {
	
	private Tile[] baseTiles;
	private ArrayList<Soldier> soldiers;
	private Player owner;
	
	public Base(Tile[] baseTiles, int initialSoldiers){
		this.baseTiles = baseTiles;
		soldiers = new ArrayList<Soldier>();
		owner = null;
	}
	
	public Tile[] getBaseTiles(){
		return baseTiles;
	}
	
	public ArrayList<Soldier> getSoldiers(){
		return soldiers;
	}
	
	public Player getOwner(){
		return owner;
	}
	
	public void setOwner(Player owner){
		this.owner = owner;
	}
	
	public boolean isTileInBase(int tileRow, int tileCol){
		for( Tile t : baseTiles){
			if( t.getColInMap() == tileCol && t.getRowInMap() == tileRow){
				return true;
			}
		}
		return false;
	}
	
	public void dump(){
		System.out.printf("Owner: %s\n", this.owner);
		System.out.printf("Number of Soldiers: %d\n", this.soldiers.size());
		for( Tile t : baseTiles){
			t.dump();
		}
	}
}
