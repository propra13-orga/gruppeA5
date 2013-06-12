package game.inventory;

import game.effect.EquipEffectArmor;
import game.effect.EquipEffectBasicAttack;
import game.item.ItemInstance;
import game.item.ItemType;

import java.util.ArrayList;

public class Inventory {
	private static final int SIZE_Y = 4;
	private static final int SIZE_X = 4;
	private static final int MAX_SIZE = SIZE_Y * SIZE_X;
	private ArrayList<ItemInstance> m_items = new ArrayList<>(MAX_SIZE);
	
	public int getHeight(){
		return SIZE_Y;
	}
	public int getWidth(){
		return SIZE_X;
	}
	
	public void addItem(ItemInstance it){
		for(int i=0; i < MAX_SIZE; i++){
			if( m_items.get(i) == null ){
				m_items.set(i, it);
				return;
			}
		}
	}
	
	public ItemInstance getItem(int slot){
		return m_items.get(slot);
	}
	
	public void removeItem(int slot){
		m_items.set(slot, null);
	}
	
	public ItemInstance getItem(int x, int y){
		return m_items.get(y * SIZE_Y + x);
	}
	
	public void removeItem(int x, int y){
		m_items.set(y * SIZE_Y + x, null);
	} 
	
	public Inventory(){
		while(m_items.size() < MAX_SIZE) m_items.add(null);
	}
	
	public static Inventory getDefaultInventoryAndPopulateItemList(){
		ItemType a = new ItemType("data/items/ruby.png", "Health Potion", "Restores 100 health upon use.").makeUsable();
		ItemType b = new ItemType("data/items/brilliant_blue.png", "Mana Potion", "Restores 100 mana upon use.").makeUsable();

		ItemType c = new ItemType("data/items/ankus.png", "Weapon", "A simple staff.\nDamage: 60")
						.makeEquipable(EquipSlot.HAND, "data/player/sceptre.png", new EquipEffectBasicAttack(60) );

		ItemType d = new ItemType("data/items/leather_armour2.png", "Leather Armor", "A sturdy leather armor.\nArmor: +15")
						.makeEquipable(EquipSlot.TORSO, "data/player/leather2.png", new EquipEffectArmor(15) ); 
		
		ItemType e = new ItemType("data/items/boots2_jackboots.png", "Leather Boots", "A pair of boots.\nArmor: +5")
						.makeEquipable(EquipSlot.BOOTS, "data/player/middle_brown.png", new EquipEffectArmor(5) ); 
		
		ItemType f = new ItemType("data/items/buckler1.png", "Buckler", "A common small shield.\nArmor: +10")
						.makeEquipable(EquipSlot.OFFHAND, "data/player/boromir.png", new EquipEffectArmor(10) ); 
		
		ItemType g = new ItemType("data/items/cloak4.png", "Red cloak", "A normal cloak.\nArmor: +5")
						.makeEquipable(EquipSlot.COAT, "data/player/red.png", new EquipEffectArmor(5) ); 
		
		ItemType h = new ItemType("data/items/helmet2_etched.png", "Iron helmet", "A soldier's helmet.\nArmor: +10")
						.makeEquipable(EquipSlot.HEAD, "data/player/cap_black1.png", new EquipEffectArmor(10) ); 
		
		Inventory inv = new Inventory();
		
		
		inv.addItem( new ItemInstance(a) );
		inv.addItem( new ItemInstance(b) );
		inv.addItem( new ItemInstance(c) );
		inv.addItem( new ItemInstance(d) );
		inv.addItem( new ItemInstance(e) );
		inv.addItem( new ItemInstance(f) );
		inv.addItem( new ItemInstance(g) );
		inv.addItem( new ItemInstance(h) );
		
		return inv;
	}
}
