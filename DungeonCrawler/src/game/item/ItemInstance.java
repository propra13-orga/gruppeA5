package game.item;

import game.item.ItemType.EquipInfo;
import game.item.ItemType.UseInfo;

public class ItemInstance implements java.io.Serializable {
	private static final long serialVersionUID = 540969812021061091L;
	
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
		String desc = m_type.m_description;
		
		if(m_type.m_useInfo != null)
			desc += "\n\nOn use:\n " + m_type.m_useInfo.getSkill().getDescription();
		
		if(m_type.m_equipInfo != null)
			desc += "\n\nOn equip:\n " + m_type.m_equipInfo.getEquipEffect().getDescription();
	
		return desc;
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
