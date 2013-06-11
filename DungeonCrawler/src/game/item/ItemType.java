package game.item;

import game.effect.IEquipEffect;
import game.inventory.EquipSlot;

public class ItemType {
	protected String m_icon;
	protected String m_name;
	protected String m_description;
	protected EquipInfo m_equipInfo = null;
	protected UseInfo m_useInfo = null;
	
	public class EquipInfo{
		private IEquipEffect m_equipEffect;
		private EquipSlot m_equipSlot;
		private String m_appearance;
		
		public EquipSlot getEquipSlot(){
			return m_equipSlot;
		}
		public String getAppearance(){
			return m_appearance;
		}
		public IEquipEffect getEquipEffect(){
			return m_equipEffect;
		}
	}
	
	public class UseInfo{
		//Skill m_skill, etc
	}
	
	public UseInfo getUseInfo(){
		return m_useInfo;
	}
	public EquipInfo getEquipInfo(){
		return m_equipInfo;
	}
	
	public ItemType makeEquipable(EquipSlot slot, String appearance, IEquipEffect effect){
		m_equipInfo = new EquipInfo();
		m_equipInfo.m_equipSlot = slot;
		m_equipInfo.m_appearance = appearance;
		m_equipInfo.m_equipEffect = effect;
		
		return this;
	}
	
	public ItemType makeUsable(){
		m_useInfo = new UseInfo();
		return this;
	}
	
	public ItemType(String icon, String name, String desc){
		m_icon = icon;
		m_name = name;
		m_description = desc;
	}
}
