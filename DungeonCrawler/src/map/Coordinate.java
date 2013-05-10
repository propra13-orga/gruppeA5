package map;

public class Coordinate{
	/**
	 * The x and y coordinate
	 */
	public int mX, mY;
	
	/**
	 * Returns a unique hash code for coordinates up to 999
	 */
	public int hashCode(){
		return mX*1000 + mY;
	}
	
	public boolean equals(Object o){
		Coordinate rhs = (Coordinate) o;
		
		return mX == rhs.mX && mY == rhs.mY;
	}
	
	public Coordinate(int x, int y){
		mX = x;
		mY = y;
	}
	public Coordinate(){
	
	}
}