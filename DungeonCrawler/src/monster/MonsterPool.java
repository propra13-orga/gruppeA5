package monster;

import game.HitBox;

import java.util.ArrayList;
import java.util.Random;

import map.Map;

public class MonsterPool {
	private ArrayList<MonsterGroup> m_monsters = new ArrayList<>();
	private Random m_rand = new Random();
	
	public MonsterGroup collisionAt( HitBox hitBox ){

		for( MonsterGroup m : m_monsters ){
			if( HitBox.collidesWith(m.m_hitBox, hitBox) ){
				return m;
			}
		}

		return null;
		
	}
	
	public void issueRandomWanderOrders(){
		Map map = Map.getInstance();
	
		for( MonsterGroup m : m_monsters ){
			if( m.isAIControlled() && m_rand.nextInt(100) > 97 && m.getCurrentOrder() != MonsterGroup.MonsterOrder.OrderType.MOVE ){
				int x = map.getGridX(m.getX()+5);
				int y = map.getGridY(m.getY()+5);
				int r = m_rand.nextInt(4);
				
				switch(r){
				case 0: x+=1; break;
				case 1: x-=1; break;
				case 2: y+=1; break;
				case 3: y-=1; break;
				}
				if(map.isPathable(x,y))
					m.issueOrder( map.getCanvasX(x), map.getCanvasY(y) );
			}
		}
	}
	
	public void updateAll(){
		for( MonsterGroup m : m_monsters ) {
			m.update();
		}
	}
	
	public void renderAll(){
		for( MonsterGroup m : m_monsters ) {
			m.render();
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
