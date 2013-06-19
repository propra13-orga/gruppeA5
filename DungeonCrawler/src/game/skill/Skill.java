package game.skill;

import java.util.HashMap;

import std.anim.FadeAnim;
import std.anim.GlobalAnimQueue;

import entity.IEntity;
import game.Point;
import game.combat.CombatLog;
import game.combat.Party;

public class Skill {
	protected String m_name;
	protected TargetType m_targetType;
	protected String m_desc;
	protected int m_manaCost = 0;
	protected String m_sfx = "data/cut.png";
	
	public int getManaCost(){
		return m_manaCost;
	}
	
	public String getDescription(){
		return m_desc;
	}
	
	public TargetType getTargetType(){
		return m_targetType;
	}
	
	public String getName(){
		return m_name;
	}
	
	public void setSfx(String imagePath){
		m_sfx = imagePath;
	}
	
	protected void playAnimation(Point p){
		if(m_sfx != ""){
			GlobalAnimQueue.playAnimation(  new FadeAnim(p.x, p.y, m_sfx, 50) );
		}
	}
	
	protected void deductMana(IEntity source){
		source.getStats().mCurrMana -= m_manaCost; //TODO: Put this stuff into Stats methods
	}
	
	public void applyEffect(IEntity source, Party party, CombatLog combatLog){
		System.out.println("Error. Skill.applyEffect() should not be called.");
	}
	
	
	private static HashMap<String, Skill> s_skills = new HashMap<>();
	
	public static Skill getSkill(String name){
		return s_skills.get(name.toLowerCase());
	}
	
	private static void loadSkills(){
		SkillLoader sl = new SkillLoader();
		sl.load("data/skills.txt");
		s_skills = sl.getSkillList();
	}
	
	static{
		loadSkills();
	}
}
