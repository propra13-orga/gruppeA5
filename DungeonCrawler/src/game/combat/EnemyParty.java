package game.combat;

import java.util.Iterator;
import java.util.List;

import entity.IEntity;

public class EnemyParty extends Party {

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

}
