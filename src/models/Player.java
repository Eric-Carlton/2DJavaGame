package models;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Player extends Person {
	
	private BufferedImage spriteSheet;
	
	public Player(){
		this(new Point(0,0));
	}
	
	public Player(Point pos){
		this(1,10,1,1, pos);
	}
	
	public Player(int strength, int hp, int defense, int speed, Point position){
		super(strength, hp, defense, speed, position);
		loadImages();
		setDirection(Direction.Up);
	}
	
	public void dump(){
		System.out.printf("\nPlayer\n------\nX: %d\nY: %d\nStrength: %d\nHP: %d\nDefense: %d\nDirection: %s\n", position.x,position.y,strength,hp,defense,getDirection());
	}
	
	private void loadImages(){
		try {
			spriteSheet = ImageIO.read(new File("Assets/playerSpriteSheet.png"));
			extractSprites();
		} catch (IOException e) {
			e.printStackTrace();
			spriteSheet = null;
		}
	}

	private void extractSprites(){
		BufferedImage img = spriteSheet.getSubimage(48, 0, 48, 48);
		lookingBackward = new BufferedImage(40,40,BufferedImage.TYPE_INT_ARGB);
		scaleImage(img, lookingBackward);
		
		img = spriteSheet.getSubimage(48, 48, 48, 48);
		lookingForward = new BufferedImage(40,40,BufferedImage.TYPE_INT_ARGB);
		scaleImage(img, lookingForward);
		
		img = spriteSheet.getSubimage(48, 96, 48, 48);
		lookingLeft = new BufferedImage(40,40,BufferedImage.TYPE_INT_ARGB);
		scaleImage(img, lookingLeft);
		
		img = spriteSheet.getSubimage(48, 144, 48, 48);
		lookingRight = new BufferedImage(40,40,BufferedImage.TYPE_INT_ARGB);
		scaleImage(img, lookingRight);
	}
	
	private void scaleImage(BufferedImage toScale, BufferedImage scaled){
		Graphics2D gfx = scaled.createGraphics();
		gfx.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		gfx.drawImage(toScale, 0, 0, scaled.getWidth(), scaled.getHeight(), null);
		
		gfx.dispose();
	}
	
	
}
