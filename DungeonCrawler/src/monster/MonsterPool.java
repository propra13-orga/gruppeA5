package monster;

import game.HitBox;

import java.util.ArrayList;

import map.Map;

public class MonsterPool {
	private ArrayList<MonsterGroup> m_monsters = new ArrayList<>();
	
	public MonsterGroup collisionAt( HitBox hitBox ){

		for( MonsterGroup m : m_monsters ){
			if( HitBox.collidesWith(m.m_hitBox, hitBox) ){
				return m;
			}
		}

		return null;
		
	}
	
	public void renderAll(Map map){
		for( MonsterGroup m : m_monsters ) {
			m.render(map);
		}
	}
	
	public void addMonster(MonsterGroup m){
		m_monsters.add(m);
	}
	
	public void removeMonster(MonsterGroup m){
		m_monsters.remove(m);
	}
	
	public MonsterPool(){
	}
}
