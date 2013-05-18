import java.awt.event.KeyEvent;

import std.StdDraw;
import std.StdIO;

public class Player {
	
	
	
	double playerX, playerY; //Die Koordinaten des Spielers 
	double backupX, backupY; 
	//Hitbox
	double xMin = 9; 
	double xMax = 23;
	double yMin = 2;
	double yMax = 32;
	
	int player_health = 1; //Die Anzahl der Leben des Spielers
	
	//Der Spieler lebt solange er mindestens ein Leben hat
	//Wird der Spieler verwundet wird ein Leben abgezogen
	public boolean isAlive(){ return player_health > 0;}
	public void isHit(){ player_health--;}
	public void enterTeleporter(){}
	
	//Bei Tasteneingabe wird der Spieler im Koordinatensystem in die jeweilige Richtung bewegt
	public void update1(){
		backupX = playerX;
		backupY = playerY;
		if( StdIO.isKeyPressed(KeyEvent.VK_W)) {playerY-=3;}
		else if( StdIO.isKeyPressed(KeyEvent.VK_S)) {playerY+=3;}
		if( StdIO.isKeyPressed(KeyEvent.VK_A)) {playerX-=3;}
		else if( StdIO.isKeyPressed(KeyEvent.VK_D)) {playerX+=3;}
		
	}
	
	public void update2(){
		backupX = playerX;
		backupY = playerY;
		if( StdIO.isKeyPressed(KeyEvent.VK_UP)) {playerY-=3;}
		else if( StdIO.isKeyPressed(KeyEvent.VK_DOWN)) {playerY+=3;}
		if( StdIO.isKeyPressed(KeyEvent.VK_LEFT)) {playerX-=3;}
		else if( StdIO.isKeyPressed(KeyEvent.VK_RIGHT)) {playerX+=3;}
	}
	
	//Sollte die angesteuerte Koordinate nicht Pathable sein wird die ursprüngliche Koordinate wiederhergestellt 
	//Das geschieht vor dem Rendering
	public void undo(){
		playerX = backupX;
		playerY = backupY;
	}
	
	//Zeichnet den Spieler an die Stelle im Koordinatensystem
	public void render1(){
		StdDraw.picture(playerX,playerY,"data/red.png");
		StdDraw.picture(playerX,playerY,"data/boromir.png");
		StdDraw.picture(playerX,playerY,"data/deep_elf_m.png");
		StdDraw.picture(playerX,playerY,"data/banded.png");
		StdDraw.picture(playerX,playerY,"data/leg_armor03.png");
		backupX = playerX;
		backupY = playerY;
	}
	
	public void render2(){
		StdDraw.picture(playerX,playerY,"data/green.png");
		StdDraw.picture(playerX,playerY,"data/boromir.png");
		StdDraw.picture(playerX,playerY,"data/deep_elf_m.png");
		StdDraw.picture(playerX,playerY,"data/banded.png");
		StdDraw.picture(playerX,playerY,"data/leg_armor03.png");
		backupX = playerX;
		backupY = playerY;
	}
	
	
}
