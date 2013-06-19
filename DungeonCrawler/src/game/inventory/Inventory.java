package game.inventory;

import game.item.ItemInstance;
import java.util.ArrayList;
import java.util.List;

public class Inventory {
	private static final int SIZE_Y = 4;
	private static final int SIZE_X = 4;
	private static final int MAX_SIZE = SIZE_Y * SIZE_X;
	private ArrayList<ItemInstance> m_items = new ArrayList<>(MAX_SIZE);
	private int m_currentNumOfItems = 0;
	
	public int getHeight(){
		return SIZE_Y;
	}
	public int getWidth(){
		return SIZE_X;
	}
	
	public void addItem(ItemInstance it){
		if(it == null)
			return;
	
		for(int i=0; i < MAX_SIZE; i++){
			if( m_items.get(i) == null ){
				m_items.set(i, it);
				m_currentNumOfItems += 1;
				return;
			}
		}
	}
	
	public boolean isFull(){
		return m_currentNumOfItems >= MAX_SIZE;
	}
	
	public ItemInstance getItem(int slot){
		return m_items.get(slot);
	}
	
	public void removeItem(int slot){
		if(m_items.get(slot) != null){
			m_currentNumOfItems -= 1;
			m_items.set(slot, null);
		}
	}
	
	public void removeItem(ItemInstance item) {
		for(int i=0; i<m_items.size(); i++){
			if( item == m_items.get(i) ){
				removeItem(i);
				return;
			}
		}
	}
	
	public ItemInstance getItem(int x, int y){
		return getItem(y * SIZE_Y + x);
	}
	
	public void removeItem(int x, int y){
		removeItem(y * SIZE_Y + x);
	} 
	
	public List<ItemInstance> getItemList(){
		return m_items;
	}
	
	public Inventory(){
		while(m_items.size() < MAX_SIZE) m_items.add(null);
	}
		
}
