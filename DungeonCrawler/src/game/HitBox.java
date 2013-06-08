package game;

import std.StdDraw;

public class HitBox {
	//private final static double HEIGHT = Constants.TILE_SIZE - 12;
	//private final static double WIDTH  = Constants.TILE_SIZE - 12;
	
	//size of hitbox
	//private double m_maxX 		= 0.;
	private double m_minX 		= 0.;
	//private double m_maxY 		= 0.;
	private double m_minY 		= 0.;
	private double m_halfHeight = 0.;
	private double m_halfWidth 	= 0.;
	
	//position of hitbox
	private double m_x = 0.;
	private double m_y = 0.;
	
	public void render(){
		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.rectangle(m_x, m_y, 2*m_halfWidth, 2*m_halfHeight);
	}
	
	public void setPosition(double x, double y){
		//If I ever need a getPosition, this one here might be a bad idea. Maybe better to have a 
		//private getActualPosition() and use it in collidesWith().
		m_x = x + m_minX;
		m_y = y + m_minY;
	}
	
	public boolean collidesWith(HitBox b){
		if ( Math.abs(m_x - b.m_x) > m_halfWidth + b.m_halfWidth ) return false;
	    if ( Math.abs(m_y - b.m_y) > m_halfHeight + b.m_halfHeight ) return false;
	    
	    return true;
	}
	
	public static boolean collidesWith(HitBox a, HitBox b){
		return a.collidesWith(b);
	}
	
	public HitBox(double width, double height){
		if(height < 0 || width < 0)
			throw new IllegalArgumentException("Bad HitBox dimensions.");
	
		//m_maxX = width;
		//m_maxY = height;
		
		m_halfHeight = height / 2.;
		m_halfWidth = width / 2.;
	}
	
	public HitBox(double minX, double minY, double maxX, double maxY){
		if(maxY < 0 || maxX < 0 || minY > maxY || minX > maxX)
			throw new IllegalArgumentException("Bad HitBox dimensions.");
	
		m_minX = minX;
		m_minY = minY;
		//m_maxX = maxX;
		//m_maxY = maxY;
		
		m_halfHeight = (maxY-minY) / 2.;
		m_halfWidth = (maxX-minX) / 2.;
	}
}
