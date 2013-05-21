import java.awt.event.KeyEvent;

import map.CellInfo;
import map.Coordinate;
import map.Map;

import std.StdDraw;
import std.StdIO;
import std.StdWin;
import std.StdIO.KeyEventInfo;



public class Main {
	/* Wichtige Methoden:
	 * 
	 * Ein Event-loop:
	 * 
	 * while( StdIO.hasKeyEvents() ){
			KeyEventInfo e = StdIO.pollKeyEvent();
			//Blah blah
	 * }
	 * 
	 * Damit kann man Tastatureingaben auslesen. In KeyEventInfo steht drin, welcher Key gedrückt/losgelassen wurde.
	 * Genauso auch für MouseEvents.
	 * 
	 * Methoden aus map.Map:
	 * 
	 * Map m;
	 * 
	 * m.isPathable(x, y) - Gibt true zurück, wenn das Feld bei (x,y) begehbar ist.
	 * CellInfo ti = m.getCellInfo(x,y) - CellInfo beinhaltet, ob Events oder Teleporter auf dem Feld (x,y) sind. 
	 * 										Gibt null zurück, wenn weder Event noch Teleporter auf (x,y) sind.
	 * m.loadLevel("Eine map.txt") - Lädt (und öffnet) eine map mit angegebenen Namen. Gibt true zurück wenn erfolgreich.
	 * 
	 * double cx = m.getCanvasX(x);
	 * double cy = m.getCanvasY(y);
	 * Diese beiden Methoden geben die tatsächliche Fensterkoordinate (cy,cy) aus, an der sich das Feld (x,y) befindet.
	 * 
	 * Ein Beispiel folgt:
	 */
	 
	private static String s_input = "";
	 
	//Gibt die gedrückten Tasten auf dem Bildschirm aus.
	public static void Input(){
		//Printet jede Taste, die gedrückt wurde, auf dem Bildschirm aus.
		while( StdIO.hasKeyEvents() ){
			//Hole das nächste key event
			KeyEventInfo e = StdIO.pollKeyEvent();
			
			//Falls mKeyDown == false, dann wurde die Taste losgelassen und nicht gedrückt.
			//Also ignorieren wir das event in diesem Fall.
			if(!e.mKeyDown)
				continue;
			
			//Hole den Key code des Keys.
			int i = e.mKeyCode;
			s_input += KeyEvent.getKeyText(i) + " "; //Und speichert den Namen der Taste in einem String.
			
			//Falls der string länger als 100 ist, leeren wir ihn.
			if(s_input.length() > 100)
				s_input = "";
		}
		
		//Zeichne den string an die linke untere Ecke.
		StdDraw.setPenColor(StdDraw.BOOK_RED);
		StdDraw.textLeft(10,580, s_input);
	}
	 
	static boolean s_shutdown=false;
	
	public static void main(String[] args) {
		//Initialisiert ein Fenster mit der Standardgröße von 800x600 pixel.
		StdWin.init();
		
		//Erstellt die Map an der Position 144,44 (von oben links gesehen).
		//Die Zahlen sind so gewählt, dass die Map genau in der Mitte des Fensters ist.
		Map map = new Map(144,44, "data/tiles.txt");
		
		//Lädt das erste Level aus einer Textdatei. Wenn das laden fehlschlägt, soll einfach
		//das Program beendet werden.
		if( !map.loadLevel("data/map1.txt") )
			s_shutdown = true;

		//Der game loop:
		while(!s_shutdown){
			//Fülle das Fenster mit Schwarz, dann zeichne die Map.
			StdDraw.clear(StdDraw.BLACK);
			
			Player p1 = new Player();			
			
			p1.playerX = map.getCanvasX(14);
			p1.playerY = map.getCanvasY(2);
			
			while (p1.isAlive()){
				p1.update(map);
				p1.render();
				Input();
				//Warte 16 frames, bevor das nächste Bild gerendert werden soll (=> 60 fps)
				StdDraw.show(16);
				map.render();
				//p1.enterTeleporter(map);
			}
			
			
			
			
			StdDraw.clear();
		}
		
		//Falls der loop zuende ist (also shutdown == true), wird das Fenster geschlossen.
		StdDraw.close();
	}

}