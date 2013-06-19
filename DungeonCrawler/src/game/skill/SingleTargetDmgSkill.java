package game.skill;

import entity.IEntity;
import game.combat.CombatLog;
import game.combat.Party;

public class SingleTargetDmgSkill extends Skill {
	private int m_damageAmount;
	
	@Override
	public void applyEffect(IEntity source, Party party, CombatLog combatLog){
		IEntity target = party.getCurrentTargeted();
		int dmg = target.doDamage(m_damageAmount);
		
		deductMana(source);
		
		playAnimation( party.getLocOfEntity(target) );
		
		//TODO: Use String builder? Investigate.
		combatLog.add( source.getName() + " dealt " + dmg + " damage to " + target.getName() + "." );
	}
	
	
	public int getDamageAmount(){
		return m_damageAmount;
	}
	
	public SingleTargetDmgSkill(String name, String desc, int damage, int manaCost){
		m_targetType = TargetType.ENEMY_SINGLE;
		m_name = name;	//TODO: Put into superclass constructor, since every skill has a name
		m_damageAmount = damage;
		m_manaCost = manaCost;
		
		m_desc = desc;
		if(m_manaCost > 0) 
			m_desc += "\n\n" + m_manaCost + " mana.";
	}
}
