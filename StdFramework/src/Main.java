import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Random;

import Std.StdDraw;
import Std.StdIO.KeyEventInfo;
import Std.StdWin;
//import Std.StdIO;
import Std.StdIO;


public class Main {

	/**
	 * @param args
	 */
	
	private static void program1(){
	 
		 //Zeichne blaues Quadrat mit der linken oberen Ecke bei (10,10) und der Seitenlänge 100.
		 StdDraw.setPenColor(StdDraw.BLUE);
		 StdDraw.square(10, 10, 100);
		 
		 //Zeichne einen roten ausgefüllten Kreis mit dem Mittelpunkt (100,200) und Radius 50;
		 StdDraw.setPenColor(StdDraw.RED);
	     StdDraw.filledCircleCentered(100, 200, 50);
	     
	     //Zeichne ein gelbes Quadrat in den roten Kreis.
	     StdDraw.setPenColor(StdDraw.YELLOW);
	     StdDraw.squareCentered(100, 200, 25);
	     
	     //Schreibe einen (gelben) Text in die Mitte des roten Kreises.
	     StdDraw.text(100,200, "Text");
	     
	     //Zeichne eine gerundete Linie und einen dicken Punkt in der Mitte
	     StdDraw.setPenColor(StdDraw.BOOK_RED);
	     StdDraw.setPenRadius(5);
	     StdDraw.arcCentered(600, 400, 50, 200, 45);
	     StdDraw.setPenRadius(50);
	     StdDraw.point(600,400);

	     //Setze die Stiftgröße auf Standard (1.0).
	     StdDraw.setPenRadius(); 

	     // Zeichne einen blauen Diamanten.
	     StdDraw.setPenColor(StdDraw.BOOK_BLUE);
	     double[] x = { 200, 300, 400, 300 };
	     double[] y = { 200, 300, 200, 100 };
	     StdDraw.filledPolygon(x, y);
	 }
	 
	 @SuppressWarnings("unused")
	private static void program2(){
		 		
		 double upperBoxX = 400.0;
		 double lowerBoxX = 400.0;
		 		
		 while(true){
			 //Fülle das Fenster mit Weiß.
			 StdDraw.clear();
		 
			 //Zeichne ein Rechteck mit schwarzen dicken Rand.
			 StdDraw.setPenColor(StdDraw.BLACK);
			 StdDraw.setPenRadius(5);
			 StdDraw.rectangle(10,10,780,80);
			 
			 StdDraw.setPenRadius(1);
		 
			 //Lese momentane Mausposition
			 double x = StdIO.mouseX();
			 double y = StdIO.mouseY();
			 
			 //Wenn die Mausposition innerhalb des Rechtecks ist, setze die Zeichenfarbe auf Rot.
			 if(x > 10 && x < 790 && y > 10 && y < 90)
				 StdDraw.setPenColor(StdDraw.RED);
				 
			 //Zeichne ein Quadrat an der Mausposition.
	    	 StdDraw.filledSquareCentered(x, y, 10.0);
	    		
	    	 //Schreibe die X/Y Koordinate der Maus auf den Bildschirm.
	    	 StdDraw.setPenColor(StdDraw.BLACK);
	    	 StdDraw.text(40, 590, (int) x + " : " + (int) y);
	    		
	    	 while( StdIO.hasKeyEvents() ){
	    		 KeyEventInfo ki = StdIO.pollKeyEvent();
	    		 
	    		 if( ki.mKeyCode == KeyEvent.VK_LEFT && ki.mKeyDown == true )
	    			 upperBoxX -= 10;
	    		 else if( ki.mKeyCode == KeyEvent.VK_RIGHT && ki.mKeyDown == true )
	    			 upperBoxX += 10;
	    	 }
	    	 
	    	 if( StdIO.isKeyPressed(KeyEvent.VK_A) )
	    		 lowerBoxX -= 1;
	    	 else if( StdIO.isKeyPressed(KeyEvent.VK_D) )
	    		 lowerBoxX += 1;
	    		
	    	 StdDraw.textLeft(10, 350, "Pfeiltasten um zu bewegen.");
	    	 StdDraw.textLeft(10, 400, "A und D um zu bewegen.");
	    		
	    	 StdDraw.filledSquareCentered(upperBoxX, 350, 15);
	    	 StdDraw.filledSquareCentered(lowerBoxX, 400, 15);
	    		
	    	 //Rendere das Bild mit Framezeit von 10 Millisekunden.
	    	 // Diese Methode braucht dann 10ms um ausgeführt zu werden. Dieser loop hier kann also
	    	 //nur 100 mal pro Sekunde ausgeführt werden. Wir haben also 100 frames pro Sekunde.
	    	 StdDraw.show(10);
		 }
	    	
	 }
	 
