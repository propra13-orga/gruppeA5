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
	
	/**
	 * Returns the item type's icon as a String
	 * @return
	 */
	public String getIcon(){
		return m_icon;
	}
	
	/**
	 * Returns the item type's name.
	 * @return
	 */
	public String getName(){
		return m_name;
	}
	
	/**
	 * Returns the item type's description.
	 * @return
	 */
	public String getDescription(){
		return m_description;
	}
	
	/**
	 * Get the item type's gold cost.
	 * @return
	 */
	public int getPrice(){
		return m_price;
	}
	
	public class EquipInfo implements java.io.Serializable {
		private static final long serialVersionUID = 658777784124385071L;
		
		private IEquipEffect m_equipEffect;
		private EquipSlot m_equipSlot;
		private String m_appearance;
		
		/**
		 * Returns the EquipSlot this item has to be fitted in.
		 * @return
		 */
		public EquipSlot getEquipSlot(){
			return m_equipSlot;
		}
		
		/**
		 * Returns the base appearance as a String of an image url.
		 * @return
		 */
		public String getAppearance(){
			return m_appearance;
		}
		
		/**
		 * Returns the EquipEffect of this item type.
		 * @return
		 */
		public IEquipEffect getEquipEffect(){
			return m_equipEffect;
		}
	}
	
	public class UseInfo implements java.io.Serializable{
		private static final long serialVersionUID = 3863172458429174517L;
		
		private Skill m_skill;
		
		/**
		 * Returns the Skill that this item will activate upon use.
		 * @return
		 */
		public Skill getSkill(){
			return m_skill;
		}
	}
	
	/**
	 * Return this item type's UseInfo
	 * @return
	 */
	public UseInfo getUseInfo(){
		return m_useInfo;
	}
	
	/**
	 * Returns this item type's EquipInfo.
	 * @return
	 */
	public EquipInfo getEquipInfo(){
		return m_equipInfo;
	}
	
	/**
	 * Turns this item type equipable
	 * @param slot		Slot to equip
	 * @param appearance	item layer that will be rendered over the player character
	 * @param effect	EquipEffect of this item
	 * @return	this
	 */
	public ItemType makeEquipable(EquipSlot slot, String appearance, IEquipEffect effect){
		m_equipInfo = new EquipInfo();
		m_equipInfo.m_equipSlot = slot;
		m_equipInfo.m_appearance = appearance;
		m_equipInfo.m_equipEffect = effect;
		
		return this;
	}
	
	/**
	 * Makes this item type usable
	 * @param skill		Skill that will be used upon item activation
	 * @return	this
	 */
	public ItemType makeUsable(Skill skill){
		m_useInfo = new UseInfo();
		m_useInfo.m_skill = skill;
		return this;
	}
	
	/**
	 * Creates a new item type
	 * @param icon	Path to image url of icon
	 * @param name	Name of item type
	 * @param desc	Description of item type
	 * @param price	Gold price of item type
	 */
	public ItemType(String icon, String name, String desc, int price){
		m_icon = icon;
		m_name = name;
		m_description = desc;
		m_price = price;
	}
	
	private static HashMap<String, ItemType> s_itemTypes = new HashMap<>();
	private static Set<Entry<String, ItemType>> s_entrySet;
	
	/**
	 * Returns the ItemType with specified name
	 * @param name	Name to look for. Case insensitive
	 * @return	null, if not found.
	 */
	public static ItemType getItemType(String name){
		return s_itemTypes.get(name.toLowerCase());
	}
	
	/**
	 * Returns the set of all item types.
	 * @return
	 */
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
