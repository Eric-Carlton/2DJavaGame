package models;

public class Base {
	
	private Tile[] baseTiles;
	private Player owner;
	
	public Base(Tile[] baseTiles){
		this.baseTiles = baseTiles;
		owner = null;
	}
	
	public Tile[] getBaseTiles(){
		return baseTiles;
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
		for( Tile t : baseTiles){
			t.dump();
		}
	}
}
