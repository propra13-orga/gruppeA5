package game.player;

import entity.IEntity;

public class DamageSkill extends Skill {
	private int m_damageAmount;
	
	@Override
	public String applyEffect(IEntity source, IEntity target){
		int dmg = target.doDamage(m_damageAmount);
		//TODO: Use String builder? Investigate.
		return source.getName() + " dealt " + dmg + " damage to " + target.getName() + ".";
	}
	
	public DamageSkill(String name, int damage){
		m_name = name;	//TODO: Put into superclass constructor, since every skill has a name
		m_damageAmount = damage;
	}
}
