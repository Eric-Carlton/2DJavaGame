package models;

import java.awt.Point;

public class Player extends Person {
	
	public Player(){
		this(1,10,1, new Point(0,0));
	}
	
	public Player(Point pos){
		this(1,10,1, pos);
	}
	
	public Player(int strength, int hp, int defense, Point position){
		super(strength, hp, defense, position);
		loadImages();
	}
	
	public void dump(){
		System.out.printf("\nPlayer\n------\nX: %d\nY: %d\nStrength: %d\nHP: %d\nDefense: %d\nDirection: %s\n", position.x,position.y,strength,hp,defense,direction);
	}
	
	private void loadImages(){
		
	}
	
	
}
