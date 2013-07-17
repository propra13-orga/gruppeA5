package map;

public class CellInfo{
	/**
	 * 'true' if the cell in question contains an event
	 */
	public boolean mHasEvent = false;
	
	/**
	 * The cells event id. Invalid if mHasEvent == false
	 */
	public int mEventID = 0;
	
	/**
	 * 'true' if the cell in question contains a teleporter
	 */
	public boolean mHasTeleporter = false;
	
	/**
	 * The map name of the teleporter destination. Invalid if mHasTeleporter == false
	 */
	public String mNewMap = null;
	
	/**
	 * The Coordinate of the teleporter destination. Invalid if mHasTeleporter == false
	 */
	public Coordinate mNewPosition = null;
	
}