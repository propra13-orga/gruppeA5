package game;
import gamestate.GameStates;
import gamestate.GlobalGameState;
import gamestate.IGameState;

import java.awt.event.KeyEvent;
import std.StdDraw;
import std.StdIO;
import std.StdIO.KeyEventType;


public class GSMainMenu implements IGameState, StdIO.IKeyListener {

	private int m_y = 0;
	
	/**
	 * Draws Buttons and a rectangle for selction on the screen
	 */
	@Override
	public void render() {
		StdDraw.setPenColor( StdDraw.WHITE );
		StdDraw.picture(350,225, "data/ui/MenuNewGame.png");
		StdDraw.picture(330,250, "data/ui/HostMultiplayerBtn.png");
		StdDraw.picture(330,275, "data/ui/JoinMultiplayerBtn.png");
		StdDraw.picture(350,300, "data/ui/MenuSettings.png");
		StdDraw.picture(350,325, "data/ui/MenuCredits.png");
		StdDraw.picture(350,350, "data/ui/MenuQuit.png");
		StdDraw.setPenColor(StdDraw.WHITE);
		StdDraw.rectangle(300,223 + m_y*25,200,25);
	}

	@Override
	public void update() {}

	/**
	 * when entering MENU-gameState:<br>
	 * - the KeyListener is added
	 */
	@Override
	public void onEnter() {
		StdIO.addKeyListener(this, KeyEventType.KeyPressed);
	}

	/**
	 * when leaving MENU-gameState:<br>
	 * - the KeyListener is removed
	 */
	@Override
	public void onExit() {
		StdIO.removeKeyListener(this, KeyEventType.KeyPressed);
	}
	
	/**
	 * waits for a keyboard input by the user<br>
	 * allows the following keys:<br>
	 * "W", "S", "UP", "DOWN"
	 * depending on the pressed key the rectangle moves
	 * 
	 * @param e a KeyEvent
	 */
	public void receiveEvent(KeyEvent e) {
		if( (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) && m_y<6){
			m_y+=1;
		}
		if( (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) && m_y>0){
			m_y-=1;
		}
		getMenuState(e);
	}
	
	/**
	 * waits for a keyboard input by the user <br>
	 * allows the "Enter"-Key
	 * depending on the rectangle position one of the following options kicks in:<br>
	 * - the MENU-gameState is left<br>
	 * - the GAME-gameState is entered<br>
	 * - the CREDITS-gameState is entered<br>
	 * - the program terminates<br>
	 * 
	 * @param e a KeyEvent
	 */
	public void getMenuState(KeyEvent e){
		
		if ( m_y == 0 ){
			if ( e.getKeyCode() == KeyEvent.VK_ENTER){
				GSGame.getInstance().startGameSingle();
				GlobalGameState.setActiveGameState(GameStates.GAME);
			}
		}
		if ( m_y == 1 ){
			if ( e.getKeyCode() == KeyEvent.VK_ENTER){
				//GSGame.getInstance().startGameMulti(true);
				GlobalGameState.setActiveGameState(GameStates.MULTI_HOST);
			}
		}
		if ( m_y == 2 ){
			if ( e.getKeyCode() == KeyEvent.VK_ENTER){
				//GSGame.getInstance().startGameMulti(false);
				GlobalGameState.setActiveGameState(GameStates.MULTI_JOIN);
			}
		}
		if ( m_y == 3 ){
			if ( e.getKeyCode() == KeyEvent.VK_ENTER ){
				StdDraw.text(400,300, "Settings");
			}
		}
		if ( m_y == 4 ){
			if ( e.getKeyCode() == KeyEvent.VK_ENTER ){
				GlobalGameState.setActiveGameState(GameStates.CREDITS);
			}
		}
		if ( m_y == 5 ){
			if ( e.getKeyCode() == KeyEvent.VK_ENTER ){
				System.exit(0);
			}
		}
	}
}