package monster;

import java.util.ArrayList;
import java.util.List;


import game.HitBox;
import std.StdDraw;
import map.Map;

public class MonsterGroup {
	private double m_x;
	private double m_y;
	public String mPath;
	HitBox m_hitBox = new HitBox(4, 4, 28, 28);
	
	private ArrayList<MonsterType> m_monsters = new ArrayList<>();
	
	public void setPosition(double x, double y){
		m_x = x;
		m_y = y;
		m_hitBox.setPosition(m_x, m_y);
	}
	
	public double getX(){
		return m_x;
	}
	
	public double getY(){
		return m_y;
	}
	
	public void render(Map map){
		StdDraw.picture(m_x, m_y, mPath);
		
		
		m_hitBox.render();
	}
	
	public List<MonsterType> getMonsters(){
		return m_monsters;
	}
	
	public MonsterGroup(double x, double y, String path){
		setPosition(x,y);
		mPath = path;
		
		//TODO: Meep meep
		if( path.equals("data/monsters/redback.png") ){
			m_monsters.add( MonsterType.get("Spider") );
			m_monsters.add( MonsterType.get("Spider") );
			m_monsters.add( MonsterType.get("Spider") );
		}else{
			m_monsters.add( MonsterType.get("ShiningEye") );
			m_monsters.add( MonsterType.get("UnseenHorror") );
			m_monsters.add( MonsterType.get("ShiningEye") );
		}
	}
	
}
