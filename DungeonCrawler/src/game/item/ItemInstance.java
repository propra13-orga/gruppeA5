package game.item;

import game.item.ItemType.EquipInfo;
import game.item.ItemType.UseInfo;

public class ItemInstance implements java.io.Serializable {
	private static final long serialVersionUID = 540969812021061091L;
	
	private ItemType m_type;
	
	/**
	 * Returns whether the item is usable
	 * @return
	 */
	public boolean isUsable(){
		return m_type.getUseInfo() != null;
	}
	
	/**
	 * Returns whether the item is equipable
	 * @return
	 */
	public boolean isEquipable(){
		return m_type.getEquipInfo() != null;
	}
	
	/**
	 * Returns a String containing the image URL of the item's icon.
	 * @return
	 */
	public String getIcon(){
		return m_type.m_icon;
	}
	
	/**
	 * Returns the item's name
	 * @return
	 */
	public String getName(){
		return m_type.m_name;
	}
	
	/**
	 * Returns the items formatted description as a String
	 * @return
	 */
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
	
	/**
	 * Returns the item's EquipInfo.
	 * @return	null, if not equipable
	 */
	public EquipInfo getEquipInfo(){
		return m_type.getEquipInfo();
	}
	
	/**
	 *	Returns the item's UseInfo
	 *	@return	null, if not usable
	 */
	public UseInfo getUseInfo(){
		return m_type.getUseInfo();
	}
}
