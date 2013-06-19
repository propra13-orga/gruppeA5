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
	
	@Override
	public void render() {
		StdDraw.setPenColor( StdDraw.BLACK );
		
		//Toller Button für Start Game. So können später alle Btns aussehen, wenn noch genug Zeit ist.
		//StdDraw.pictureCentered(400, 200, "data/ui/BtnNewGame.png");
		StdDraw.picture(350,225, "data/ui/MenuNewGame.png");
		StdDraw.picture(350,250, "data/ui/MenuSettings.png");
		StdDraw.picture(350,275, "data/ui/MenuCredits.png");
		StdDraw.picture(350,300, "data/ui/MenuQuit.png");
		StdDraw.setPenColor(StdDraw.WHITE);
		StdDraw.rectangle(325,y,150,25);
	}

	@Override
	public void update() {}

	@Override
	public void onEnter() {
		StdIO.addKeyListener(this, KeyEventType.KeyPressed);
	}

	@Override
	public void onExit() {
		StdIO.removeKeyListener(this, KeyEventType.KeyPressed);
	}
	
	public void receiveEvent(KeyEvent e) {
		if( (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) && y<= 273){
			y += 25;
		}
		if( (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) && y>= 248){
			y -= 25;
		}
		getMenuState(e);
	}
	
	
	public void getMenuState(KeyEvent e){
		
		if ( y == 223 ){
			if ( e.getKeyCode() == KeyEvent.VK_ENTER){
				GSGame.getInstance().startGame();
				GlobalGameState.setActiveGameState(GameStates.GAME);
			}
		}
		if ( y == 248 ){
			if ( e.getKeyCode() == KeyEvent.VK_ENTER ){
				StdDraw.text(400,300, "Settings");
			}
		}
		if ( y == 273 ){
			if ( e.getKeyCode() == KeyEvent.VK_ENTER ){
				GlobalGameState.setActiveGameState(GameStates.CREDITS);
			}
		}
		if ( y == 298 ){
			if ( e.getKeyCode() == KeyEvent.VK_ENTER ){
				System.exit(0);
			}
		}
	}
}