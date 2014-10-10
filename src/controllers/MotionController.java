package controllers;

import models.Map;

public class MotionController {

	private ViewableMapController mapCtrl;
	
	public MotionController(ViewableMapController mapCtrl){
		this.mapCtrl = mapCtrl;
	}
	
	public Map moveLeft(){
		mapCtrl.moveLeft(1);
		return mapCtrl.getViewableArea();
	}
	
	public Map moveRight(){
		mapCtrl.moveRight(1);
		return mapCtrl.getViewableArea();
	}
	
	public Map moveUp(){
		mapCtrl.moveUp(1);
		return mapCtrl.getViewableArea();
	}
	
	public Map moveDown(){
		mapCtrl.moveDown(1);
		return mapCtrl.getViewableArea();
	}
}
