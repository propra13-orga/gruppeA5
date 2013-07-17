package monster;

import entity.IEntity;
import game.combat.EnemyParty;
import game.combat.Party;

import java.util.ArrayList;

public class EnemyPlayer extends MonsterGroup {

	private static EnemyPlayer s_theOneAndOnly = null;
	
	public static EnemyPlayer getEnemyPlayer(){
		return s_theOneAndOnly;
	}
	
	private ArrayList<IEntity> m_entities = null;
	
	public ArrayList<IEntity> getEntities(){
		return m_entities;
	}
	
	public void setEntities(ArrayList<IEntity> e){
		m_entities = e;
	}

	public boolean isAIControlled(){
		return false;
	}
	
	public void synchronizeWithPosition( double x, double y ){
		double deltaX = x - getX();
		double deltaY = y - getY();
		
		if( deltaX*deltaX + deltaY*deltaY >= 400 ){
			setPosition(x,y);
		}
	}

	public EnemyPlayer(double x, double y, String path) {
		super(x, y, path);
		MOVE_SPEED = 3.f;
		s_theOneAndOnly = this;
	}

	public Party createParty( double y ){
		return new EnemyParty(y, m_entities);
	}

}
