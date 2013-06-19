package game.effect;

import entity.Companion;
import game.skill.SingleTargetDmgSkill;

public class EquipEffectBasicAttack implements IEquipEffect {
	private SingleTargetDmgSkill m_damageSkill;

	@Override
	public void onEquip(Companion c) {
		c.setBasicAttack(m_damageSkill);
	}

	@Override
	public void onUnequip(Companion c) {
		c.setBasicAttack(null);
	}

	public EquipEffectBasicAttack(int damage){
		m_damageSkill = new SingleTargetDmgSkill("Attack", "", damage, 0);
	}

	@Override
	public String getDescription(){
		return "Deals " + m_damageSkill.getDamageAmount() + " damage.";
	}
}
