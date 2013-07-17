package game.checkpoint;

import entity.Companion;
import game.player.Player;
import map.Map;


public class Checkpoint {
	private static String m_currentlevelName;
	private static double m_playerX;
	private static double m_playerY;

	/**
	 * Saves the current player position and loaded level. Only the last saved data will be kept.
	 */
	public static void save(){
		m_currentlevelName = Map.getInstance().getCurrentLevelname();
		m_playerX = Player.getInstance().getX();
		m_playerY = Player.getInstance().getY();
	}

	/**
	 * Restores the stored player position and level. Also restores all players to full hp.
	 */
	public static void load(){
		Map.getInstance().loadLevel(m_currentlevelName);
		Player.getInstance().setPosition(m_playerX, m_playerY);
		
		for( Companion c : Player.getInstance().getCompanions() ){
			c.restoreToFull();
		}
	}
	
	private Checkpoint(){}
}
