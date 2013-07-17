package entity;

import game.skill.Skill;
import game.skill.DamageType;

public interface IEntity extends java.io.Serializable {
	void render(double x, double y);
	
	int doDamage(int dmg, DamageType type);
	int doHeal(int heal);
	
	boolean isDead();
	
	UnitStats getStats();
	
	public String getName();
	
	public Skill getBasicAttack();
}
