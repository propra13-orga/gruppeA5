package game.effect;

import entity.Companion;

public interface IEquipEffect extends java.io.Serializable {
	public void onEquip(Companion c);
	public void onUnequip(Companion c);
	public String getDescription();
}
