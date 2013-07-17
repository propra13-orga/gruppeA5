package game.skill;

import entity.IEntity;
import game.combat.CombatLog;
import game.combat.Party;

public class MultiTargetDmgSkill extends Skill {
	private static final long serialVersionUID = 4931605622816327303L;
	
	private int m_damageAmount;
	private DamageType m_damageType;
	
	@Override
	public void applyEffect(IEntity source, Party party, CombatLog combatLog){
		
		deductMana(source);
	
		for( IEntity target : party.getListOfEntities() ){
			int dmg = target.doDamage(m_damageAmount, m_damageType);
			combatLog.add( source.getName() + " dealt " + dmg + " " + m_damageType.toString().toLowerCase() + " damage to " + target.getName() + "." );
			
			playAnimation( party.getLocOfEntity(target) );
		}
	}
	
	public int getDamageAmount(){
		return m_damageAmount;
	}
	
	/**
	 * Constructor of multi-target dmg skill
	 * @param name	name of skill
	 * @param desc	description of skill
	 * @param damage	amount of damage
	 * @param type		damage type
	 * @param manaCost	mana cost
	 */
	public MultiTargetDmgSkill(String name, String desc, int damage, DamageType type, int manaCost){
		m_targetType = TargetType.ENEMY_MULTI;
		m_name = name;	//TODO: Put into superclass constructor, since every skill has a name
		m_damageAmount = damage;
		m_manaCost = manaCost;
		m_damageType = type;
		
		m_desc = desc;
		if(m_manaCost > 0) 
			m_desc += "\n\n" + m_manaCost + " mana.";
	}
}
