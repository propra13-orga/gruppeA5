package monster;

import entity.UnitStats;
import game.player.DamageSkill;
import game.player.Skill;

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
	
	private MonsterType(int maxHP, int maxMana, String path, String name, Skill basicAttack){
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
	
	static{
		s_monsterTypes.put("Spider", new MonsterType( 70,0, "data/monsters/redback.png", "Black Spider", new DamageSkill("Attack", 15) ) );
		s_monsterTypes.put("ShiningEye", new MonsterType( 100,0, "data/monsters/shining_eye.png", "Shining Eye", new DamageSkill("Attack", 18) ) );
		s_monsterTypes.put("UnseenHorror", new MonsterType( 150,50, "data/monsters/unseen_horror.png", "Unseen Horror", new DamageSkill("Attack", 22) ) );
	}
}
