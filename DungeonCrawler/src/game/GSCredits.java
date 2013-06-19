package game;

import java.awt.event.KeyEvent;

import std.StdDraw;
import std.StdIO;
import std.StdIO.KeyEventType;
import gamestate.GameStates;
import gamestate.GlobalGameState;
import gamestate.IGameState;

public class GSCredits implements IGameState, StdIO.IKeyListener {

	@Override
	public void onEnter() {
		StdIO.addKeyListener(this, KeyEventType.KeyPressed);		
	}

	@Override
	public void onExit() {
		StdIO.removeKeyListener(this, KeyEventType.KeyPressed);
	}

	@Override
	public void render() {
		StdDraw.pictureCentered(400, 300, "data/ui/Credits.png");
	}

	@Override
	public void update() {}

	@Override
	public void receiveEvent(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER){
			GlobalGameState.setActiveGameState(GameStates.MAIN_MENU);
		}
	}

}
