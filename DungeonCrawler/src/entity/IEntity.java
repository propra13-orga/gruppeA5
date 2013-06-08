package entity;

public interface IEntity {
	void render(double x, double y);
	void doDamage(int dmg);
	boolean isDead();
	
	int getCurrHealth();
	int getMaxHealth();
	
	public String getName();
}
