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
	
	@Override
	public void render() {
		StdDraw.setPenColor( StdDraw.WHITE );
		
		//Toller Button für Start Game. So können später alle Btns aussehen, wenn noch genug Zeit ist.
		//StdDraw.pictureCentered(400, 200, "data/ui/BtnNewGame.png");
		StdDraw.picture(350,225, "data/ui/MenuNewGame.png");
		//StdDraw.picture(350,250, "data/ui/MenuNewGame.png");
		StdDraw.picture(330,250, "data/ui/HostMultiplayerBtn.png");
		StdDraw.picture(330,275, "data/ui/JoinMultiplayerBtn.png");
		StdDraw.picture(350,300, "data/ui/MenuSettings.png");
		StdDraw.picture(350,325, "data/ui/MenuCredits.png");
		StdDraw.picture(350,350, "data/ui/MenuQuit.png");
		StdDraw.setPenColor(StdDraw.WHITE);
		StdDraw.rectangle(325,223 + m_y*25,175,25);
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
		if( (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) && m_y<6){
			m_y+=1;
		}
		if( (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) && m_y>0){
			m_y-=1;
		}
		getMenuState(e);
	}
	
	
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