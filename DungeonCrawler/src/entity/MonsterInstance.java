package entity;

import monster.MonsterType;
import std.StdDraw;

public class MonsterInstance implements IEntity {
	private MonsterType m_type;
	private int m_currentLife;
	
	public int getCurrHealth(){
		return m_currentLife;
	}
	
	public void setCurrHealth(int hp){
		m_currentLife = hp;
	}
	
	public boolean isDead(){
		return m_currentLife == 0;
	}
	public void doDamage(int dmg){
		m_currentLife = Math.max(m_currentLife - dmg, 0);
	}
	public MonsterType getType(){
		return m_type;
	}
	
	public String getName(){
		return m_type.getName();
	}
	
	public void render(double x, double y){
		StdDraw.picture(x, y, m_type.getPath());
	}
	
	public MonsterInstance(MonsterType type){
		m_type = type;
		m_currentLife = type.getMaxHealth();
	}

	@Override
	public int getMaxHealth() {
		return  m_type.getMaxHealth();
	}
}
