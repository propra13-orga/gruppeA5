import java.awt.event.KeyEvent;

import map.CellInfo;
import map.Coordinate;
import map.Map;

import std.StdDraw;
import std.StdIO;
import javax.swing.JOptionPane;
import javax.swing.JOptionPane; 
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Player {
	
	boolean standsOnTeleporter = false;
	
	double playerX, playerY; //Die Koordinaten des Spielers 
	//Hitbox
	double xMin = 6; 
	double xMax = 23;
	double yMin = 2;
	double yMax = 30;
	
	int player_health = 1; //Die Anzahl der Leben des Spielers
	
	//Der Spieler lebt solange er mindestens ein Leben hat
	//Wird der Spieler verwundet wird ein Leben abgezogen
	public boolean isAlive(){ return player_health > 0;}
	public void isHit(){ player_health--;}
	
	public void setLives(int lives){
		player_health = lives;
	}
	
	//Beim Betreten eines Teleporterfelds wird die nächste Map geladen
	//Der Spieler nimmt die in der MapDatei gespeicherte Position ein
	public void enterTeleporter(Map map){
		CellInfo ti = map.getCellInfo(map.getGridX(playerX+16),map.getGridY(playerY+16));
		
		if (ti != null && ti.mHasTeleporter){
		
			//Falls der Spieler gerade auf einem Teleporter steht, wird er nicht teleportiert.
			//Ansonsten würde man die ganze Zeit hin-und-her teleportiert werden wenn man einen Teleporter betritt.
			if(standsOnTeleporter == true)
				return;
		
			standsOnTeleporter = true;
			String newMapName = ti.mNewMap;
			Coordinate newMapPosition = ti.mNewPosition;
			map.loadLevel(newMapName);
			playerX = map.getCanvasX(newMapPosition.mX);
			playerY = map.getCanvasY(newMapPosition.mY);
			
		}else{
			standsOnTeleporter = false;
		}
	}
	
	private void lookForEnemy(Map map) {
		CellInfo ti = map.getCellInfo(map.getGridX(playerX+16),map.getGridY(playerY+16));
		
		if (ti != null && ti.mHasEvent){
			if(ti.mEventID == 0){
				isHit();
			}else if(ti.mEventID == 1){
				isHit();
			}
		}
		
	}
	
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
		if( StdIO.isKeyPressed(KeyEvent.VK_A)) {
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
		
		//Suche nach einem Teleporter auf dem momentanen Feld.
		enterTeleporter(map);
		lookForEnemy(map);
	}
	
	//Zeichnet den Spieler an die Stelle im Koordinatensystem
	public void render(){
		StdDraw.picture(playerX,playerY,"data/player/red.png");
		StdDraw.picture(playerX,playerY,"data/player/boromir.png");
		StdDraw.picture(playerX,playerY,"data/player/deep_elf_m.png");
		StdDraw.picture(playerX,playerY,"data/player/banded.png");
		StdDraw.picture(playerX,playerY,"data/player/leg_armor03.png");
		StdDraw.picture(70, 10, "data/herz.png");	//Herz
		StdDraw.picture(200, 10, "data/mana.png");	//Mana
		StdDraw.picture(335, 10, "data/schwert.png"); //item
	}
}
	
}
