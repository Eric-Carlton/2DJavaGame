package controllers;

import java.awt.Dimension;
import java.awt.Point;

import models.BadDimensionException;
import models.Base;
import models.LoadedMap;
import models.Map;
import models.MutableMap;
import models.Tile;

public class ViewableMapController {
	
	private LoadedMap map;
	private Dimension dim;
	private Point start;
	private Map viewableArea;
	private Point pointInMapForTopLeftOfViewable;
	
	private boolean isOnUpperBorder;
	private boolean isOnRightBorder;
	private boolean isOnLeftBorder;
	private boolean isOnLowerBorder;
	
	public ViewableMapController(LoadedMap map, Dimension dim, Point start) throws BadDimensionException{
		this.map = map;
		this.dim = dim;
		this.start = start;
		this.viewableArea = null;
		
		getViewableMapForStartingPoint();
	}
	
	public Map getViewableArea(){
		setBorderBools();
		return this.viewableArea;
	}
	
	public void moveLeft(int units){
		getViewableMapForPoint(new Point(start.x - units, start.y));
	}
	
	public void moveRight(int units){
		getViewableMapForPoint(new Point(start.x + units, start.y));
	}
	
	public void moveUp(int units){
		getViewableMapForPoint(new Point(start.x, start.y - units));
	}
	
	public void moveDown(int units){
		getViewableMapForPoint(new Point(start.x, start.y + units));
	}
	
	public Point getPointInMapForTopLeftOfViewable(){
		return pointInMapForTopLeftOfViewable;
	}
	
	public Point getCenterPoint(){
		return new Point(dim.width, dim.height);
	}
	
	public Map getMap(){
		return map;
	}
	
	public boolean isOnUpperBorder(){
		return isOnUpperBorder;
	}
	
	public boolean isOnLowerBorder(){
		return isOnLowerBorder;
	}
	
	public boolean isOnLeftBorder(){
		return isOnLeftBorder;
	}
	
	public boolean isOnRightBorder(){
		return isOnRightBorder;
	}
	
	public Dimension getDimensions(){
		return dim;
	}
	
	public void dump(){
		System.out.printf("\nViewable Map\n-----------------\nLeft border: %b\nRight border:%b\nUpper border: %b\nLower border: %b\nCenter Point\n\tX: %d\n\tY: %d\nUpper Left\n\tX: %d\n\tY: %d\n",isOnLeftBorder,isOnRightBorder,isOnUpperBorder,isOnLowerBorder,getCenterPoint().x, getCenterPoint().y, pointInMapForTopLeftOfViewable.x,pointInMapForTopLeftOfViewable.y);
	}
	
	private void getViewableMapForStartingPoint() throws BadDimensionException{
		if(start.x < 0 || start.x >= map.getCols() || start.y < 0 || start.y >= map.getRows()){
			throw new BadDimensionException("Dimensions too large for viewable map to be generated");
		}
		else{
			getViewableMapForPoint(start);
		}
	}
	
	private void getViewableMapForPoint(Point p)  {
		
		p = correctPoint(p);
		if (viewableArea == null || !p.equals(start)){
			
			for(int i = 0; i < map.getRows(); i++){
				for(int j = 0; j < map.getCols(); j++){
					map.tileAt(i, j).isVisible = false;
				}
			}
			
			MutableMap mMap = new MutableMap((2*dim.width)+1, (2*dim.height)+1, map.getTileSize(), map.getBases());
			
			pointInMapForTopLeftOfViewable = new Point(p.x - dim.width, p.y - dim.height);
			
			for(int y = pointInMapForTopLeftOfViewable.y; y < pointInMapForTopLeftOfViewable.y + 2*dim.height + 1; y++){
				for(int x = pointInMapForTopLeftOfViewable.x; x < pointInMapForTopLeftOfViewable.x + 2*dim.width + 1; x++){
					if(map.tileAt(y, x) == null){
						System.out.printf("x: %d y: %d\n\n", x, y);
						map.dump();
					}
					map.tileAt(y, x).isVisible = true;
					
					/*
					 * A base is considered one atomic unit that should load at once.
					 * Therefore, if any tile of a base is visible then
					 * all tiles in that base should be considered visible even if they won't be drawn.
					 */
					for(Base b : map.getBases()){
						if(b.isTileInBase(y, x)){
							for(Tile t : b.getBaseTiles()){
								t.isVisible = true;
							}
						}
					}
					mMap.addTile(map.tileAt(y, x));
				}
			}
			
			this.start = p;
			Map viewableMap = new Map(mMap);
			this.viewableArea = viewableMap;
		}
	}
	
	private Point correctPoint(Point toCorrect){
		Point corrected = toCorrect;
		
		if(toCorrect.x - dim.width < 0){
			corrected.x = toCorrect.x + (dim.width - toCorrect.x);
		}
		else if(toCorrect.x + dim.width >= map.getCols()){
			corrected.x = map.getCols() - dim.width - 1;
		}
		
		if(toCorrect.y - dim.height < 0){
			corrected.y = toCorrect.y + (dim.height - toCorrect.y);
		}
		else if(toCorrect.y + dim.height >= map.getRows()){
			corrected.y = map.getRows() - dim.height - 1;
		}
		
		return corrected;
	}
	
	private void setBorderBools(){
		if(start.x - dim.width <= 0)
			isOnLeftBorder = true;
		else isOnLeftBorder = false;
		
		if(start.x + dim.width >= map.getCols() - 1)
			isOnRightBorder = true;
		else isOnRightBorder = false;
		
		if(start.y - dim.height <= 0)
			isOnUpperBorder = true;
		else isOnUpperBorder = false;
		
		if(start.y + dim.height >= map.getRows() - 1)
			isOnLowerBorder = true;
		else isOnLowerBorder = false;
	}
}
