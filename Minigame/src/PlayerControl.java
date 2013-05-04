import Std.StdDraw;
import Std.StdIO;
import Std.StdWin;


public class PlayerControl {
	//player stats
	private double m_playerX, m_playerY;
	private double m_playerSize;
	private int m_health;
	
	//Variablen für kurze Unverwundbarkeit nachdem man ein Leben verloren hat.
	private final static int GRACE_PERIOD = 60; //<- 60 frames lange Unverwundbarkeit.
	private int m_graceCounter;

	
	public double getX()	{ return m_playerX; 	}
	public double getY()	{ return m_playerY; 	}
	public double getSize()	{ return m_playerSize; 	}
	public int getHealth()	{ return m_health; 		}
	public boolean isAlive(){ return m_health > 0; 	}
	
	//Wird aufgerufen, wenn der Spieler getroffen ist. 
	public void getHit(){
		//Falls der Spieler grade unverwundbar ist, mache nichts.
		if(m_graceCounter > 0)
			return;
	
		//Ansonsten reduziere HP um 1 und mache den Spieler unverwundbar
		m_health -= 1;
		m_graceCounter = GRACE_PERIOD;
	}
	

	public void update(){
		//Reduziere Unverwundbarkeitsdauer, falls Spieler unverwundbar ist.
		if( m_graceCounter > 0){
			m_graceCounter -= 1;
			
		}
		
		//Setze die Spielerposition auf die momentane Mausposition.
		m_playerX = StdIO.mouseX();
		m_playerY = StdIO.mouseY();
		
		//Den code hier kann man für Keyboard-Steuerung verwenden.
	/*
	    double m_speed = 1.5;
		if( StdIO.isKeyPressed(KeyEvent.VK_W) )
			m_playerY -= m_speed;
		else if( StdIO.isKeyPressed(KeyEvent.VK_S) )
			m_playerY += m_speed;
			
		if( StdIO.isKeyPressed(KeyEvent.VK_A) )
			m_playerX -= m_speed;
		else if( StdIO.isKeyPressed(KeyEvent.VK_D) )
			m_playerX += m_speed;
	*/
	
	}

	public void render(){
		//Während der Spieler unverwundbar ist, soll er ein bisschen blinken.
		if( m_graceCounter % 10 == 0)
			StdDraw.setPenColor(StdDraw.BLUE);
		else
			StdDraw.setPenColor(StdDraw.ORANGE);
			
		//Zeichne den Spieler
		StdDraw.filledCircleCentered(m_playerX, m_playerY, m_playerSize);
	}
	
	public PlayerControl(int lives){
		m_health = lives;
		m_playerSize = 6.0;
		m_playerX = StdWin.getWidth() / 2.0;
		m_playerY = StdWin.getHeight() / 2.0;
	}
}
