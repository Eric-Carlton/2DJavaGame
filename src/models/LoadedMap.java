package models;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

public class LoadedMap extends Map {

	private static final String grassImageLoc = "Assets/short_grass.jpeg";
	private static final String castleTopLeftImageLoc = "Assets/castle_top_left.jpeg";
	private static final String castleTopRightImageLoc = "Assets/castle_top_right.jpeg";
	private static final String castleBottomLeftImageLoc = "Assets/castle_bottom_left.jpeg";
	private static final String castleBottomRightImageLoc = "Assets/castle_bottom_right.jpeg";
	private static final String rockImageLoc = "Assets/rock.jpeg";
	

	private static BufferedImage grassImage;
	private static BufferedImage castleTopLeftImage;
	private static BufferedImage castleTopRightImage;
	private static BufferedImage castleBottomLeftImage;
	private static BufferedImage castleBottomRightImage;
	private static BufferedImage rockImage;
	

	public LoadedMap(File mapFile, Dimension tileSize){
		Dimension dim = getLevelDimensions(mapFile);
		this.cols = dim.width;
		this.rows = dim.height;
		this.tileSize = tileSize;
		tiles = new Tile[rows][cols];
		loadImages();
		getTiles(mapFile);
	}

	private Dimension getLevelDimensions(File file){
		int x = 0;
		int y = 1;

		try{
			BufferedReader rdr = createBufferedReaderForFile(file);

			long started = System.currentTimeMillis();
			int cur;
			while((cur = rdr.read()) != -1){

				if((char)cur != '\n'){
					x++;
				}else{
					y++;
				}
			}
			long done = System.currentTimeMillis();
			System.out.printf("Time to count dimensions: %d millis\n", done - started);
		}catch(IOException e){
			e.printStackTrace();
		}

		cols = x;
		rows = y;
		return new Dimension((cols/rows),rows);
	}

	private BufferedReader createBufferedReaderForFile(File file) throws IOException{
		FileReader fileRdr = new FileReader(file);
		BufferedReader rdr = new BufferedReader(fileRdr);

		return rdr;
	}

	private void getTiles(File file){

		int x = 0;
		int y = 0;

		try{
			BufferedReader rdr = createBufferedReaderForFile(file);

			int cur;
			long started = System.currentTimeMillis();
			while((cur = rdr.read()) != -1){

				if((char)cur != '\n'){
					tiles[y][x] = createTileWithTypeAndMapPos((char)cur, y, x);
					x++;
				}else{
					y++;
					x=0;
				}
			}
			long done = System.currentTimeMillis();
			System.out.printf("Time to load all tiles: %d millis\n", done - started);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	private Tile createTileWithTypeAndMapPos(char type, int y, int x){
		Tile tile;
		switch(type){
		case '1':
			tile = new Tile(y, x, TileType.CastleTopLeft, getImageForType(TileType.CastleTopLeft));
			break;
		case '2':
			tile = new Tile(y, x, TileType.CastleTopRight, getImageForType(TileType.CastleTopRight));
			break;
		case '3':
			tile = new Tile(y, x, TileType.CastleBottomLeft, getImageForType(TileType.CastleBottomLeft));
			break;
		case '4':
			tile = new Tile(y, x, TileType.CastleBottomRight, getImageForType(TileType.CastleBottomRight));
			
			Tile[] baseTiles = new Tile[4];
			baseTiles[0] = tiles[y-1][x-1];
			baseTiles[1] = tiles[y-1][x];
			baseTiles[2] = tiles[y][x-1];
			baseTiles[3] = tile;
			
			Base b = new Base(baseTiles, 0);
			
			addBase(b);
			break;
		case '5':
			tile = new Tile(tileSize.width, tileSize.height, TileType.Rock, getImageForType(TileType.Rock));
			break;
		default:
			tile = new Tile(tileSize.width, tileSize.height, TileType.Grass, getImageForType(TileType.Grass));
			break;
		}

		return tile;
	}
	
	private BufferedImage getImageForType(TileType type){
		switch(type){
		case CastleTopLeft:
			return castleTopLeftImage;
		case CastleTopRight:
			return castleTopRightImage;
		case CastleBottomLeft:
			return castleBottomLeftImage;
		case CastleBottomRight:
			return castleBottomRightImage;
		case Rock:
			return rockImage;
		default:
			return grassImage;
		}
	}

	private void loadImages(){
		long started = System.currentTimeMillis();
		try{
			BufferedImage img = ImageIO.read(new File(castleTopLeftImageLoc));
			castleTopLeftImage = new BufferedImage(tileSize.width,tileSize.height,BufferedImage.TYPE_INT_ARGB);

			Graphics2D gfx = castleTopLeftImage.createGraphics();
			gfx.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			gfx.drawImage(img, 0, 0, tileSize.width, tileSize.height, null);

			gfx.dispose();
		}catch(IOException e){
			castleTopLeftImage = null;
		}
		
		try{
			BufferedImage img = ImageIO.read(new File(castleTopRightImageLoc));
			castleTopRightImage = new BufferedImage(tileSize.width,tileSize.height,BufferedImage.TYPE_INT_ARGB);

			Graphics2D gfx = castleTopRightImage.createGraphics();
			gfx.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			gfx.drawImage(img, 0, 0, tileSize.width, tileSize.height, null);

			gfx.dispose();
		}catch(IOException e){
			castleTopRightImage = null;
		}
		
		try{
			BufferedImage img = ImageIO.read(new File(castleBottomRightImageLoc));
			castleBottomRightImage = new BufferedImage(tileSize.width,tileSize.height,BufferedImage.TYPE_INT_ARGB);

			Graphics2D gfx = castleBottomRightImage.createGraphics();
			gfx.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			gfx.drawImage(img, 0, 0, tileSize.width, tileSize.height, null);

			gfx.dispose();
		}catch(IOException e){
			castleBottomRightImage = null;
		}
		
		try{
			BufferedImage img = ImageIO.read(new File(castleBottomLeftImageLoc));
			castleBottomLeftImage = new BufferedImage(tileSize.width,tileSize.height,BufferedImage.TYPE_INT_ARGB);

			Graphics2D gfx = castleBottomLeftImage.createGraphics();
			gfx.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			gfx.drawImage(img, 0, 0, tileSize.width, tileSize.height, null);

			gfx.dispose();
		}catch(IOException e){
			castleBottomLeftImage = null;
		}
		
		try{
			BufferedImage img = ImageIO.read(new File(rockImageLoc));
			rockImage = new BufferedImage(tileSize.width,tileSize.height,BufferedImage.TYPE_INT_ARGB);

			Graphics2D gfx = rockImage.createGraphics();
			gfx.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			gfx.drawImage(img, 0, 0, tileSize.width, tileSize.height, null);

			gfx.dispose();
		}catch(IOException e){
			rockImage = null;
		}

		try{
			BufferedImage img = ImageIO.read(new File(grassImageLoc));
			grassImage = new BufferedImage(tileSize.width,tileSize.height,BufferedImage.TYPE_INT_ARGB);

			Graphics2D gfx = grassImage.createGraphics();
			gfx.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			gfx.drawImage(img, 0, 0, tileSize.width, tileSize.height, null);

			gfx.dispose();
		}catch(IOException e){
			grassImage = null;
		}
		long done = System.currentTimeMillis();
		System.out.printf("Time to load images: %d millis\n", done - started);
	}
}
