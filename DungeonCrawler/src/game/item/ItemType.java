package game.item;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import game.effect.IEquipEffect;
import game.inventory.EquipSlot;
import game.skill.Skill;

public class ItemType implements java.io.Serializable {
	private static final long serialVersionUID = -673460721227801548L;
	
	protected String m_icon;
	protected String m_name;
	protected String m_description;
	protected EquipInfo m_equipInfo = null;
	protected UseInfo m_useInfo = null;
	protected int m_price;
	
	public String getIcon(){
		return m_icon;
	}
	public String getName(){
		return m_name;
	}
	public String getDescription(){
		return m_description;
	}
	public int getPrice(){
		return m_price;
	}
	
	public class EquipInfo implements java.io.Serializable {
		private static final long serialVersionUID = 658777784124385071L;
		
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
	
	public class UseInfo implements java.io.Serializable{
		private static final long serialVersionUID = 3863172458429174517L;
		
		private Skill m_skill;
		
		public Skill getSkill(){
			return m_skill;
		}
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
	
	public ItemType makeUsable(Skill skill){
		m_useInfo = new UseInfo();
		m_useInfo.m_skill = skill;
		return this;
	}
	
	public ItemType(String icon, String name, String desc, int price){
		m_icon = icon;
		m_name = name;
		m_description = desc;
		m_price = price;
	}
	
	private static HashMap<String, ItemType> s_itemTypes = new HashMap<>();
	private static Set<Entry<String, ItemType>> s_entrySet;
	
	public static ItemType getItemType(String name){
		return s_itemTypes.get(name.toLowerCase());
	}
	
	public static Set<Entry<String, ItemType>> getItemTypeList(){
		return s_entrySet;
	}
	
	private static void loadItemTypes(){
		ItemTypeLoader itl = new ItemTypeLoader();
		itl.load("data/itemtypes.txt");
		s_itemTypes = itl.getItemTypeList();
		
		s_entrySet = Collections.unmodifiableSet(s_itemTypes.entrySet());
	}
	
	static{
		loadItemTypes();
	}
}
