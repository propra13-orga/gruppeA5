package network;

import java.util.ArrayList;

import entity.Companion;
import entity.IEntity;
import game.GSTransition;
import game.combat.GSCombat;
import game.player.Player;
import gamestate.GameStates;
import gamestate.GlobalGameState;
import map.Map;
import monster.EnemyPlayer;
import network.msg.ChatMessage;
import network.msg.EnterCombatMessage;
import network.msg.INetworkPackage;
import network.msg.MoveMessage;
import network.msg.PositionMessage;

public class NetworkManager {

	private static NetworkSocket s_socket = null;
	private static EnemyPlayer s_enemyPlayer = null;
	private static boolean s_isHost = false;
	
	public static boolean isMultiplayer(){
		return s_socket != null;
	}
	public static boolean isHost(){
		return s_isHost;
	}
	
	public static void update(){
		if(s_socket == null)
			return;
			
		INetworkPackage msg = s_socket.getMessage();
		
		if(msg==null)
			return;
		
		//System.out.println("Msg: " + msg.getType().toString() );
		
		if( msg.getType() == PackageType.CHAT_MESSAGE ){
			System.out.println( "Server received: " + ((ChatMessage)msg).getMessage() );
		}else if( msg.getType() == PackageType.MOVE ){
			MoveMessage pm = (MoveMessage) msg;
			s_enemyPlayer.issueOrder( pm.getX(), pm.getY() );
		}else if( msg.getType() == PackageType.POSITION ){
			PositionMessage pm = (PositionMessage) msg;
			s_enemyPlayer.synchronizeWithPosition( pm.getX(), pm.getY() );
		}else if( msg.getType() == PackageType.ENTER_COMBAT ){
		
			if( !isHost() ){
				ArrayList<IEntity> entityList = new ArrayList<>();
				for( Companion c : Player.getInstance().getCompanions() )
					entityList.add(c);
				NetworkManager.send( new EnterCombatMessage(entityList) );
			
				GSCombat.getInstance().prepareEncounter(Map.getInstance().getMonsterPool(), EnemyPlayer.getEnemyPlayer());
				GSTransition.getInstace().prepareTransition( GameStates.COMBAT );
				GlobalGameState.setActiveGameState(GameStates.TRANSITION);
			}
		
			System.out.println("Received CombatMessage with " + ((EnterCombatMessage)msg).getEnemy().size() + " entities.");
			EnemyPlayer.getEnemyPlayer().setEntities( ((EnterCombatMessage)msg).getEnemy() );
		}
		
		update();
	}
	
	public static void send( INetworkPackage msg ){
		if(s_socket != null)
			s_socket.send(msg);
	}
	
	public static void listenTo(NetworkSocket s){
		s_socket = s;
	}
	public static void setEnemyPlayer(EnemyPlayer p){
		s_enemyPlayer = p;
	}
	public static void declareHost(){
		s_isHost = true;
	}


	private NetworkManager(){}
}
