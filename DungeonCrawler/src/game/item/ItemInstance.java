package game.item;

import game.item.ItemType.EquipInfo;
import game.item.ItemType.UseInfo;

public class ItemInstance {
	private ItemType m_type;
	
	public boolean isUsable(){
		return m_type.getUseInfo() != null;
	}
	
	public boolean isEquipable(){
		return m_type.getEquipInfo() != null;
	}
	
	public String getIcon(){
		return m_type.m_icon;
	}
	
	public String getName(){
		return m_type.m_name;
	}
	
	public String getDescription(){
		return m_type.m_description;
	}
	
	public ItemInstance(ItemType type){
		m_type = type;
	}
	
	public EquipInfo getEquipInfo(){
		return m_type.getEquipInfo();
	}
	public UseInfo getUseInfo(){
		return m_type.getUseInfo();
	}
}
