package map;

public class CellInfo{
	/**
	 * 'true' if the cell in question contains an event
	 */
	public boolean mHasEvent;
	
	/**
	 * The cells event id. Invalid if mHasEvent == false
	 */
	public int mEventID;
	
	/**
	 * 'true' if the cell in question contains a teleporter
	 */
	public boolean mHasTeleporter;
	
	/**
	 * The map name of the teleporter destination. Invalid if mHasTeleporter == false
	 */
	public String mNewMap;
	
	/**
	 * The Coordinate of the teleporter destination. Invalid if mHasTeleporter == false
	 */
	public Coordinate mNewPosition;
}