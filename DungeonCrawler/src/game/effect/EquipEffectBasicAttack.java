package game.effect;

import entity.Companion;
import game.skill.DamageType;
import game.skill.SingleTargetDmgSkill;

public class EquipEffectBasicAttack implements IEquipEffect {
	private static final long serialVersionUID = 5390572911367141154L;
	
	private SingleTargetDmgSkill m_damageSkill;

	@Override
	public void onEquip(Companion c) {
		c.setBasicAttack(m_damageSkill);
	}

	@Override
	public void onUnequip(Companion c) {
		c.setBasicAttack(null);
	}

	public EquipEffectBasicAttack(int damage, DamageType type){
		m_damageSkill = new SingleTargetDmgSkill("Attack", "", damage, type, 0);
	}

	@Override
	public String getDescription(){
		return "Deals " + m_damageSkill.getDamageAmount() + " damage.";
	}
}
