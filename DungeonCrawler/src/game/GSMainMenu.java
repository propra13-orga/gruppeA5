package game;
import gamestate.GameStates;
import gamestate.GlobalGameState;
import gamestate.IGameState;

import java.awt.event.KeyEvent;
import std.StdDraw;
import std.StdIO;
import std.StdIO.KeyEventType;


public class GSMainMenu implements IGameState, StdIO.IKeyListener {

	@Override
	public void render() {
		StdDraw.setPenColor( StdDraw.BLACK );
		
		//Toller Button für Start Game. So können später alle Btns aussehen, wenn noch genug Zeit ist.
		StdDraw.pictureCentered(400, 200, "data/ui/BtnNewGame.png");
	}

	@Override
	public void update() {
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
		if(e.getKeyCode() == KeyEvent.VK_ENTER){
			GSGame.getInstance().startGame();
			GlobalGameState.setActiveGameState(GameStates.GAME);
		}else if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
			System.exit(0);
		}
	}

}
