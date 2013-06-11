package game.effect;

import entity.Companion;
import game.player.DamageSkill;
import game.player.Skill;

public class EquipEffectBasicAttack implements IEquipEffect {
	private Skill m_damageSkill;

	@Override
	public void onEquip(Companion c) {
		c.setBasicAttack(m_damageSkill);
	}

	@Override
	public void onUnequip(Companion c) {
		c.setBasicAttack(null);
	}

	public EquipEffectBasicAttack(int damage){
		m_damageSkill = new DamageSkill("Attack", damage);
	}
}
