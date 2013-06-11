package entity;

import game.player.Skill;

public interface IEntity {
	void render(double x, double y);
	int doDamage(int dmg);
	boolean isDead();
	
	int getCurrHealth();
	int getMaxHealth();
	
	public String getName();
	
	public Skill getBasicAttack();
}
