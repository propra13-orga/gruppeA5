package game.skill;

import entity.IEntity;
import entity.UnitStats;
import game.combat.CombatLog;
import game.combat.Party;

public class SelfManaSkill extends Skill {
	private int m_manaAmount;
	
	@Override
	public void applyEffect(IEntity source, Party party, CombatLog combatLog){
		UnitStats us = source.getStats();
		us.mCurrMana = Math.min(us.mCurrMana+m_manaAmount, us.mMaxMana );
		
		deductMana(source);
		
		playAnimation( party.getLocOfEntity(source) );
		
		//TODO: Use String builder? Investigate.
		combatLog.add( source.getName() + " restores " + m_manaAmount + " mana points" );
	}
	
	
	public int getDamageAmount(){
		return m_manaAmount;
	}
	
	public SelfManaSkill(String name, String desc, int mana, int manaCost){
		m_targetType = TargetType.SELF;
		m_name = name;	//TODO: Put into superclass constructor, since every skill has a name
		m_manaAmount = mana;
		m_manaCost = manaCost;
		
		m_desc = desc;
		if(m_manaCost > 0) 
			m_desc += "\n\n" + m_manaCost + " mana.";
	}
}
