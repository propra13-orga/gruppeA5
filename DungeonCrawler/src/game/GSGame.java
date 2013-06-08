package game;
import std.StdDraw;
import game.player.Player;
import gamestate.GameStates;
import gamestate.GlobalGameState;
import gamestate.IGameState;
import map.Map;


public class GSGame implements IGameState {
	Player m_player;
	Map m_map;
	GameInterface m_ui;
	
	boolean s_shutdown = false;

	@Override
	public void render() {	
		StdDraw.picture(0,0, "data/ui/background.png");
		
		StdDraw.setAlpha(0.33f);
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.filledRectangle(0,0,800,600);
		StdDraw.setAlpha(1.0f);
		
		m_map.render();
		m_player.render();
		m_ui.render();
	}

	@Override
	public void update() {
		
		m_player.update(m_map);
		
		if(m_player.isAlive() == false){
			GlobalGameState.setActiveGameState(GameStates.MAIN_MENU);
			
			resetPlayer();
		}
	}

	private void resetPlayer(){
	
		if( !m_map.loadLevel("data/outside.txt") ){
			s_shutdown = true;
			return;
		}
		
		m_player.setPosition( m_map.getCanvasX(7), m_map.getCanvasY(0) );
		
	}

	public GSGame(){
	
		m_ui = new GameInterface();
		m_player = new Player();
		
		//Erstellt die Map an der Position 144,44 (von oben links gesehen).
		//Die Zahlen sind so gewählt, dass die Map genau in der Mitte des Fensters ist.
		m_map = new Map(44,44, "data/tiles.txt");
				
		resetPlayer();
	}

	@Override
	public void onEnter() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub
		
	}

}
