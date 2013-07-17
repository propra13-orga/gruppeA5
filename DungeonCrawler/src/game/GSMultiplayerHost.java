package game;

import network.NetworkManager;
import network.NetworkServer;
import network.NetworkSocket;
import std.StdDraw;
import gamestate.GameStates;
import gamestate.GlobalGameState;
import gamestate.IGameState;

public class GSMultiplayerHost implements IGameState {

	NetworkServer m_server;
	NetworkSocket m_socket;

	@Override
	public void onEnter() {
		System.out.println("HOST");
		m_server = new NetworkServer();
		m_server.listenTo(4444);
		
		
	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() {
		StdDraw.text(400,300, "Hosting. Please wait...");

	}

	@Override
	public void update() {
		if( !m_server.isConnected() ){
			return;
		}
		m_socket = m_server.getNetworkSocket();
		
		NetworkManager.listenTo(m_socket);
		
		GSGame.getInstance().startGameMulti(true);
		GlobalGameState.setActiveGameState(GameStates.GAME);
	}
		

}
