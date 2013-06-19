package game.effect;

import entity.Companion;

public class EquipEffectMaxMana implements IEquipEffect {
	private int m_manaBonus;

	@Override
	public void onEquip(Companion c) {
		c.getStats().mMaxMana += m_manaBonus;
		c.getStats().mCurrMana += m_manaBonus;
	}

	@Override
	public void onUnequip(Companion c) {
		int currMana = c.getStats().mCurrMana;
		c.getStats().mMaxMana -= m_manaBonus;
		c.getStats().mCurrMana = Math.max(currMana-m_manaBonus,0);
	}

	public EquipEffectMaxMana(int bonus){
		m_manaBonus = bonus;
	}
	
	@Override
	public String getDescription(){
		return "Increases mana capacity by " + m_manaBonus + ".";
	}
}
