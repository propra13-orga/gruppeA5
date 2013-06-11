package game.combat;

import java.awt.event.KeyEvent;

import entity.Companion;

import monster.MonsterGroup;
import monster.MonsterPool;

import std.StdDraw;
import std.StdIO;
import std.StdIO.KeyEventType;
import std.anim.GlobalAnimQueue;
import std.anim.FadeAnim;
import game.player.Player;
import gamestate.GameStates;
import gamestate.GlobalGameState;
import gamestate.IGameState;

public class GSCombat implements IGameState, StdIO.IKeyListener {

	private MonsterPool m_encounterPool = null;
	private MonsterGroup m_encounter = null;

	private static GSCombat s_instance = null;
	
	public static GSCombat getInstance(){
		return s_instance;
	}

	public void prepareEncounter(MonsterPool mp, MonsterGroup m){
		m_encounterPool = mp;
		m_encounter = m;
	}
	
	private enum State{
		PLAYER_ACTION, 
		ENEMY_ACTION,
		MESSAGE
	}
	
	private State m_currState;

	private Party m_enemies;
	private Party m_allies;
	private ActionMenu m_actionMenu = new ActionMenu();

	private String message = "";

	@Override
	public void render() {
		m_enemies.render(/*m_currState == State.TARGET_ENEMY, m_currState == State.ENEMY_ACTION*/);
		m_allies.render(/*m_currState == State.TARGET_ALLY, m_currState != State.ENEMY_ACTION*/);
		
		if(m_currState == State.PLAYER_ACTION )
			m_actionMenu.render();
		
		//Show message box
		if(m_currState == State.MESSAGE){	
			StdDraw.rectangle(25, 390, 400, 140);
			StdDraw.textLeft(25, 400, message);
		}
	}

	@Override
	public void update() {

	}

	@Override
	public void onEnter() {
		m_enemies = EnemyParty.fromMonsterGroup(130, m_encounter);
		m_allies = new AllyParty( 300, Player.getInstance().getCompanions() ); //TODO: currently deletes companions :D
			
		m_allyTurn.onEnter();
		m_actionMenu.setCompanion( (Companion) m_allies.getCurrentHighlighted() );
		m_currState = State.PLAYER_ACTION;
		StdIO.addKeyListener(this, KeyEventType.KeyReleased);
	}
	
	@Override
	public void onExit() {
		StdIO.removeKeyListener(this, KeyEventType.KeyReleased);
	}

	public GSCombat(){
		if(s_instance == null)
			s_instance = this;
		else
			System.out.println("Error! Another instance of GSCombat has been created.");
	}
	
	//*******************************
	// ********** ENEMY *************
	//*******************************
	private EnemyTurn m_enemyTurn = new EnemyTurn();
	private class EnemyTurn{
		private boolean firstEnemy = false;

		public void onEnter(){
			m_currState = State.ENEMY_ACTION;
			
			m_enemies.resetHighlight();
			m_enemies.setRenderTarget(false);
			m_enemies.setRenderHighlight(true);
			m_allies.setRenderTarget(true);
			m_allies.setRenderHighlight(false);
			
			firstEnemy = true;
			onStep();
		}
		public void onStep(){
			m_currState = State.ENEMY_ACTION;
			
			if( m_allies.isDefeated() || m_enemies.isDefeated() ){
				exitCombat();
				return;
			}
			
			message = m_enemies.getCurrentHighlighted().getBasicAttack().applyEffect( m_enemies.getCurrentHighlighted(), m_allies.getCurrentTargeted() );
			m_allies.updateGroup();
				
			if( firstEnemy || m_enemies.highlightNext() ){
				firstEnemy = false;
				m_messageTurn.prepare(MessageTurn.ENEMY);
				m_messageTurn.onEnter();
			}else{
				m_allyTurn.onEnter();
			}
		}
		public void onKey(KeyEvent e){

		}
	}
	
