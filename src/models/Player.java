package models;

public class Player {
	public int basesOwned;
	public int experience;
	
	public Player(){
		this(0);
	}
	
	public Player(int basesOwned){
		this.basesOwned = basesOwned;
		experience = 0;
	}
}
