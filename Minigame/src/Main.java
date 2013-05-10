import std.StdDraw;
import std.StdWin;


public class Main {
	
	//Ziel des Spiels:
	//Von rechts kommen die ganze Zeit Gegner angeflogen, denen man ausweichen muss.
	//Maussteuerung und begrenzte Leben.
	
	/*Das Herzst�ck der mei�ten Spiele ist der game-loop.
	
		Er hat mei�tens die Form:

		while( <bedingung> ){
			<update all game components>
			
			<render all game components>
		}

		Der game loop unten ist ganz �hnlich aufgebaut.
		F�r unser game sollte jede wichtige Komponente eine update() und render() Methode besitzen.
		Die update() methode wird jeden frame ausgef�hrt und soll Sachen wie Spielerbewegung oder Animationen handhaben.
		Die render() methode wir auch jeden frame ausgef�hrt und benutzt StdDraw um alles auf den Bildschirm zu zeichnen.
		
		Unwichtigere komponenten (z.B. Waffen oder Items) m�ssen also in eine der wichtigeren Komponenten reingepackt werden.
		Die Waffe kann also z.B. von der Spielerkomponente gehandhabt werden. 
		Items k�nnen von der Spielfeldkomponente gemanaged werden.
	*/
	
	public static void main(String[] args) {
		//�ffne Fenster
		StdWin.init();
		
		//Erstelle die einzelnen Komponenten
		PlayerControl pc = new PlayerControl(5);	//Spielersteuerung
		EnemyControl ec = new EnemyControl( pc );	//Gegner
		GameInterface ui = new GameInterface( pc );	//HUD
		
		//Game loop:
		while( pc.isAlive() ){	//Solange der Spieler noch am Leben ist:
		
			StdDraw.clear();
			//Update alle Komponenten
			pc.update();
			ec.update();
			ui.update();
			
			//Rendere alle Komponenten
			pc.render();
			ec.render();
			ui.render();
			
			//Update das Fenster (mit 1000ms / 60 = 16ms delay. Also 60 Bilder pro Sekunde).
			StdDraw.show( (int) (1000. / 60.) );
			
		}
		
		//Wenn das Program hier ankommt, hat der Spieler verloren.
		//F�lle den Bilschirm mit Schwarz, zeige das game over Bild.
		StdDraw.clear(StdDraw.BLACK);
		StdDraw.pictureCentered(StdWin.getWidth() / 2, StdWin.getHeight() / 2, "gameover.jpg");
		StdDraw.show();
	}

}
