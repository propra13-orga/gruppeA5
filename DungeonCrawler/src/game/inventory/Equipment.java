package game.inventory;

import entity.Companion;
import game.item.ItemInstance;

import java.util.EnumMap;

public class Equipment implements java.io.Serializable {
	private static final long serialVersionUID = -4709473431771469464L;
	
	private EnumMap<EquipSlot, ItemInstance> m_equip = new EnumMap<>(EquipSlot.class);
	private Companion m_owner;
	
	/**
	 * Adds the supplied item to the supplied EquipSlow
	 * @param item
	 * @param slot
	 * @return		the ItemInstance previously in @slot, null if empty.
	 */
	public ItemInstance equipItem(ItemInstance item, EquipSlot slot){
		ItemInstance last = m_equip.get(slot);
		m_equip.put(slot, item);
		
		if(last != null)
			last.getEquipInfo().getEquipEffect().onUnequip(m_owner);
		
		item.getEquipInfo().getEquipEffect().onEquip(m_owner);
		
		return last;
	}
	
	/**
	 * Returns the ItemInstance currently equipped in the supplied slot
	 * @param slot	slot to be looked at
	 * @return	null if slot is empty, otherwise a valid ItemInstance
	 */
	public ItemInstance getEquippedItem(EquipSlot slot){
		return m_equip.get(slot);
	}
	
	/**
	 * Constructs the object for a supplied Companion
	 * @param owner
	 */
	public Equipment(Companion owner){
		m_owner = owner;
	}
}
