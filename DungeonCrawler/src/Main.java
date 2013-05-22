import java.awt.event.KeyEvent;

import map.Map;
import map.CellInfo;

import std.StdDraw;
import std.StdIO;
import std.StdWin;
import std.StdIO.KeyEventInfo;



public class Main {
	static boolean s_shutdown=false;
	
	public static void main(String[] args) {
		//Initialisiert ein Fenster mit der Standardgröße von 800x600 pixel.
		StdWin.init();
		
		MainMenu menu = new MainMenu("");
		
		//Erstellt die Map an der Position 144,44 (von oben links gesehen).
		//Die Zahlen sind so gewählt, dass die Map genau in der Mitte des Fensters ist.
		Map map = new Map(144,44, "data/tiles.txt");
		
		//Lädt das erste Level aus einer Textdatei. Wenn das laden fehlschlägt, soll einfach
		//das Program beendet werden.
		if( !map.loadLevel("data/outside.txt") )
			s_shutdown = true;


		Player p1 = new Player();			
		p1.playerX = map.getCanvasX(7);
		p1.playerY = map.getCanvasY(0);

		//Der game loop:
		while(!s_shutdown){
		
			//Solange das Spiel nicht starten soll, brauchen wir auch den render loop nicht durchführen.
			if(!menu.wantStartGame())
				continue;
		
			//Fülle das Fenster mit Schwarz, dann zeichne die Map.
			StdDraw.clear(StdDraw.BLACK);
			
			p1.update(map);
			
			//Falls der Spieler tot ist, game stoppen und Menü anzeigen.
			if(p1.isAlive() == false){
				menu.showMenu();
				
				//Vorbereiten für Neustart.
				p1.setLives(1);
				p1.playerX = map.getCanvasX(7);
				p1.playerY = map.getCanvasY(0);
				if( !map.loadLevel("data/outside.txt") )
					s_shutdown = true;
				
			}
			
			map.render();
			p1.render();
			ui.render();
			//Warte 16 frames, before das nächste Bild gerendert werden soll (=> 60 fps)
			StdDraw.show(16);
			StdDraw.clear();
		}
		
		//Falls der loop zuende ist (also shutdown == true), wird das Fenster geschlossen.
		StdDraw.close();
	}

}