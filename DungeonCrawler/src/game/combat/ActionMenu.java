package game.combat;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import entity.Companion;
import game.player.Skill;

import std.StdDraw;

public class ActionMenu {
	private Companion m_companion;
	private int m_currSelected = 0;
	private List<String> m_entries = new ArrayList<>();
	private Skill m_selectedSkill;
	
	private enum State{ SELECT_ACTION, SELECT_SUBACTION, SELECTED }
	private State m_state = State.SELECT_ACTION;
	
	public void render(){
		final double STEP = 20.0;
		StdDraw.setPenColor(StdDraw.WHITE);

		double i = 0;
		for(String e : m_entries){
			StdDraw.textLeft(460, 400+i, e);
			i += STEP;
		}
		StdDraw.textLeft(450,400 + (m_currSelected*STEP), ">");
			
		StdDraw.rectangle(550, 390, 180, 140);
		StdDraw.textLeft(555, 400, m_companion.getName() );
	}
	
	public void setCompanion(Companion c){
		m_companion = c;
		m_state = State.SELECT_ACTION;
	}

	public void resetMenu(){
		m_entries.clear();
		m_entries.add("Attack");
		m_entries.add("Skills");
		m_entries.add("Items");
		m_entries.add("Defend");
		m_currSelected = 0;
		m_selectedSkill = null;
	}
	private void showSkills(){
		m_entries.clear();
		m_currSelected = 0;
		for( Skill e : m_companion.getSkillList() ){
			m_entries.add( e.getName() );
		}
	}
	
	public boolean hasSelected(){
		return m_state == State.SELECTED;
	}
	
	public Skill getSelectedSkill(){
		return m_selectedSkill;
	}
	
	public void handleInput(KeyEvent evt){
		switch( evt.getKeyCode() ){
		
		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
			m_currSelected = m_currSelected-1; 
			if(m_currSelected < 0) m_currSelected = m_entries.size()-1;
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
			m_currSelected = (m_currSelected+1) % m_entries.size();
			break;
		case KeyEvent.VK_ENTER:
			if(m_currSelected == 0 || m_state == State.SELECT_SUBACTION){
				if(m_state == State.SELECT_SUBACTION)
					m_selectedSkill = m_companion.getSkillList().get(m_currSelected);
				else
					m_selectedSkill = m_companion.getAttackSkill();
			
				m_state = State.SELECTED;
			}else{
				m_state = State.SELECT_SUBACTION;
				showSkills();
			}
			break;
		}
	}
}











