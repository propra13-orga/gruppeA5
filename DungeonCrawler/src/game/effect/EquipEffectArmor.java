package game.effect;

import entity.Companion;

public class EquipEffectArmor implements IEquipEffect {
	private int m_armorRating;

	@Override
	public void onEquip(Companion c) {
		c.getStats().mArmor += m_armorRating;
	}

	@Override
	public void onUnequip(Companion c) {
		c.getStats().mArmor -= m_armorRating;
	}

	public EquipEffectArmor(int armorRating){
		m_armorRating = armorRating;
	}
}
