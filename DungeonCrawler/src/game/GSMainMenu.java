package game;
import gamestate.GameStates;
import gamestate.GlobalGameState;
import gamestate.IGameState;

import java.awt.event.KeyEvent;
import std.StdDraw;
import std.StdIO;


public class GSMainMenu implements IGameState {

	@Override
	public void render() {
		StdDraw.setPenColor( StdDraw.BLACK );
		
		//Toller Button für Start Game. So können später alle Btns aussehen, wenn noch genug Zeit ist.
		StdDraw.pictureCentered(400, 200, "data/ui/BtnNewGame.png");
	}

	@Override
	public void update() {
	
		if( StdIO.isKeyPressed( KeyEvent.VK_ESCAPE ) ){
			System.exit(0);
		}
	
		if( StdIO.isKeyPressed( KeyEvent.VK_ENTER ) ){
			GSGame.getInstance().startGame();
			GlobalGameState.setActiveGameState(GameStates.GAME);
		}
	}

	@Override
	public void onEnter() {
	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub
		
	}

}
