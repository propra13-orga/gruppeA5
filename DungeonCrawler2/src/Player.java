import java.awt.event.KeyEvent;

import map.Map;

import std.StdDraw;
import std.StdIO;

public class Player {
	
	
	
	double playerX, playerY; //Die Koordinaten des Spielers 
	//Hitbox
	double xMin = 9; 
	double xMax = 23;
	double yMin = 5;
	double yMax = 29;
	
	int player_health = 1; //Die Anzahl der Leben des Spielers
	
	//Der Spieler lebt solange er mindestens ein Leben hat
	//Wird der Spieler verwundet wird ein Leben abgezogen
	public boolean isAlive(){ return player_health > 0;}
	public void isHit(){ player_health--;}
	public void enterTeleporter(){}
	
	//Bei Tasteneingabe wird überprüft, ob das angestrebte Feld mit der Hitbox kollidiert
	//Wenn nicht wird der Spieler auf das angestrebte Feld bewegt
	public void update(Map map){
		
		if( StdIO.isKeyPressed(KeyEvent.VK_W)) {
			if (map.isPathable(map.getGridX(playerX + xMin),map.getGridY(playerY-3 + yMin)) 
					&& map.isPathable(map.getGridX(playerX + xMax), map.getGridY(playerY-3 + yMin))){
				playerY-=3;
			}
			
		}
		else if( StdIO.isKeyPressed(KeyEvent.VK_S)) {
			if (map.isPathable(map.getGridX(playerX + xMin),map.getGridY(playerY+3 + yMax)) 
					&& map.isPathable(map.getGridX(playerX + xMax), map.getGridY(playerY+3 + yMax))){
				playerY +=3;
			}
		}
		else if( StdIO.isKeyPressed(KeyEvent.VK_A)) {
			if (map.isPathable(map.getGridX(playerX-3 + xMin),map.getGridY(playerY + yMin)) 
					&& map.isPathable(map.getGridX(playerX-3 + xMin),map.getGridY(playerY + yMax))){
				playerX -=3;
			}
		}
		else if( StdIO.isKeyPressed(KeyEvent.VK_D)) {
			if (map.isPathable(map.getGridX(playerX+3 + xMax),map.getGridY(playerY + yMin)) 
					&& map.isPathable(map.getGridX(playerX+3 + xMax),map.getGridY(playerY + yMax))){
				playerX +=3;
			}
		}
		
	}
	
	//Zeichnet den Spieler an die Stelle im Koordinatensystem
	public void render(){
		StdDraw.picture(playerX,playerY,"data/red.png");
		StdDraw.picture(playerX,playerY,"data/boromir.png");
		StdDraw.picture(playerX,playerY,"data/deep_elf_m.png");
		StdDraw.picture(playerX,playerY,"data/banded.png");
		StdDraw.picture(playerX,playerY,"data/leg_armor03.png");
	}
	
	
}
