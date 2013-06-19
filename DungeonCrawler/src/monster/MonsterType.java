package monster;

import entity.UnitStats;
import game.skill.Skill;

import java.util.HashMap;

public class MonsterType {
	private int m_maxHealth;
	private int m_maxMana;
	private String m_path;
	private String m_name;
	private Skill m_basicAttack;
	
	public int getMaxHealth() {
		return m_maxHealth;
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
		UnitStats s = new UnitStats();
		s.mMaxHealth = m_maxHealth;
		s.mCurrHealth = m_maxHealth;
		s.mMaxMana = m_maxMana;
		s.mCurrMana = m_maxMana;
		return s;
	}
	
	MonsterType(int maxHP, int maxMana, String path, String name, Skill basicAttack){
		m_maxHealth = maxHP;
		m_maxMana = maxMana;
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
