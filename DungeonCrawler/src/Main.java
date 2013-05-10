import java.awt.event.KeyEvent;

import map.Map;
import map.CellInfo;

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
	 * Damit kann man Tastatureingaben auslesen. In KeyEventInfo steht drin, welcher Key gedr�ckt/losgelassen wurde.
	 * Genauso auch f�r MouseEvents.
	 * 
	 * Methoden aus map.Map:
	 * 
	 * Map m;
	 * 
	 * m.isPathable(x, y) - Gibt true zur�ck, wenn das Feld bei (x,y) begehbar ist.
	 * CellInfo ti = m.getCellInfo(x,y) - CellInfo beinhaltet, ob Events oder Teleporter auf dem Feld (x,y) sind. 
	 * 										Gibt null zur�ck, wenn weder Event noch Teleporter auf (x,y) sind.
	 * m.loadLevel("Eine map.txt") - L�dt (und �ffnet) eine map mit angegebenen Namen. Gibt true zur�ck wenn erfolgreich.
	 * 
	 * double cx = m.getCanvasX(x);
	 * double cy = m.getCanvasY(y);
	 * Diese beiden Methoden geben die tats�chliche Fensterkoordinate (cy,cy) aus, an der sich das Feld (x,y) befindet.
	 * 
	 * Ein Beispiel folgt:
	 */
	 
	private static String s_input = "";
	 
	//Gibt die gedr�ckten Tasten auf dem Bildschirm aus.
	public static void Input(){
		//Printet jede Taste, die gedr�ckt wurde, auf dem Bildschirm aus.
		while( StdIO.hasKeyEvents() ){
			//Hole das n�chste key event
			KeyEventInfo e = StdIO.pollKeyEvent();
			
			//Falls mKeyDown == false, dann wurde die Taste losgelassen und nicht gedr�ckt.
			//Also ignorieren wir das event in diesem Fall.
			if(!e.mKeyDown)
				continue;
			
			//Hole den Key code des Keys.
			int i = e.mKeyCode;
			s_input += KeyEvent.getKeyText(i) + " "; //Und speichert den Namen der Taste in einem String.
			
			//Falls der string l�nger als 100 ist, leeren wir ihn.
			if(s_input.length() > 100)
				s_input = "";
		}
		
		//Zeichne den string an die linke untere Ecke.
		StdDraw.setPenColor(StdDraw.BOOK_RED);
		StdDraw.textLeft(10,580, s_input);
	}
	 
	
	//Stellt die Spielfigur auf dem Bildschirm dar.
	public static void Player(Map m){
		//Wir wollen den Spieler auf dem Feld an der Stelle (3,4) darstellen.
		//Dabei ist das Feld (0,0) die obere linke Ecke und das Feld (15,15) die untere rechte Ecke.
		//Das Spielfeld ist als 16x16 Felder gro�, mit jedem Feld 32x32 pixel.
		
		//(cx,cy) ist die Koordinate im Festerbezogenen Koordinatensystem f�r das Feld (3,4).
		//StdDraw-Methoden, die an die Stelle (cx,cy) zeichnen, w�rden also �ber das Feld (3,4) zeichnen.
		double cx = m.getCanvasX(3);
		double cy = m.getCanvasY(4);
		
		//Zeichne ein rotes Rechteck um das Feld.
		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.square(cx,cy, 32);
		
		//Zeichne den Spieler.
		//Die Spieler-sprites sind layered.
		//D.h. dass Waffen, R�stungsteile, etc alle in unterschiedlichen Bildern gespeichert sind.
		//Wenn man also eine Figur mit R�stung und Schwert darstellen will, musst man zuerst die Figur zeichnen,
		//dann die R�stung und das Schwert dr�berzeichnen:
		StdDraw.picture(cx, cy, "data/red.png");			//Umhang
		StdDraw.picture(cx, cy, "data/deep_elf_m.png");	//Figur
		StdDraw.picture(cx, cy, "data/banded.png"); 		//Brustpanzer
		StdDraw.picture(cx, cy, "data/leg_armor03.png");	//Beinpanzer
		StdDraw.picture(cx, cy, "data/boromir.png");		//Schwert
		
		//Das wird sp�ter bestimmt noch praktisch sein.
		
	}
	
	
	static boolean s_shutdown=false;
	
	public static void main(String[] args) {
		//Initialisiert ein Fenster mit der Standardgr��e von 800x600 pixel.
		StdWin.init();
		
		//Erstellt die Map an der Position 144,44 (von oben links gesehen).
		//Die Zahlen sind so gew�hlt, dass die Map genau in der Mitte des Fensters ist.
		Map map = new Map(144,44, "data/tiles.txt");
		
		//L�dt das erste Level aus einer Textdatei. Wenn das laden fehlschl�gt, soll einfach
		//das Program beendet werden.
		if( !map.loadLevel("data/map1.txt") )
			s_shutdown = true;

		//Der game loop:
		while(!s_shutdown){
			//F�lle das Fenster mit Schwarz, dann zeichne die Map.
			StdDraw.clear(StdDraw.BLACK);
			
			map.render();
			
			Player(map);
			
			Input();
			
			//Warte 16 frames, before das n�chste Bild gerendert werden soll (=> 60 fps)
			StdDraw.show(16);
			StdDraw.clear();
		}
		
		//Falls der loop zuende ist (also shutdown == true), wird das Fenster geschlossen.
		StdDraw.close();
	}

}