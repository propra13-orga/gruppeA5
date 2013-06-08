package game.combat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import monster.MonsterGroup;
import monster.MonsterType;

import entity.IEntity;
import entity.MonsterInstance;

public class EnemyParty extends Party {

	public void attackSelected(int dmg){
		IEntity e = m_entities.get(m_currTargeted);
		e.doDamage(dmg);
	
		if( e.isDead() ){
			m_entities.remove(m_currTargeted);
			m_currTargeted = 0;
		}
	}

	public void updateGroup(){
		Iterator<? extends IEntity> iter = m_entities.iterator();
		boolean reset = false;
		
	    while (iter.hasNext()) {
	    	IEntity e = iter.next();
	        if( e.isDead() ){
	        	iter.remove();
	        	reset = true;
	        }
	    }
	     //TODO: use method to reset to first targetable entity?
	     if(reset){
	    	 m_currTargeted = 0;
	    	 m_currHighlighted = 0;
	     }
	}
	
	public boolean isDefeated(){
		return m_entities.size() == 0;
	}

	public EnemyParty(double y, List<? extends IEntity> list) {
		super(y, list);
	}
	
	public static Party fromMonsterGroup( double y, MonsterGroup mg ){
		List<IEntity> entities = new ArrayList<>();

		for( MonsterType m : mg.getMonsters() ){
			entities.add( new MonsterInstance( m ) );
		}

		return new EnemyParty(y, entities);
	}

}
