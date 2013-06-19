package game.skill;

import entity.IEntity;
import game.combat.CombatLog;
import game.combat.Party;

public class SingleTargetHealSkill extends Skill {
	private int m_healAmount;
	
	@Override
	public void applyEffect(IEntity source, Party party, CombatLog combatLog){
		IEntity target = party.getCurrentTargeted();
		int dmg = target.doHeal(m_healAmount);
		
		deductMana(source);
		
		playAnimation( party.getLocOfEntity(target) );
		
		//TODO: Use String builder? Investigate.
		combatLog.add( source.getName() + " heals " + target.getName() + " for " + dmg + " hit points" );
	}
	
	
	public int getDamageAmount(){
		return m_healAmount;
	}
	
	public SingleTargetHealSkill(String name, String desc, int heal, int manaCost){
		m_targetType = TargetType.ALLY_SINGLE;
		m_name = name;	//TODO: Put into superclass constructor, since every skill has a name
		m_healAmount = heal;
		m_manaCost = manaCost;
		
		m_desc = desc;
		if(m_manaCost > 0) 
			m_desc += "\n\n" + m_manaCost + " mana.";
	}
}
