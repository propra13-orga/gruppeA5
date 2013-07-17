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
	
	/**
	 * Returns Inventory height
	 * @return
	 */
	public int getHeight(){
		return SIZE_Y;
	}
	
	/**
	 * Returns inventory width
	 * @return
	 */
	public int getWidth(){
		return SIZE_X;
	}
	
	/**
	 * Adds the supplied item to the inventory.
	 * If the inventory is full, nothing will happen.
	 * @param it	if null, nothing will happen
	 */
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
	
	/**
	 * Returns whether the inventory is full
	 * @return	true if full
	 */
	public boolean isFull(){
		return m_currentNumOfItems >= MAX_SIZE;
	}
	
	/**
	 * Returns item at slot.
	 * @param slot	Supplied slot. All slots are numbered 0 through X
	 * @return
	 */
	public ItemInstance getItem(int slot){
		return m_items.get(slot);
	}
	
	/**
	 * Removes item in slot
	 * @param slot	Supplied slot. All slots are numbered 0 through X
	 */
	public void removeItem(int slot){
		if(m_items.get(slot) != null){
			m_currentNumOfItems -= 1;
			m_items.set(slot, null);
		}
	}
	
	/**
	 * Looks for ItemInstance and removes it from the inventory
	 * @param item	Item to be removed, nothing happens if not found
	 */
	public void removeItem(ItemInstance item) {
		for(int i=0; i<m_items.size(); i++){
			if( item == m_items.get(i) ){
				removeItem(i);
				return;
			}
		}
	}
	
	
	/**
	 * Returns an item from the inventory. Slots are labeled as a 2D array
	 * @param x
	 * @param y
	 * @return
	 */
	public ItemInstance getItem(int x, int y){
		return getItem(y * SIZE_Y + x);
	}
	
	/**
	 * Removes an item from the inventory. Slots are labeled as a 2D array.
	 * @param x
	 * @param y
	 */
	public void removeItem(int x, int y){
		removeItem(y * SIZE_Y + x);
	} 
	
	/**
	 * Get a list of all items
	 * @return
	 */
	public List<ItemInstance> getItemList(){
		return m_items;
	}
	
	public Inventory(){
		while(m_items.size() < MAX_SIZE) m_items.add(null);
	}
		
}
