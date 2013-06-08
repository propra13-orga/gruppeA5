package gamestate;

public interface IGameState {
	public void onEnter();
	public void onExit();
	
	public void render();
	public void update();
}
