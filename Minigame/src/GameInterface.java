import std.StdDraw;


public class GameInterface {

	private PlayerControl m_player;

	//Update-methode ist nur zur Vollständigkeit hier..
	public void update(){
	
	}
	
	//Zeichne die Spieler-HP in die linke obere Ecke.
	public void render(){
		StdDraw.textLeft( 10, 15, "HEALTH: " + m_player.getHealth() );
	}
	
	public GameInterface( PlayerControl pc ){
		m_player = pc;
	}
}
