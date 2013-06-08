package game.shop;
import java.awt.event.KeyEvent;

import std.StdDraw;
import std.StdIO;
import std.StdIO.KeyEventType;
import gamestate.GameStates;
import gamestate.GlobalGameState;
import gamestate.IGameState;


public class GSShop implements IGameState, StdIO.IKeyListener {


	@Override
	public void render() {
		StdDraw.text(400,300, "SHOP MENU HERE");
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void receiveEvent(KeyEvent e) {
		if( e.getKeyCode() == KeyEvent.VK_ENTER ){
			GlobalGameState.setActiveGameState( GameStates.GAME );
		}
	}
	
	@Override
	public void onEnter() {
		StdIO.addKeyListener(this, KeyEventType.KeyReleased);
	}

	@Override
	public void onExit() {
		StdIO.removeKeyListener(this, KeyEventType.KeyReleased);
	}
}
