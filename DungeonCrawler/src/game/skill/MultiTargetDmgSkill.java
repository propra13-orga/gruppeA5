package game.skill;

import entity.IEntity;
import game.combat.CombatLog;
import game.combat.Party;

public class MultiTargetDmgSkill extends Skill {
	private int m_damageAmount;
	
	@Override
	public void applyEffect(IEntity source, Party party, CombatLog combatLog){
		
		deductMana(source);
	
		for( IEntity target : party.getListOfEntities() ){
			int dmg = target.doDamage(m_damageAmount);
			combatLog.add( source.getName() + " dealt " + dmg + " damage to " + target.getName() + "." );
			
			playAnimation( party.getLocOfEntity(target) );
		}
	}
	
	public int getDamageAmount(){
		return m_damageAmount;
	}
	
	public MultiTargetDmgSkill(String name, String desc, int damage, int manaCost){
		m_targetType = TargetType.ENEMY_MULTI;
		m_name = name;	//TODO: Put into superclass constructor, since every skill has a name
		m_damageAmount = damage;
		m_manaCost = manaCost;
		
		m_desc = desc;
		if(m_manaCost > 0) 
			m_desc += "\n\n" + m_manaCost + " mana.";
	}
}
