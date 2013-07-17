package entity;

import game.skill.Skill;
import game.skill.DamageType;

public interface IEntity extends java.io.Serializable {
	/**
	 * Renders the entity to the screen
	 * @param x	x coordinate
	 * @param y	y coordinate
	 */
	void render(double x, double y);
	
	/**
	 * Deals damage to the entity
	 * @param dmg	amount of damage
	 * @param type	damage type
	 * @return	actual damage dealt (after armor mitigation)
	 */
	int doDamage(int dmg, DamageType type);
	
	/**
	 * Heals the player
	 * @param heal	amount of healing
	 * @return	actual healing done
	 */
	int doHeal(int heal);
	
	/**
	 * Returns whether the unit is dead
	 * @returnd
	 */
	boolean isDead();
	
	/**
	 * Returns the unit's UnitStats
	 * @return
	 */
	UnitStats getStats();
	
	/**
	 * Returns the unit's name
	 * @return
	 */
	public String getName();
	
	/**
	 * Returns the unit's basic attack skill
	 * @return
	 */
	public Skill getBasicAttack();
}
