package game;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import network.NetworkManager;
import network.msg.ChatMessage;

import std.StdDraw;

public class ChatWindow {
	private static List<String> s_chat = new ArrayList<>();
	private static String s_currMsg = "";
	private static int s_x, s_y;
	
	public static void setPosition(int x, int y){
		s_x = x;
		s_y = y;
	}
	
	public static void render(){
	
		if( !NetworkManager.isMultiplayer() )
			return;
	
		int y=10;
		
		StdDraw.setPenColor(StdDraw.YELLOW);
		for(String s : s_chat){
			StdDraw.textLeft(s_x+2, s_y+y, s);
			y += 20;
		}
		
		StdDraw.textLeft(s_x+2, s_y+90, s_currMsg);

		StdDraw.rectangle(s_x, s_y, 235, 100);
	}
	
	public static void addMessage(String msg){
	
		if( s_chat.size() >= 4 ){
			s_chat.remove(0);
		}
		s_chat.add(msg);
	}
	
	public static boolean processKey(KeyEvent evt){
		if( evt.getKeyCode() == KeyEvent.VK_ENTER ){
			
			NetworkManager.send( new ChatMessage("Other: " + s_currMsg) );
			addMessage( "You: " + s_currMsg );
			
			s_currMsg = "";
			return true;
		}
	
		s_currMsg += evt.getKeyChar();
		return false;
	}
}
