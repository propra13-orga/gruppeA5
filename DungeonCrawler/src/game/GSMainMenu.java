package game;
import gamestate.GameStates;
import gamestate.GlobalGameState;
import gamestate.IGameState;

import java.awt.event.KeyEvent;
import std.StdDraw;
import std.StdIO;
import std.StdIO.KeyEventType;


public class GSMainMenu implements IGameState, StdIO.IKeyListener {

	double y=223;
	
	/**
	 * Draws Buttons and a rectangle for selction on the screen
	 */
	@Override
	public void render() {
		StdDraw.setPenColor( StdDraw.BLACK );
		StdDraw.picture(350,225, "data/ui/NewGameBtn.png");
		StdDraw.picture(350,250, "data/ui/HostMultiplayerBtn.png");
		StdDraw.picture(350,275, "data/ui/JoinMultiplayerBtn.png");
		StdDraw.picture(350,300, "data/ui/CreditsBtn.png");
		StdDraw.picture(350, 325, "data/ui/QuitBtn.png");
		StdDraw.setPenColor(StdDraw.WHITE);
		StdDraw.rectangle(350,y,150,25);
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
		if( (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) && y<= 298){
			y += 25;
		}
		if( (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) && y>= 248){
			y -= 25;
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
		
		if ( y == 223 ){
			if ( e.getKeyCode() == KeyEvent.VK_ENTER){
				GSGame.getInstance().startGame();
				GlobalGameState.setActiveGameState(GameStates.GAME);
			}
		}
		if ( y == 248 ){
			if ( e.getKeyCode() == KeyEvent.VK_ENTER ){}
		}
		if ( y == 273){
			if ( e.getKeyCode() == KeyEvent.VK_ENTER){}
		}
		if ( y == 298 ){
			if ( e.getKeyCode() == KeyEvent.VK_ENTER ){
				GlobalGameState.setActiveGameState(GameStates.CREDITS);
			}
		}
		if ( y == 323 ){
			if ( e.getKeyCode() == KeyEvent.VK_ENTER ){
				System.exit(0);
			}
		}
	}
}