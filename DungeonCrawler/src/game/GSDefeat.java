package game;

import java.awt.event.KeyEvent;

import std.StdDraw;
import std.StdIO;
import std.StdIO.KeyEventType;
import game.checkpoint.Checkpoint;
import gamestate.GameStates;
import gamestate.GlobalGameState;
import gamestate.IGameState;

public class GSDefeat implements IGameState, StdIO.IKeyListener {
	private static final int STARTING_LIVES = 3;
	private int m_numOfLives = STARTING_LIVES;

	@Override
	public void onEnter() {
		StdIO.addKeyListener(this, KeyEventType.KeyReleased);
	}
	
	@Override
	public void onExit() {
		StdIO.removeKeyListener(this, KeyEventType.KeyReleased);
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.text(400, 300, "You died!");
		StdDraw.text(400, 320, m_numOfLives + " lives remaining.");
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void receiveEvent(KeyEvent e) {
		if(e.getKeyCode() != KeyEvent.VK_ENTER){
			return;
		}
		
		if(m_numOfLives > 0){
			m_numOfLives--;
			Checkpoint.load();
			GlobalGameState.setActiveGameState(GameStates.GAME);
		}else{
			m_numOfLives = STARTING_LIVES;
			GlobalGameState.setActiveGameState(GameStates.MAIN_MENU);
		}
	}

}
