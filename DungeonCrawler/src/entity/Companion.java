package entity;

import game.inventory.EquipSlot;
import game.inventory.Equipment;
import game.item.ItemInstance;
import game.player.DamageSkill;
import game.player.Skill;

import java.util.ArrayList;
import java.util.List;

import std.StdDraw;

public class Companion implements IEntity {
	private String m_name;
	private String m_baseAppearance;
	
	private Skill m_attackSkill;
	private static final Skill m_defaultAttackSkill = new DamageSkill("Punch", 25);
	
	private ArrayList<Skill> m_skillNames = new ArrayList<>();
	private Equipment m_equipment = new Equipment(this);
	private CompanionStats m_stats = new CompanionStats();
	
	public Skill getBasicAttack(){
		return m_attackSkill;
	}
	public void setBasicAttack(Skill newAttack){
		if(newAttack==null)
			m_attackSkill = m_defaultAttackSkill;
		else
			m_attackSkill = newAttack;
	}
	
	public List<Skill> getSkillList(){
		return m_skillNames;
	}

	public String getName(){
		return m_name;
	}
	
	public String getBaseAppearance(){
		return m_baseAppearance;
	}
	
	public Equipment getEquipment(){
		return m_equipment;
	}
	
	public CompanionStats getStats(){
		return m_stats;
	}
	
	private void renderLayer(double x, double y, ItemInstance ii){
		if(ii != null)
			StdDraw.picture(x,y, ii.getEquipInfo().getAppearance() );
	}
	
	@Override
	public void render(double x, double y) {
		
		renderLayer(x, y, m_equipment.getEquippedItem(EquipSlot.COAT) );
		StdDraw.picture(x,y,m_baseAppearance);
		renderLayer(x, y, m_equipment.getEquippedItem(EquipSlot.HEAD) );
		renderLayer(x, y, m_equipment.getEquippedItem(EquipSlot.HAND) );
		renderLayer(x, y, m_equipment.getEquippedItem(EquipSlot.OFFHAND) );
		renderLayer(x, y, m_equipment.getEquippedItem(EquipSlot.TORSO) );
		renderLayer(x, y, m_equipment.getEquippedItem(EquipSlot.LEGS) );
		renderLayer(x, y, m_equipment.getEquippedItem(EquipSlot.BOOTS) );

	}	
	
	public static List<Companion> getDefaultCompanionList(){
		List<Companion> list = new ArrayList<>();
		Companion c;
		
		c = new Companion();
		c.m_name = "John";
		c.m_baseAppearance = "data/player/deep_elf_m.png";
		c.m_attackSkill = new DamageSkill("Attack", 35);
		c.m_skillNames.add( new DamageSkill("Strike", 50) );
		c.m_skillNames.add( new DamageSkill("Fireball", 100) );
		
		list.add(c);
		
		c = new Companion();
		c.m_name = "Bob";
		c.m_baseAppearance = "data/player/dwarf_m.png";
		c.m_attackSkill = new DamageSkill("Attack", 35);
		c.m_skillNames.add( new DamageSkill("DUNK!", 65) );
		list.add(c);
		
		return list;
	}

	@Override
	public int doDamage(int dmg) {
		int armor = m_stats.mArmor;
		//A single armor point adds 1% effective hp.
		int adjustedDmg = (int) Math.max(  dmg / (1 + (armor*0.01) ) , 1   );
		
		m_stats.mCurrHealth = Math.max(m_stats.mCurrHealth - adjustedDmg, 0);
		
		return adjustedDmg;
	}

	@Override
	public boolean isDead() {
		return m_stats.mCurrHealth <= 0;
	}

	@Override
	public int getCurrHealth() {
		return m_stats.mCurrHealth;
	}

	@Override
	public int getMaxHealth() {
		return m_stats.mMaxHealth;
	}


}
