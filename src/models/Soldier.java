package models;

import java.awt.Point;

public class Soldier {
	int strength;
	int speed;
	int size;
	Point pos;
	
	public Soldier(Point pos){
		this.pos = pos;
		this.strength = 1;
		this.speed = 1;
		this.size = 10;
	}
}
