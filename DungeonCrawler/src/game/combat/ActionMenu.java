package game.combat;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import entity.Companion;
import game.inventory.Inventory;
import game.item.ItemInstance;
import game.player.Player;
import game.skill.Skill;

import std.StdDraw;

public class ActionMenu {
	private Companion m_companion;
	private int m_currSelected = 0;
	
	private List<Object> m_selectedEntry = new ArrayList<>();
	private List<String> m_entries = new ArrayList<>();
	private List<String> m_descriptions = new ArrayList<>();
	private boolean m_emptyList;
	
	private enum State{ SELECT_ACTION, SELECT_SUBACTION, SELECTED }
	public enum Option{ SELECT_SKILL, SELECT_ITEM, SELECT_ATTACK };
	private Option m_option;
	private State m_state = State.SELECT_ACTION;
	
	public void render(){
		final double STEP = 20.0;
		StdDraw.setPenColor(StdDraw.WHITE);

		double y = 0;
		int i = 0;
		for(String e : m_entries){
			if(m_currSelected==i)
				StdDraw.setPenColor(StdDraw.RED);
			StdDraw.textLeft(440, 400+y, e);
			if(m_currSelected==i)
				StdDraw.setPenColor(StdDraw.WHITE);
			y += STEP;
			i += 1;
		}

		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.textLeft(430,400 + (m_currSelected*STEP), ">");
			
		StdDraw.setPenColor(StdDraw.WHITE);
		StdDraw.rectangle(550, 390, 220, 180);

		StdDraw.textLeftFmt(555, 405, m_descriptions.get(m_currSelected) );

		
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
		
		m_descriptions.clear();
		m_descriptions.add("Performs a basic attack.");
		m_descriptions.add("Opens a sub-menu to choose\navailable skills.");
		m_descriptions.add("Opens a sub-menu to choose\nfrom available items.");
		m_descriptions.add("Defends against the next\nattack.");
		
		m_currSelected = 0;
		m_emptyList = false;
		m_state = State.SELECT_ACTION;
	}
	
	private void showItems(){
		m_entries.clear();
		m_descriptions.clear();
		m_selectedEntry.clear();
		
		m_currSelected = 0;
		
		Inventory inv = Player.getInstance().getInventory();
		
		
		for( ItemInstance ii : inv.getItemList() ){
			if( ii==null || !ii.isUsable() )
				continue;
		
			m_selectedEntry.add( ii );
			m_entries.add( ii.getName() );
			m_descriptions.add( ii.getDescription() );
		}
		
		if(m_selectedEntry.isEmpty()){
			m_emptyList = true;
			
			m_entries.add("<none>");
			m_descriptions.add("No available options.");
		}	
	}
	
	private void showSkills(){
		m_entries.clear();
		m_descriptions.clear();
		m_selectedEntry.clear();
		
		m_currSelected = 0;
		for( Skill e : m_companion.getSkillList() ){
			if( !m_companion.canCastSkill(e) )
				continue;
		
			m_selectedEntry.add( e );
			m_entries.add( e.getName() );
			m_descriptions.add( e.getDescription() );
		}
		
		if(m_selectedEntry.isEmpty()){
			m_emptyList = true;
			
			m_entries.add("<none>");
			m_descriptions.add("No available options.");
		}
	}
	
	public boolean hasSelected(){
		return m_state == State.SELECTED;
	}
	
	public Option getSelectedOption(){
		return m_option;
	}
	
	public Skill getSelectedBasicAttack(){
		if(m_option != Option.SELECT_ATTACK)
			System.out.println("Error! Wrong selection taken!");
		return m_companion.getBasicAttack();
	}
	public Skill getSelectedSkill(){
		if(m_option != Option.SELECT_SKILL)
			System.out.println("Error! Wrong selection taken!");
		return (Skill) m_selectedEntry.get(m_currSelected);
	}
	public ItemInstance getSelectedItemInstance(){
		if(m_option != Option.SELECT_ITEM)
			System.out.println("Error! Wrong selection taken!");
		return (ItemInstance) m_selectedEntry.get(m_currSelected);
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
			if(m_state == State.SELECT_SUBACTION){
				if(m_emptyList)
					return;
			
				m_state = State.SELECTED;
			}else{
				if(m_currSelected == 0){ //Attack
					m_option = Option.SELECT_ATTACK;
					m_state = State.SELECTED;
				}else if(m_currSelected == 1){	//Skill
					m_option = Option.SELECT_SKILL;
					showSkills();
					m_state = State.SELECT_SUBACTION;
				}else if(m_currSelected == 2){ //Item
					m_option = Option.SELECT_ITEM;
					showItems();
					m_state = State.SELECT_SUBACTION;
				}
			}
			break;
		case KeyEvent.VK_ESCAPE:
			if(m_state == State.SELECT_SUBACTION){
				resetMenu();
			}
			break;
		
		}
	}
}