	private AllyTurn m_allyTurn = new AllyTurn();
	private class AllyTurn{
		public void onEnter(){
			m_currState = State.PLAYER_ACTION;
			m_mode = SELECT_ACTION;
			
			m_allies.resetHighlight();
			m_allies.setRenderHighlight(true);
			m_allies.setRenderTarget(false);
			m_enemies.setRenderHighlight(false);
						
			m_actionMenu.resetMenu();
			m_actionMenu.setCompanion( (Companion) m_allies.getCurrentHighlighted() );
		}
		public void onStep(){
			m_currState = State.PLAYER_ACTION;

			if( m_allies.isDefeated() || m_enemies.isDefeated() ){
				exitCombat();
				return;
			}

			if( m_allies.highlightNext() ){
				m_actionMenu.resetMenu();
				m_actionMenu.setCompanion( (Companion) m_allies.getCurrentHighlighted() );
				m_currState = State.PLAYER_ACTION;
				m_enemies.setRenderTarget(false);
				m_mode = SELECT_ACTION;
			}else{
				m_enemyTurn.onEnter();
				m_messageTurn.prepare(MessageTurn.ENEMY);
			}
		
		}
		
		private int m_mode;
		private static final int SELECT_ACTION = 0;
		private static final int SELECT_TARGET = 1;
		
		private void handleSelectAction(KeyEvent e){	
			m_actionMenu.handleInput(e);
			
			if(m_actionMenu.hasSelected()){
				m_enemies.setRenderTarget(true);
				m_mode = SELECT_TARGET;
			}
		}

		private void handleTargetEnemy(KeyEvent e){
			switch( e.getKeyCode() ){
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A:
				m_enemies.targetPrev(); break;
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D:
				m_enemies.targetNext(); break;
			case KeyEvent.VK_ENTER:
				
				message = m_actionMenu.getSelectedSkill().applyEffect( m_allies.getCurrentHighlighted(), m_enemies.getCurrentTargeted() );
				m_enemies.updateGroup();
				
				GlobalAnimQueue.playAnimation(  new FadeAnim(210, 210, "data/cut.png", 30) );
				
				m_messageTurn.prepare(MessageTurn.ALLY);
				m_messageTurn.onEnter();
				
				
				
				break;
			}
		}
		
		public void onKey(KeyEvent e){
			if(m_mode == SELECT_ACTION)
				handleSelectAction(e);
			else
				handleTargetEnemy(e);
		/*
			switch( e.getKeyCode() ){
			case KeyEvent.VK_ENTER:
			
				
				
				
				break;
			}*/
		}
		
		
	}
	
	/*
	 * Änderung:
	 * 
	 * Turn-classes jetzt:
	 * 	- onStep() - MessageTurn führt immer onStep() aus
	 * 	- start()/reset() - resetted das object für neuen turn
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	
	
	private MessageTurn m_messageTurn = new MessageTurn();
	private class MessageTurn{
		private int m_which;
		public static final int ALLY = 0;
		public static final int ENEMY = 1;
		public void prepare(int which){
			m_which = which;
		}
	
		public void onEnter(){
			m_currState = State.MESSAGE;
		}
		public void onStep(){
		
		}
		public void onKey(KeyEvent e){
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				
				if(m_which==1){
					m_enemyTurn.onStep();
				}else{
					m_allyTurn.onStep();
				}
			}
		}
	}

	public void exitCombat(){
		m_encounterPool.removeMonster(m_encounter);
		FadeAnim anim = new FadeAnim(m_encounter.getX(),m_encounter.getY(), m_encounter.mPath);
		GlobalAnimQueue.playAnimation( anim );
		GlobalGameState.setActiveGameState(GameStates.GAME);
	}

	@Override
	public void receiveEvent(KeyEvent e) {
	
		if(m_currState == State.ENEMY_ACTION)
			m_enemyTurn.onKey(e);
		else if(m_currState == State.PLAYER_ACTION)
			m_allyTurn.onKey(e);
		else if(m_currState == State.MESSAGE)
			m_messageTurn.onKey(e);
	}

}







