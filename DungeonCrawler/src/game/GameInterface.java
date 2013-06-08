package game;
import std.StdDraw;


public class GameInterface {

	public void render(){
		StdDraw.picture(70, 10, "data/herz.png");	//Herz
		StdDraw.picture(200, 10, "data/mana.png");	//Mana
		StdDraw.picture(335, 10, "data/schwert.png"); //item
	
		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.textLeft( 10, 20, "Leben:" );
		StdDraw.textLeft( 150, 20, "Mana:" );
		StdDraw.textLeft( 300, 20, "Item:");
	}
}
	