package entity;

import game.skill.Skill;

public interface IEntity {
	void render(double x, double y);
	
	int doDamage(int dmg);
	int doHeal(int heal);
	
	boolean isDead();
	
	UnitStats getStats();
	
	public String getName();
	
	public Skill getBasicAttack();
}
