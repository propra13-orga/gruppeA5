package game.item;

import std.StdDraw;
import map.Map;

public class PickupableItem {
	ItemType m_type;
	private int m_x;
	private int m_y;
	
	public int getX(){
		return m_x;
	}
	public int getY(){
		return m_y;
	}
	
	public ItemType getItemType(){
		return m_type;
	}
	
	public void render(Map map){
		double x = map.getCanvasX(m_x);
		double y = map.getCanvasY(m_y);
		
		StdDraw.picture( x, y, m_type.getIcon() );
	}
	
	public PickupableItem(int x, int y, String itemType){
		m_x = x;
		m_y = y;
		m_type = ItemType.getItemType(itemType);
	}
}
