package game.inventory;

import entity.Companion;
import game.item.ItemInstance;

import java.util.EnumMap;

public class Equipment {
	private EnumMap<EquipSlot, ItemInstance> m_equip = new EnumMap<>(EquipSlot.class);
	private Companion m_owner;
	
	public ItemInstance equipItem(ItemInstance item, EquipSlot slot){
		ItemInstance last = m_equip.get(slot);
		m_equip.put(slot, item);
		
		if(last != null)
			last.getEquipInfo().getEquipEffect().onUnequip(m_owner);
		
		item.getEquipInfo().getEquipEffect().onEquip(m_owner);
		
		return last;
	}
	
	public ItemInstance getEquippedItem(EquipSlot slot){
		return m_equip.get(slot);
	}
	public Equipment(Companion owner){
		m_owner = owner;
	}
}
