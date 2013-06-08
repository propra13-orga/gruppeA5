package gamestate;
import java.util.EnumMap;

public class GlobalGameState {
	
	private static IGameState s_currentGameState = null;
	private static EnumMap<GameStates, IGameState> s_states = new EnumMap<>(GameStates.class);
	
	public static void associateGameState(GameStates gs, IGameState igs){
		s_states.put( gs, igs );
	}
	
	public static void setActiveGameState(GameStates gs){
		s_currentGameState.onExit();
		s_currentGameState = s_states.get(gs);
		s_currentGameState.onEnter();
	}
	
	public static void initiateGlobalGameState(GameStates gs){
		s_currentGameState = s_states.get(gs);
		s_currentGameState.onEnter();
	}
	
	public static IGameState getActiveGameState(){
		return s_currentGameState;
	}
	
	public static IGameState getGameState(GameStates gs){
		return s_states.get(gs);
	}
	
	private GlobalGameState(){}
}
