package game;
import java.awt.event.KeyEvent;

import std.StdDraw;
import std.StdIO;
import std.StdIO.KeyEventType;
import game.checkpoint.Checkpoint;
import game.player.Player;
import gamestate.GameStates;
import gamestate.GlobalGameState;
import gamestate.IGameState;
import map.Map;


public class GSGame implements IGameState, StdIO.IKeyListener {
	private Player m_player;
	private Map m_map;
	private GameInterface m_ui;
	private static GSGame m_this;
	
	boolean s_shutdown = false;

	public static GSGame getInstance(){
		return m_this;
	}

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
		
		Map.getInstance().getMonsterPool().updateAll();
		Map.getInstance().getMonsterPool().issueRandomWanderOrders();
		
		if(m_player.isAlive() == false){
			System.out.println("Error! Player alive check not passed in GSGame.");
		}
		
	}

	private void resetPlayer(){
	
		if( !m_map.loadLevel("data/outside1.txt") ){
			s_shutdown = true;
			return;
		}
		
		m_player.setPosition( m_map.getCanvasX(3), m_map.getCanvasY(11) );
		
	}
	
	public void startGame(){
		m_ui = new GameInterface();
		m_player = new Player();
		
		//Erstellt die Map an der Position 144,44 (von oben links gesehen).
		//Die Zahlen sind so gewählt, dass die Map genau in der Mitte des Fensters ist.
		m_map = new Map(44,44, "data/tiles.txt");
				
		resetPlayer();
		Checkpoint.save();
	}

	public GSGame(){
		m_this = this;
	}


	@Override
	public void onEnter() {
		StdIO.addKeyListener(this, KeyEventType.KeyReleased);
	}

	@Override
	public void onExit() {
		StdIO.removeKeyListener(this, KeyEventType.KeyReleased);
	}

	@Override
	public void receiveEvent(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_I){
			GlobalGameState.setActiveGameState(GameStates.INVENTORY);
		}else if(e.getKeyCode() == KeyEvent.VK_ENTER){
			m_player.attempInteraction();
		}
	}
}
