package game.item;

import java.util.ArrayList;
import map.Map;

public class PickupPool {
	private ArrayList<PickupableItem> m_pickupables = new ArrayList<>();
	
	public PickupableItem PickupableAt( int x, int y ){

		for( PickupableItem m : m_pickupables ){
			if( m.getX() == x && m.getY() == y ){
				return m;
			}
		}

		return null;
		
	}
	
	
	public void renderAll(Map map){
		for( PickupableItem m : m_pickupables ) {
			m.render(map);
		}
	}
	
	public void addPickupable(PickupableItem m){
		m_pickupables.add(m);
	}
	
	public void removePickupable(PickupableItem m){
		m_pickupables.remove(m);
	}
	
	public PickupPool(){
	}
}
