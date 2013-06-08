package entity;

import game.player.DamageSkill;
import game.player.Skill;

import java.util.ArrayList;
import java.util.List;

import std.StdDraw;

public class Companion implements IEntity {
	private String m_name;
	private String m_baseAppearance;
	private int m_health;
	private int m_maxHealth;
	
	private Skill m_attackSkill;
	private ArrayList<Skill> m_skillNames = new ArrayList<>();
	
	public Skill getAttackSkill(){
		return m_attackSkill;
	}
	
	public List<Skill> getSkillList(){
		return m_skillNames;
	}

	public String getName(){
		return m_name;
	}
	
	public String getBaseAppearance(){
		return m_baseAppearance;
	}
	
	@Override
	public void render(double x, double y) {
	
		StdDraw.picture(x,y,"data/player/red.png");
		StdDraw.picture(x,y,"data/player/boromir.png");
		StdDraw.picture(x,y,m_baseAppearance);
		StdDraw.picture(x,y,"data/player/banded.png");
		StdDraw.picture(x,y,"data/player/leg_armor03.png");
	}	
	
	public static List<Companion> getDefaultCompanionList(){
		List<Companion> list = new ArrayList<>();
		Companion c;
		
		c = new Companion();
		c.m_name = "John";
		c.m_baseAppearance = "data/player/deep_elf_m.png";
		c.m_health = 100;
		c.m_maxHealth = 100;
		c.m_attackSkill = new DamageSkill("Attack", 35);
		c.m_skillNames.add( new DamageSkill("Strike", 50) );
		c.m_skillNames.add( new DamageSkill("Fireball", 100) );
		list.add(c);
		
		c = new Companion();
		c.m_name = "Bob";
		c.m_baseAppearance = "data/player/dwarf_m.png";
		c.m_health = 100;
		c.m_maxHealth = 100;
		c.m_attackSkill = new DamageSkill("Attack", 35);
		c.m_skillNames.add( new DamageSkill("DUNK!", 65) );
		list.add(c);
		
		return list;
	}

	@Override
	public void doDamage(int dmg) {
		m_health = Math.max(m_health - dmg, 0);
	}

	@Override
	public boolean isDead() {
		return m_health <= 0;
	}

	@Override
	public int getCurrHealth() {
		return m_health;
	}

	@Override
	public int getMaxHealth() {
		return m_maxHealth;
	}


}
