package entity;

import game.skill.Skill;
import monster.MonsterType;
import std.StdDraw;

public class MonsterInstance implements IEntity {
	private MonsterType m_type;
	private UnitStats m_stats;
	
	public int getCurrHealth(){
		return m_stats.mCurrHealth;
	}
	
	public void setCurrHealth(int hp){
		m_stats.mCurrHealth = hp;
	}
	
	public boolean isDead(){
		return m_stats.mCurrHealth == 0;
	}
	public int doDamage(int dmg){
		//TODO: Calculate-in armor. Same as Companion
		m_stats.mCurrHealth = Math.max(m_stats.mCurrHealth - dmg, 0);
		return dmg;
	}
	
	@Override
	public int doHeal(int heal) {
		m_stats.mCurrHealth = Math.min(m_stats.mCurrHealth + heal, m_stats.mMaxHealth);
		return heal;
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
		m_stats = type.getFreshMonsterStats();
	}

	@Override
	public UnitStats getStats(){
		return m_stats;
	}

	@Override
	public Skill getBasicAttack() {
		return m_type.getBasicAttack();
	}
}
