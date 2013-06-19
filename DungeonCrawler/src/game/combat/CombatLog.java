package game.combat;

import java.util.ArrayList;

import std.StdDraw;

public class CombatLog {
	private ArrayList<String> m_messages = new ArrayList<>();
	private int m_framesSinceStart = 0;
	
	public void add(String msg){
		m_messages.add(msg);
	}
	
	public boolean isEmpty(){
		return m_messages.isEmpty();
	}
	
	public void next(){
		if( !isEmpty() )
			m_messages.remove(0);
		
		m_framesSinceStart = 0;
	}
	
	public void render(){
		StdDraw.setPenColor(StdDraw.WHITE);
		StdDraw.rectangle(25, 390, 400, 140);
		
		if( !m_messages.isEmpty() ){
			String s = m_messages.get(0);
			s = s.substring(0, Math.min(m_framesSinceStart, s.length()-1));
		
			StdDraw.textLeft(30, 405, s);
			m_framesSinceStart += 2; //TODO: Frame-rate dependend
		}
	}
}
