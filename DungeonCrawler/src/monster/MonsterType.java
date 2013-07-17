package monster;

import entity.UnitStats;
import game.skill.Skill;

import java.util.HashMap;

public class MonsterType {
	private String m_path;
	private String m_name;
	private Skill m_basicAttack;
	private UnitStats m_stats;
		
	public int getMaxHealth() {
		return m_stats.mMaxHealth;
	}

	public String getPath() {
		return m_path;
	}

	public String getName() {
		return m_name;
	}
	
	public Skill getBasicAttack(){
		return m_basicAttack;
	}
	
	public UnitStats getFreshMonsterStats(){
		return new UnitStats(m_stats);
	}
	
	MonsterType(UnitStats stats, String path, String name, Skill basicAttack){
		m_stats = stats;
		m_stats.mCurrHealth = m_stats.mMaxHealth;
		m_stats.mCurrMana = m_stats.mMaxMana;
		
		m_path = path;
		m_name = name;
		m_basicAttack = basicAttack;
	}
	
	private static HashMap<String, MonsterType> s_monsterTypes = new HashMap<>();
	
	static MonsterType get(String name){
		return s_monsterTypes.get(name);
	}
	
	private static void loadMonsterTypes(){
		MonsterTypeLoader mtl = new MonsterTypeLoader();
		mtl.load("data/monstertypes.txt");
		s_monsterTypes = mtl.getMonsterTypes();
	}
	
	static{
		loadMonsterTypes();
	}
}
