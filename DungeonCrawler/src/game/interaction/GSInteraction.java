package game.interaction;
import java.awt.Image;
import java.awt.event.KeyEvent;
import std.StdDraw;
import std.StdIO;
import std.StdIO.KeyEventType;
import gamestate.GameStates;
import gamestate.GlobalGameState;
import gamestate.IGameState;


public class GSInteraction implements IGameState, StdIO.IKeyListener {
	private Image m_background;
	
	private static String s_text = "Error";
	private int m_charactersDisplayed;
	
	public static void setText(String text){
		s_text = text;
	}

	@Override
	public void render() {
		StdDraw.image(0,0,m_background);
		
		StdDraw.setAlpha(0.33f);
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.filledRectangle(0,0,800,800);
		
		StdDraw.setAlpha(0.55f);
		StdDraw.filledRectangle(200, 400, 400, 140);
		StdDraw.setAlpha(1.f);
		StdDraw.setPenColor(StdDraw.WHITE);
		StdDraw.rectangle(200, 400, 400, 140);
		
		String text = s_text.substring(0, Math.min(m_charactersDisplayed,s_text.length()) );
		StdDraw.textLeftFmt(205,415,text);
		
	}

	@Override
	public void update() {
		m_charactersDisplayed += 2;
	}

	@Override
	public void receiveEvent(KeyEvent e) {
	
		switch(e.getKeyCode()){
			
		case KeyEvent.VK_ENTER:	
				
			GlobalGameState.setActiveGameState(GameStates.GAME);
			break;
		
		}

	}
	
	@Override
	public void onEnter() {
		m_background = StdDraw.getSnapshot();
		m_charactersDisplayed = 0;
	
		StdIO.addKeyListener(this, KeyEventType.KeyReleased);
	}

	@Override
	public void onExit() {
		StdIO.removeKeyListener(this, KeyEventType.KeyReleased);
	}
}
