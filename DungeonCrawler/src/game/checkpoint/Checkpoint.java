package game.checkpoint;

import entity.Companion;
import game.player.Player;
import map.Map;


public class Checkpoint {
	private static String m_currentlevelName;
	private static double m_playerX;
	private static double m_playerY;


	public static void save(){
		m_currentlevelName = Map.getInstance().getCurrentLevelname();
		m_playerX = Player.getInstance().getX();
		m_playerY = Player.getInstance().getY();
	}

	public static void load(){
		Map.getInstance().loadLevel(m_currentlevelName);
		Player.getInstance().setPosition(m_playerX, m_playerY);
		
		for( Companion c : Player.getInstance().getCompanions() ){
			c.restoreToFull();
		}
	}
	
	private Checkpoint(){}
}
