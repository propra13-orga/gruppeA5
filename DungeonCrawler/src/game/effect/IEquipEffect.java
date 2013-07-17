package game.effect;

import entity.Companion;

public interface IEquipEffect extends java.io.Serializable {
	/**
	 * Applies the equipment effect to the supplied Companion 
	 * @param c		Companion to be affected
	 */
	public void onEquip(Companion c);
	
	/**
	 * Removes the equip effect from the supplied Companion
	 * @param c
	 */
	public void onUnequip(Companion c);
	
	/**
	 * Returns the effect's description as a String
	 * @return
	 */
	public String getDescription();
}
