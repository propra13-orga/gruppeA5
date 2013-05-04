import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import Std.StdDraw;


public class EnemyControl {
	
	//
	private static class Enemy{
		public double mX, mY;
		public double mSize;
		public double mSpeed;
		
		public static double mBaseSpeed = 3;
		
		//Erstelle einen Gegner mit zufälligen Stats.
		public Enemy( Random random ){
			mX = 850;
			mY = random.nextDouble() * 600;
			mSize = 4 + random.nextDouble() * 4;
			mSpeed = mBaseSpeed + random.nextDouble() * 2;
		}
	}
	
	//Liste aller Gegner
	private ArrayList<Enemy> m_enemies = new ArrayList<Enemy>();
	
	//Zufallsgenerator.
	private Random m_random = new Random();
	
	//Anzahl der vergangenen Frames. Das benutze ich hier um Zeit zu messen.
	private int m_frameCounter = 0;

	//Zeit seit dem letzten Gegnerspawn
	private int m_spawnCounter = 0;
	
	//Alle m_spawnInterval frames soll ein neuer Gegner spawnen.
	private int m_spawnInterval = 3;
	
	//Der Spieler
	private PlayerControl m_player;

	//Überprüft ob der Kreis mit dem Mittelpunkt (x1,y1) und dem Radius s1 mit dem Kreis (x2,y2) mit Radius s2 kollidiert.
	//Gibt true zurück, falls das der Fall ist.
	private boolean collisionCheck(double x1, double y1, double s1, double x2, double y2, double s2){
		double dx = x2-x1;
		double dy = y2-y1;
		
		return dx*dx + dy*dy < (s1+s2)*(s1+s2);
	}

	//Update-methode wird jeden Frame aufgerufen.
	public void update(){
		//Erhöhe die counter.
		m_spawnCounter++;
		m_frameCounter++;
		
		//Falls es an der Zeit ist, einen neuen Gegner zu spawnen..
		if(m_spawnCounter >= m_spawnInterval){
			m_spawnCounter = 0;
			//..erstelle ihn und ..
			Enemy e = new Enemy( m_random );
			//.. füge ihn zu der Liste aller Gegner hinzu.
			m_enemies.add( e );
		}
		
		// Hier wird die Schwierigkeit des Games in Intervallen erhöht
		
		//Alle 500 frames wird die Geschwindigkeit der Gegner höher.
		if(m_frameCounter % 500 == 0){
			Enemy.mBaseSpeed += 0.5;
			System.out.println("Increase speed to " + Enemy.mBaseSpeed);
		}
		
		//Alle 1000 frames ( ca. alle 15 Sekunden ) wird die Spawngeschwindigkeit der Gegner erhöht
		//Aber nur 3 mal. Dann spawn schon bei jedem frame ein Gegner. Das reicht. 
		if(m_frameCounter == 1000){
			System.out.println("Increase density to one enemy per " + m_frameCounter + "frames");
			m_spawnInterval = 2;
		}
		if(m_frameCounter == 2000){
			System.out.println("Increase density to one enemy per " + m_frameCounter + "frames");
			m_spawnInterval = 1;
		}
		if(m_frameCounter == 3000){
			System.out.println("Increase density to one enemy per " + m_frameCounter + "frames");
			m_spawnInterval = 0;
		}
		
		//Jetzt wird die Position aller Gegner geupdatet.
		double px = m_player.getX();
		double py = m_player.getY();
		double ps = m_player.getSize();
		
		//Für jeden Gegner:
		for(Iterator<Enemy> i = m_enemies.iterator(); i.hasNext();) {
			Enemy e = i.next();
			
			//Bewege ihn nach links,
			e.mX -= e.mSpeed;
			
			//Falls er das Spielfeld auf der linken Seite verlassen hat, entferne ihn.
			if(e.mX < -50 ){
				i.remove();
				continue;
			}
				
			//Überprüfe, ob der Gegner mit dem Spieler kollidiert.
			if( collisionCheck(e.mX, e.mY, e.mSize, px, py, ps) ){
				m_player.getHit();	//Falls ja, entferne ein Leben.
			}
		 }
		
	}
	
	//Zeichne alle gegner.
	public void render(){
		StdDraw.setPenColor(StdDraw.RED);
		for( Enemy e : m_enemies ){
			StdDraw.filledCircleCentered(e.mX, e.mY, e.mSize);
		}
	}
	
	public EnemyControl( PlayerControl pc ){
		m_player = pc;
	}
}
