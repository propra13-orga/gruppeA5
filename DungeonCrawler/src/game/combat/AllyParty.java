package game.combat;

import java.util.List;

import entity.IEntity;

public class AllyParty extends Party {
	
	public boolean isDefeated(){
		for( IEntity e : m_entities)
			if(!e.isDead()) return false; 
			
		return true;
	}
	
	public void updateGroup(){
		if(m_entities.get(m_currTargeted).isDead()){
			targetNext();
		}
	}

	public void targetNext(){
		int size = m_entities.size();
		
		m_currTargeted++;
		while( m_currTargeted < size ){
			if(m_entities.get(m_currTargeted).isDead() == false){
				return;
			}
			m_currTargeted++;
		}
	
		m_currTargeted = 0;
		while( m_currTargeted < size ){
			if(m_entities.get(m_currTargeted).isDead() == false){
				return;
			}
			m_currTargeted++;
		}
	}
	
	public void targetPrev(){
		int size = m_entities.size();
		
		m_currTargeted--;
		while( m_currTargeted > 0 ){
			if(m_entities.get(m_currTargeted).isDead() == false){
				return;
			}
			m_currTargeted--;
		}
	
		m_currTargeted = size-1;
		while( m_currTargeted > 0 ){
			if(m_entities.get(m_currTargeted).isDead() == false){
				return;
			}
			m_currTargeted--;
		}
	}
	
	public void resetHighlight(){
		m_currHighlighted = -1;
		
		highlightNext();
	}

	public boolean highlightNext(){
		m_currHighlighted++;
		while( m_currHighlighted < m_entities.size() ){
			if(m_entities.get(m_currHighlighted).isDead() == false){
				System.out.println(m_currHighlighted);
				return true;
			}
			m_currHighlighted++;
		}
	
		return false;
	}


	public AllyParty(double y, List<? extends IEntity> list) {
		super(y, list);
	}



}
