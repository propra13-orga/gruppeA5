package game;

import javax.swing.JOptionPane;

import network.NetworkClient;
import network.NetworkManager;
import network.NetworkSocket;
import std.StdDraw;
import gamestate.GameStates;
import gamestate.GlobalGameState;
import gamestate.IGameState;

public class GSMultiplayerJoin implements IGameState {

	NetworkClient m_client;
	NetworkSocket m_socket;

	@Override
	public void onEnter() {
		System.out.println("CLIENT");
	
		String s = (String)JOptionPane.showInputDialog(
                null,
                "Enter IP address to connect to:\n",
                "Input IP address",
                JOptionPane.PLAIN_MESSAGE,
                null, null, "");
	
		if ( (s == null) || (s.length() == 0) ) {
			GlobalGameState.setActiveGameState(GameStates.MAIN_MENU);
		    return;
		}
	
		m_client = new NetworkClient();
		m_client.connectTo(s, 4444);
	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() {
		StdDraw.text(400,300, "Joining. Please wait...");

	}

	@Override
	public void update() {
		if( !m_client.isConnected() ){
			return;
		}
		m_socket = m_client.getNetworkSocket();
		
		NetworkManager.listenTo(m_socket);
		
		GSGame.getInstance().startGameMulti(false);
		GlobalGameState.setActiveGameState(GameStates.GAME);
	}

}
