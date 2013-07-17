package game.item;

import std.StdDraw;
import map.Map;

public class PickupableItem {
	ItemType m_type;
	private int m_x;
	private int m_y;
	
	/**
	 * Get X coordinate of item in the world
	 * @return
	 */
	public int getX(){
		return m_x;
	}
	
	/**
	 * Get Y coordinate of item in the world
	 * @return
	 */
	public int getY(){
		return m_y;
	}
	
	/**
	 * Returns the item type of the item
	 * @return
	 */
	public ItemType getItemType(){
		return m_type;
	}
	
	/**
	 * Renders the item to the specified map
	 * @param map
	 */
	public void render(Map map){
		double x = map.getCanvasX(m_x);
		double y = map.getCanvasY(m_y);
		
		StdDraw.picture( x, y, m_type.getIcon() );
	}
	
	/**
	 * Creates an item at location (x,y) of type itemType
	 * @param x
	 * @param y
	 * @param itemType	case insensitive
	 */
	public PickupableItem(int x, int y, String itemType){
		m_x = x;
		m_y = y;
		m_type = ItemType.getItemType(itemType);
	}
}
