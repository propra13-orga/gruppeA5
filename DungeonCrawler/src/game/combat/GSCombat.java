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
import game.GSTransition;
import game.checkpoint.Checkpoint;
import game.item.ItemInstance;
import game.player.Player;
import game.skill.Skill;
import game.skill.TargetType;
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
	private CombatLog m_combatLog = new CombatLog(); //recreated in onEnter()?

	@Override
	public void render() {
	
		StdDraw.picture(0,0, "data/ui/background.png");
		
		StdDraw.setAlpha(0.5f);
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.filledRectangle(0,0,800,600);
		StdDraw.setAlpha(1.0f);
	
		m_enemies.render();
		m_allies.render();
		
		if(m_currState == State.PLAYER_ACTION )
			m_actionMenu.render();
		
		//Show message box
		if(m_currState == State.MESSAGE){	
			m_combatLog.render();
		}
	}

	@Override
	public void update() {

	}

	@Override
	public void onEnter() {
		m_enemies = EnemyParty.fromMonsterGroup(130, m_encounter);
		m_allies = new AllyParty( 300, Player.getInstance().getCompanions() );
			
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
			
			if( m_allies.isDefeated() ){
				exitCombatPlayerDefeat();
				return;
			}else if( m_enemies.isDefeated() ){
				exitCombatPlayerVictory();
				return;
			}

			if( firstEnemy || m_enemies.highlightNext() ){
				firstEnemy = false;
				
				m_enemies.getCurrentHighlighted().getBasicAttack().applyEffect(m_enemies.getCurrentHighlighted(), m_allies, m_combatLog);
				m_allies.updateGroup();
				
				m_messageTurn.prepare(NextTurn.ENEMY);
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
		private Party m_targetParty;
	
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

			if( m_allies.isDefeated() ){
				exitCombatPlayerDefeat();
				return;
			}else if( m_enemies.isDefeated() ){
				exitCombatPlayerVictory();
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
				m_messageTurn.prepare(NextTurn.ENEMY);
			}
		
		}
		
		private int m_mode;
		private Skill m_selectedSkill;
		private ItemInstance m_selectedItemInstance;
		private static final int SELECT_ACTION = 0;
		private static final int SELECT_TARGET = 1;
		
		private void useSkill(){
			m_selectedSkill.applyEffect( m_allies.getCurrentHighlighted(), m_targetParty, m_combatLog );
			
			if(m_selectedItemInstance != null)
				Player.getInstance().getInventory().removeItem(m_selectedItemInstance);
			
			m_enemies.updateGroup();
		}
		
		
		private void handleSelectAction(KeyEvent e){	
			m_actionMenu.handleInput(e);
			
			if(m_actionMenu.hasSelected()){
				Skill sel;
				ItemInstance ii = null;
				ActionMenu.Option op = m_actionMenu.getSelectedOption();
				
				if(op == ActionMenu.Option.SELECT_ATTACK){
					sel = m_actionMenu.getSelectedBasicAttack();
				}else if(op == ActionMenu.Option.SELECT_SKILL)
					sel = m_actionMenu.getSelectedSkill();
				else /*if(op == ActionMenu.Option.SELECT_ITEM)*/{
					ii = m_actionMenu.getSelectedItemInstance();
					sel = ii.getUseInfo().getSkill();
				}
				
				m_selectedItemInstance = ii;
				m_selectedSkill = sel;
				
				switch(sel.getTargetType()){
				
				case ENEMY_SINGLE:
					m_targetParty = m_enemies;
					m_targetParty.setRenderTarget(true);
					m_mode = SELECT_TARGET;
					break;
					
				case ENEMY_MULTI:
					m_targetParty = m_enemies;
					useSkill();
					m_messageTurn.prepare(NextTurn.ALLY);
					m_messageTurn.onEnter();
					break;
				
				case ALLY_SINGLE:
					m_targetParty = m_allies;
					m_targetParty.setRenderTarget(true);
					m_mode = SELECT_TARGET;
					break;
					
				case ALLY_MULTI:
					m_targetParty = m_allies;
					useSkill();
					m_messageTurn.prepare(NextTurn.ALLY);
					m_messageTurn.onEnter();
					break;
					
				case SELF:
					m_targetParty = m_allies;
					useSkill();
					m_messageTurn.prepare(NextTurn.ALLY);
					m_messageTurn.onEnter();
					break;
					
				default:
					System.out.println("TargetType not yet implemented");
					break;
					
				}
			
			}
		}

		private void handleTargetEnemy(KeyEvent e){
			switch( e.getKeyCode() ){
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A:
				m_targetParty.targetPrev(); break;
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D:
				m_targetParty.targetNext(); break;
			case KeyEvent.VK_ENTER:
				
				useSkill();
				m_messageTurn.prepare(NextTurn.ALLY);
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
	public enum NextTurn{ALLY, ENEMY};
	private class MessageTurn{
		private NextTurn m_which;
				
		public void prepare(NextTurn which){
			m_which = which;
		}
	
		public void onEnter(){
			m_currState = State.MESSAGE;
		}
		public void onStep(){
		
		}
		public void onKey(KeyEvent e){
			if(e.getKeyCode() != KeyEvent.VK_ENTER)
				return;
				
			m_combatLog.next();
				
			if( !m_combatLog.isEmpty()  ){
				return;
			}
				
			if(m_which==NextTurn.ENEMY){
				m_enemyTurn.onStep();
			}else{
				m_allyTurn.onStep();
			}
			
		}
	}

	private void exitCombatPlayerDefeat(){
		//Activate last check point
		Checkpoint.load();
		//GlobalGameState.setActiveGameState(GameStates.DEFEAT);
		GSTransition.getInstace().prepareTransition( GameStates.DEFEAT );
		GlobalGameState.setActiveGameState(GameStates.TRANSITION);
	}

	private void exitCombatPlayerVictory(){
		m_encounterPool.removeMonster(m_encounter);
		FadeAnim anim = new FadeAnim(m_encounter.getX(),m_encounter.getY(), m_encounter.mIcon);
		GlobalAnimQueue.playAnimation( anim );
		GlobalGameState.setActiveGameState(GameStates.GAME);
		
		//DEBUG
		Player.getInstance().addGold(100);
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







