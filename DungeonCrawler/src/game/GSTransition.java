package game;

import std.anim.AnimBase;
import std.anim.BGFadeAnim;
import std.anim.GlobalAnimQueue;
import std.anim.AnimBase.AnimAction;
import gamestate.GameStates;
import gamestate.GlobalGameState;
import gamestate.IGameState;

public class GSTransition implements IGameState {

	private static GSTransition s_instance = null;
	
	private AnimBase m_anim = null;
	private GameStates m_nextGameState = null;

	@Override
	public void onEnter() {
		if(m_anim == null){
			if(m_nextGameState != null)
				GlobalGameState.setActiveGameState(m_nextGameState);
			else
				System.out.println("Error, m_nextGameState == null.");
		}else{
		
			m_anim.runWhenDone( new AnimAction() {
				
				@Override
				public void run() {
					GlobalGameState.setActiveGameState(m_nextGameState);
					m_anim = null;
					m_nextGameState = null;
				}
			} );
		
			GlobalAnimQueue.playAnimation( m_anim );
		}
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	public void prepareTransition( GameStates nextGS ){
		m_nextGameState = nextGS;
		
		if(nextGS == GameStates.COMBAT )
			m_anim = new BGFadeAnim();
		else
			System.out.println("Error! No suitable animation for this transition found.");
	}

	public static GSTransition getInstace(){
		return s_instance;
	}

	public GSTransition(){
		if(s_instance == null)
			s_instance = this;
		else
			System.out.println("Error! A second instance of GSTransition has been created.");
	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub
		
	}
}
