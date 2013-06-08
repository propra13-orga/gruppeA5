package game.player;

import entity.IEntity;

public class Skill {
	protected String m_name;
	
	public String getName(){
		return m_name;
	}
	
	public String applyEffect(IEntity source, IEntity target){
		return "";
	}
}
