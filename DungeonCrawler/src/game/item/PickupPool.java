package game.item;

import java.util.ArrayList;
import map.Map;

public class PickupPool {
	private ArrayList<PickupableItem> m_pickupables = new ArrayList<>();
	
	/**
	 * Returns the item at location (x,y) in grid space
	 * @param x
	 * @param y
	 * @return	null, if no item there, else a valid PickupableItem
	 */
	public PickupableItem PickupableAt( int x, int y ){

		for( PickupableItem m : m_pickupables ){
			if( m.getX() == x && m.getY() == y ){
				return m;
			}
		}

		return null;
		
	}
	
	/**
	 * Renders all items to the screen
	 * @param map
	 */
	public void renderAll(Map map){
		for( PickupableItem m : m_pickupables ) {
			m.render(map);
		}
	}
	
	/**
	 * Adds a new item to the pool of collectible items
	 * @param m
	 */
	public void addPickupable(PickupableItem m){
		m_pickupables.add(m);
	}
	
	/**
	 * removes an item from the pool of collectible items
	 * @param m
	 */
	public void removePickupable(PickupableItem m){
		m_pickupables.remove(m);
	}
	
	public PickupPool(){
	}
}