	 @SuppressWarnings("unused")
	 private static void program3(){
		 final int FIELD_SIZE = 16; //Anzahl der Quadrate (Felder) in einer Reihe.
	 
		 double currAngle = 0;
		 Color currColor = Color.RED;
		 
		 while(true){
			 //Fülle das Fenster mit Weiß.
			 StdDraw.clear();
			 
			 //Lese Mausposition
			 double mx = StdIO.mouseX();
			 double my = StdIO.mouseY();
			 
			 //Errechne das Quadrat, in dem sich die Maus befindet.
			 int sX = (int) mx / 34;
			 int sY = (int) my / 34;
			 
			 //Falls die Maus sich außerhalb des Feldes befindet, setze xS und sY auf eine nichtexistierende Position
			 if( !(sX < FIELD_SIZE && sY < FIELD_SIZE) ){
				 sX = -1;
				 sY = -1;
			 }
		 
			 //Zeichne die Felder
			 for(int x=0; x<FIELD_SIZE; x++){
				 for(int y=0; y<FIELD_SIZE; y++){
				 
					 //Falls sich die Maus auf dem Feld befindet: Nimm Rot.
					 //Falls sich die Maus horizontal oder vertikal zum Feld befindet: Nimm Grau.
					 //ansonsten: Nimm Schwarz.
					 if(x == sX && y == sY)
						 StdDraw.setPenColor(StdDraw.RED);
					 else if(x == sX || y == sY)
						 StdDraw.setPenColor(StdDraw.GRAY);
					 else
						 StdDraw.setPenColor(StdDraw.BLACK);

					 StdDraw.filledSquare(x*34, y*34, 32);
				 }
			 }
			 
			 //Zeichne den Kreis, der die Farbe wechselt.
			 
			 StdDraw.setPenColor(StdDraw.BOOK_RED);
		     StdDraw.setPenRadius(10.5);
		     StdDraw.arcCentered(700, 200, 25, 0, currAngle);
		     StdDraw.setPenColor(currColor);
		     StdDraw.setPenRadius(40);
		     StdDraw.point(700,200);
		     
		     currAngle += 4;
		     
		     if(currAngle >= 360){
		    	 //Generiere eine Zufallsfarbe
		    	 Random r = new Random();
		    	 currColor = new Color( r.nextInt(255), r.nextInt(255), r.nextInt(255) );
		    	 currAngle = 0;
		     }
	    		
	    	 //Rendere das Bild mit Framezeit von 20 Millisekunden.
	    	 // Diese Methode braucht dann 20ms um ausgeführt zu werden. Dieser loop hier kann also
	    	 //nur 50 mal pro Sekunde ausgeführt werden. Wir haben also 50 frames pro Sekunde.
	    	 StdDraw.show(20);
		 }		 
	 }
	 
	 public static void main(String[] args) {
		 //Standardmäßig erstellt ein Fenster der Größe 800x600.
		 StdWin.init();
		 
		 //Wähle ein Beispielprogram aus:
		 
		 //program1();
		 program2();
		 //program3();
   
    }

}
