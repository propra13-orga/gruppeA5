package entity;

import game.inventory.EquipSlot;
import game.inventory.Equipment;
import game.item.ItemInstance;
import game.skill.DamageType;
import game.skill.SingleTargetDmgSkill;
import game.skill.Skill;

import java.util.ArrayList;
import java.util.List;

import std.StdDraw;

public class Companion implements IEntity {
	private static final long serialVersionUID = 6630624532692618980L;
	
	private String m_name;
	private String m_baseAppearance;
	
	private Skill m_attackSkill;
	private static final Skill m_defaultAttackSkill = new SingleTargetDmgSkill("Punch", "", 25, DamageType.PHYSICAL, 0);
	
	private ArrayList<Skill> m_skillNames = new ArrayList<>();
	private Equipment m_equipment = new Equipment(this);
	private UnitStats m_stats = new UnitStats();
	
	/**
	 * Returns the Companion's basic attack skill.
	 */
	public Skill getBasicAttack(){
		return m_attackSkill;
	}
	
	/**
	 * Sets the Companion's basic attack skill
	 * @param newAttack	New attack skill
	 */
	public void setBasicAttack(Skill newAttack){
		if(newAttack==null)
			m_attackSkill = m_defaultAttackSkill;
		else
			m_attackSkill = newAttack;
	}
	
	/**
	 * Returns a list of all skills.
	 */
	public List<Skill> getSkillList(){
		return m_skillNames;
	}

	/**
	 * Returns whether the unit is able to cast a skill
	 * @param e 	Skill to be checked
	 * @return		true if skill can be cast
	 */
	public boolean canCastSkill(Skill e){
		return e.getManaCost() <= m_stats.mCurrMana;
	}

	/**
	 * Returns the name of the Companion
	 */
	public String getName(){
		return m_name;
	}
	
	/**
	 * Returns the basic appearance of the unit
	 * @return	A string containing the image path for the base appearance
	 */
	public String getBaseAppearance(){
		return m_baseAppearance;
	}
	
	/**
	 * Returns the unit's equipment
	 * @return
	 */
	public Equipment getEquipment(){
		return m_equipment;
	}

	private void renderLayer(double x, double y, ItemInstance ii){
		if(ii != null)
			StdDraw.picture(x,y, ii.getEquipInfo().getAppearance() );
	}
	
	@Override
	/**
	 * Renders the unit
	 * @param x	x coordinate to render
	 * @param y	y coordinate to render
	 */
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
	
	/**
	 * Debug method that creates a bunch of default Companions.
	 * @return	a list of Companions
	 */
	public static List<Companion> getDefaultCompanionList(){
		List<Companion> list = new ArrayList<>();
		Companion c;
		
		c = new Companion();
		c.m_name = "John";
		c.m_baseAppearance = "data/player/deep_elf_m.png";
		c.m_attackSkill = new SingleTargetDmgSkill("Attack", "", 35, DamageType.PHYSICAL, 0);
		
		c.m_skillNames.add( Skill.getSkill("Strike") );
		c.m_skillNames.add( Skill.getSkill("Fireball") );

		c.m_stats.mMaxHealth = 100;
		c.m_stats.mMaxMana = 100;
		c.restoreToFull();
		list.add(c);
		
		c = new Companion();
		c.m_name = "Bob";
		c.m_baseAppearance = "data/player/dwarf_m.png";
		c.m_attackSkill = new SingleTargetDmgSkill("Attack", "", 35, DamageType.PHYSICAL, 0);
		
		c.m_skillNames.add( Skill.getSkill("Smite") );
		c.m_skillNames.add( Skill.getSkill("LayOnHands") );

		c.m_stats.mMaxHealth = 100;
		c.m_stats.mMaxMana = 100;
		c.restoreToFull();
		list.add(c);
		
		return list;
	}

	/**
	 * Heals the Companion to max hp/mana
	 */
	public void restoreToFull(){
		m_stats.mCurrHealth = m_stats.mMaxHealth;
		m_stats.mCurrMana = m_stats.mMaxMana;
	}

	@Override
	/**
	 * Deals damage to this unit.
	 * @param dmg		how much damage
	 * @param dmgType	damage type
	 */
	public int doDamage(int dmg, DamageType dmgType) {
		int armor = 0;
	
		switch(dmgType){
		case FIRE: 		armor = m_stats.mFireResist; break;
		case ICE:		armor = m_stats.mIceResist;  break;
		case PHYSICAL:	armor = m_stats.mArmor; 	 break;
		}
		
		//A single armor point adds 1% effective hp.
		int adjustedDmg = (int) Math.max(  dmg / (1 + (armor*0.01) ) , 1   );
		
		m_stats.mCurrHealth = Math.max(m_stats.mCurrHealth - adjustedDmg, 0);
		
		return adjustedDmg;
	}
	
	@Override
	/**
	 * Heals the unit
	 * @param heal	Amount to heal
	 */
	public int doHeal(int heal) {
		m_stats.mCurrHealth = Math.min(m_stats.mCurrHealth + heal, m_stats.mMaxHealth);
		return heal;
	}

	@Override
	/**
	 * Returns whether the unit is dead
	 */
	public boolean isDead() {
		return m_stats.mCurrHealth <= 0;
	}

	@Override
	/**
	 * Returns the unit's UnitStats
	 */
	public UnitStats getStats(){
		return m_stats;
	}



}
