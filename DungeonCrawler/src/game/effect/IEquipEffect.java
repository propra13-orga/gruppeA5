package game.effect;

import entity.Companion;

public interface IEquipEffect {
	public void onEquip(Companion c);
	public void onUnequip(Companion c);
}
