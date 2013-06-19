package game.item;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import game.effect.EquipEffectArmor;
import game.effect.EquipEffectBasicAttack;
import game.effect.EquipEffectMaxMana;
import game.effect.IEquipEffect;
import game.inventory.EquipSlot;
import game.skill.Skill;

public class ItemType {
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
		
		ItemType a = new ItemType("data/items/ruby.png", "Health Potion", "A common magical potion\n used toheal wounds.", 50)
		.makeUsable( Skill.getSkill("HealthPotion") );
		
		ItemType b = new ItemType("data/items/brilliant_blue.png", "Mana Potion", "The brilliant blue potion can be\nused to restore magical\nenergy.", 50)
				.makeUsable( Skill.getSkill("ManaPotion") );
		
		ItemType c = new ItemType("data/items/ankus.png", "Staff", "A simple staff.", 170)
				.makeEquipable(EquipSlot.HAND, "data/player/sceptre.png", new EquipEffectBasicAttack(60) );
		
		ItemType d = new ItemType("data/items/leather_armour2.png", "Leather Armor", "A sturdy leather armor.", 300)
				.makeEquipable(EquipSlot.TORSO, "data/player/leather2.png", new EquipEffectArmor(15) ); 
		
		ItemType e = new ItemType("data/items/boots2_jackboots.png", "Leather Boots", "A pair of boots.", 120)
				.makeEquipable(EquipSlot.BOOTS, "data/player/middle_brown.png", new EquipEffectArmor(5) ); 
		
		ItemType f = new ItemType("data/items/buckler1.png", "Buckler", "A common small shield.", 150)
				.makeEquipable(EquipSlot.OFFHAND, "data/player/boromir.png", new EquipEffectArmor(10) ); 
		
		ItemType g = new ItemType("data/items/cloak4.png", "Red cloak", "A normal cloak.", 80)
				.makeEquipable(EquipSlot.COAT, "data/player/red.png", new EquipEffectArmor(5) ); 
		
		ItemType h = new ItemType("data/items/helmet2_etched.png", "Iron helmet", "A soldier's helmet.", 100)
				.makeEquipable(EquipSlot.HEAD, "data/player/cap_black1.png", new EquipEffectArmor(10) ); 
		
		ItemType i = new ItemType("data/items/dark_blue.png", "Mystic Book", "An old tome that holds magic.", 100)
		.makeEquipable(EquipSlot.OFFHAND, "data/player/book_blue.png", new EquipEffectMaxMana(40) ); 
		
		
		
		s_itemTypes.put("HealthPotion".toLowerCase(), a);
		s_itemTypes.put("ManaPotion".toLowerCase(), b);
		s_itemTypes.put("Staff".toLowerCase(), c);
		s_itemTypes.put("LeatherArmor".toLowerCase(), d);
		s_itemTypes.put("LeatherBoots".toLowerCase(), e);
		s_itemTypes.put("Buckler".toLowerCase(), f);
		s_itemTypes.put("RedCloak".toLowerCase(), g);
		s_itemTypes.put("IronHelmet".toLowerCase(), h);
		s_itemTypes.put("MysticBook".toLowerCase(), i);
		
		s_entrySet = Collections.unmodifiableSet(s_itemTypes.entrySet());
	}
	
	static{
		loadItemTypes();
	}
}
