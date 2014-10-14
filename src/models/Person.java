package models;

import java.awt.Point;
import java.awt.image.BufferedImage;

public class Person {
	
	public int strength;
	public int hp;
	public int defense;
	
	public Point position;
	
	protected BufferedImage lookingLeft;
	protected BufferedImage lookingRight;
	protected BufferedImage lookingForward;
	protected BufferedImage lookingBackward;
	
	protected BufferedImage attackingRight;
	protected BufferedImage attackingLeft;
	protected BufferedImage attackingForward;
	protected BufferedImage attackingBackward;
	
	protected Direction direction;
	
	BufferedImage currentImage;
	
	public Person(int strength, int hp, int defense, Point position){
		this.strength = strength;
		this.hp = hp;
		this.defense = defense;
		this.position = position;
		this.direction = Direction.Up;
	}
	
	public void moveLeft(){
			position.x--;
			if(direction != Direction.Left)
				setDirection(Direction.Left);
	}
	
	public void moveRight(){
		position.x++;
		if(direction != Direction.Right)
			setDirection(Direction.Right);
	}
	
	public void moveUp(){
		position.y--;
		if(direction != Direction.Up)
			setDirection(Direction.Up);
	}
	
	public void moveDown(){
		position.y++;
		if(direction != Direction.Down)
			setDirection(Direction.Down);
	}
	
	public void setDirection(Direction d){
		direction = d;
		
		switch(direction){
		case Up:
			currentImage = lookingForward;
			break;
		case Down:
			currentImage = lookingBackward;
			break;
		case Left:
			currentImage = lookingLeft;
			break;
		case Right:
			currentImage = lookingRight;
			break;
		}
	}
	
	public void attack(){
		switch(direction){
		case Up:
			currentImage = attackingForward;
			break;
		case Down:
			currentImage = attackingBackward;
			break;
		case Left:
			currentImage = attackingLeft;
			break;
		case Right:
			currentImage = attackingRight;
			break;
		}
	}
	
	public BufferedImage getCurrentImage(){
		return this.currentImage;
	}
	
	

}
