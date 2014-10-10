package models;

public class BadDimensionException extends Exception {
	private static final long serialVersionUID = 4604433574073516921L;
	
	public BadDimensionException(){ 
		super(); 
	}
	public BadDimensionException(String message){ 
		super(message); 
	}

}
