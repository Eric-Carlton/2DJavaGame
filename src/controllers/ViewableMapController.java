package controllers;

import java.awt.Dimension;
import java.awt.Point;

import models.BadDimensionException;
import models.LoadedMap;
import models.Map;
import models.MutableMap;

public class ViewableMapController {
	
	private LoadedMap map;
	private Dimension dim;
	private Point start;
	private Map viewableArea;
	private Point pointInMapForTopLeftOfViewable;
	
	
	public ViewableMapController(LoadedMap map, Dimension dim, Point start) throws BadDimensionException{
		this.map = map;
		this.dim = dim;
		this.start = start;
		this.viewableArea = null;
		getViewableMapForStartingPoint();
	}
	
	public Map getViewableArea(){
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
			
			MutableMap mMap = new MutableMap((2*dim.width)+1, (2*dim.height)+1, map.getTileSize(), map.getBases());
			
			pointInMapForTopLeftOfViewable = new Point(p.x - dim.width, p.y - dim.height);
			
			for(int y = pointInMapForTopLeftOfViewable.y; y < pointInMapForTopLeftOfViewable.y + 2*dim.height + 1; y++){
				for(int x = pointInMapForTopLeftOfViewable.x; x < pointInMapForTopLeftOfViewable.x + 2*dim.width + 1; x++){
					if(map.tileAt(y, x) == null){
						System.out.printf("x: %d y: %d\n\n", x, y);
						map.dump();
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
}
