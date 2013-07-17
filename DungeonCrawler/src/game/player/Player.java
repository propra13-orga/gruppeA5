package game.player;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import network.NetworkManager;
import network.msg.EnterCombatMessage;
import network.msg.MoveMessage;
import network.msg.PositionMessage;

import entity.Companion;
import entity.IEntity;
import game.GSTransition;
import game.HitBox;
import game.checkpoint.Checkpoint;
import game.combat.GSCombat;
import game.interaction.GSInteraction;
import game.inventory.Inventory;
import game.item.ItemInstance;
import game.item.PickupPool;
import game.item.PickupableItem;
import gamestate.GameStates;
import gamestate.GlobalGameState;

import map.CellInfo;
import map.Coordinate;
import map.Map;
import monster.MonsterGroup;
import monster.MonsterPool;

import std.StdDraw;
import std.StdIO;

public class Player {
	
	private boolean standsOnTeleporter = false;
	private boolean standsOnEvent = false;
	
	private double playerX, playerY; //Die Koordinaten des Spielers 
	//Hitbox
	private double xMin = 6; 
	private double xMax = 23;
	private double yMin = 2;
	private double yMax = 30;
	
	private HitBox m_hitBox = new HitBox(4, 2, 26, 31);
	
	private int m_updateCounter = 0;
	
	private boolean m_isMovable = true;
	
	public void setMovable(boolean movable){
		m_isMovable = movable;
	}
	
	//TODO: Get rid of those two default methods ... eventually..
	private List<Companion> m_companions = Companion.getDefaultCompanionList();
	
	public List<Companion> getCompanions(){
		return m_companions;
	}
	
	private Inventory m_inventory = new Inventory();
	public Inventory getInventory(){
		return m_inventory;
	}
	
	private int m_gold = 600;
	
	public int getGold(){
		return m_gold;
	}
	public void addGold(int gold){
		m_gold += gold;
	}
	public void removeGold(int gold){
		m_gold = Math.max(0, m_gold-gold);
	}
	
	
	//Der Spieler lebt solange er mindestens ein Leben hat
	//Wird der Spieler verwundet wird ein Leben abgezogen
	public boolean isAlive(){ 
		//TODO: NYI
		return true;
	}
	
	public void setPosition(double x, double y){
		playerX = x;
		playerY = y;
	}
	public double getX(){
		return playerX;
	}
	public double getY(){
		return playerY;
	}
	
	private static Player s_player = null;
	
	public static Player getInstance(){
		return s_player;
	}
	
	public Player(){
		s_player = this;
	}
	
	
	//Beim Betreten eines Teleporterfelds wird die nächste Map geladen
	//Der Spieler nimmt die in der MapDatei gespeicherte Position ein
	public void enterTeleporter(Map map){
		CellInfo ti = map.getCellInfo(map.getGridX(playerX+16),map.getGridY(playerY+16));
		
		if (ti != null && ti.mHasTeleporter){
		
			//Falls der Spieler gerade auf einem Teleporter steht, wird er nicht teleportiert.
			//Ansonsten würde man die ganze Zeit hin-und-her teleportiert werden wenn man einen Teleporter betritt.
			if(standsOnTeleporter == true)
				return;
		
			standsOnTeleporter = true;
			String newMapName = ti.mNewMap;
			Coordinate newMapPosition = ti.mNewPosition;
			map.loadLevel(newMapName);
			playerX = map.getCanvasX(newMapPosition.mX);
			playerY = map.getCanvasY(newMapPosition.mY);
			
		}else{
			standsOnTeleporter = false;
		}
	}
	
	private void lookForEvents(Map map){
		CellInfo ti = map.getCellInfo(map.getGridX(playerX+16),map.getGridY(playerY+16));
		
		if (ti != null && ti.mHasEvent){
			
			if(standsOnEvent == true)
				return;
			
			standsOnEvent = true;
			if(ti.mEventID == 2){
				GlobalGameState.setActiveGameState( GameStates.SHOP );
			}
			if(ti.mEventID == 3){
				GlobalGameState.setActiveGameState( GameStates.QUEST );
			}
			if(ti.mEventID == 4){
				Checkpoint.save();
			}
			if(ti.mEventID == 5){
				GlobalGameState.setActiveGameState(GameStates.MAIN_MENU);
			}
			
		}else{
			standsOnEvent = false;
		}
	}
	
	private void lookForEnemy(Map map) {
	
		MonsterPool mp = map.getMonsterPool();
		
		m_hitBox.setPosition(playerX, playerY);
		MonsterGroup m = mp.collisionAt( m_hitBox );
		
		if(m == null)
			return;
			
		//Init multiplayer data transfer
		if( NetworkManager.isMultiplayer() ){
		
			if( !NetworkManager.isHost() ){
				return; //Do not want to initiate fights when not host.
			}
		
			ArrayList<IEntity> entityList = new ArrayList<>();
			for( Companion c : m_companions )
				entityList.add(c);
			
			System.out.println("Sending CombatMessage with " + entityList.size() + " entities.");
			NetworkManager.send( new EnterCombatMessage(entityList) );
		}
		
		GSCombat.getInstance().prepareEncounter(mp, m);
		GSTransition.getInstace().prepareTransition( GameStates.COMBAT );
		GlobalGameState.setActiveGameState(GameStates.TRANSITION);
	}
	
	public void attempInteraction(){
		Map map = Map.getInstance();
		PickupPool pp = map.getPickupPool();
		int x = map.getGridX(playerX + 11);
		int y = map.getGridY(playerY + 15);
		
		PickupableItem pi = pp.PickupableAt( x, y );
		
		if(pi != null){
			if(m_inventory.isFull())
				GSInteraction.setText("You have found a " + pi.getItemType().getName() + ", but\nyour inventory is full.");
			else{
				m_inventory.addItem( new ItemInstance(pi.getItemType()) );
				pp.removePickupable(pi);
				GSInteraction.setText("You have found a " + pi.getItemType().getName() + "!");
			}
			GlobalGameState.setActiveGameState(GameStates.INTERACT);
		}
	
		
	}
	
	//Bei Tasteneingabe wird überprüft, ob das angestrebte Feld mit der Hitbox kollidiert
	//Wenn nicht wird der Spieler auf das angestrebte Feld bewegt
	public void update(Map map){
		
		if( m_isMovable ){
		
			//TODO: Use HitBox for that
			if( StdIO.isKeyPressed(KeyEvent.VK_W) || StdIO.isKeyPressed(KeyEvent.VK_UP)) {
				if (map.isPathable(map.getGridX(playerX + xMin),map.getGridY(playerY-3 + yMin)) 
						&& map.isPathable(map.getGridX(playerX + xMax), map.getGridY(playerY-3 + yMin))){
					playerY-=3;
				}
			}
			else if( StdIO.isKeyPressed(KeyEvent.VK_S) || StdIO.isKeyPressed(KeyEvent.VK_DOWN)) {
				if (map.isPathable(map.getGridX(playerX + xMin),map.getGridY(playerY+3 + yMax)) 
						&& map.isPathable(map.getGridX(playerX + xMax), map.getGridY(playerY+3 + yMax))){
					playerY +=3;
				}
			}
			if( StdIO.isKeyPressed(KeyEvent.VK_A) || StdIO.isKeyPressed(KeyEvent.VK_LEFT)) {
				if (map.isPathable(map.getGridX(playerX-3 + xMin),map.getGridY(playerY + yMin)) 
						&& map.isPathable(map.getGridX(playerX-3 + xMin),map.getGridY(playerY + yMax))){
					playerX -=3;
				}
			}
			else if( StdIO.isKeyPressed(KeyEvent.VK_D) || StdIO.isKeyPressed(KeyEvent.VK_RIGHT)) {
				if (map.isPathable(map.getGridX(playerX+3 + xMax),map.getGridY(playerY + yMin)) 
						&& map.isPathable(map.getGridX(playerX+3 + xMax),map.getGridY(playerY + yMax))){
					playerX +=3;
				}
			}	
		
		}
		
		m_updateCounter++;
		
		if(m_updateCounter > 20){
			m_updateCounter = 0;
			NetworkManager.send( new PositionMessage(playerX, playerY) );
		}else if(m_updateCounter % 4 == 0){
			NetworkManager.send( new MoveMessage(playerX, playerY) );
		}
		
		
		//Suche nach einem Teleporter auf dem momentanen Feld.
		enterTeleporter(map);
		lookForEnemy(map);
		lookForEvents(map);
	}
	
	
	//Zeichnet den Spieler an die Stelle im Koordinatensystem
	public void render(){
		Companion c = m_companions.get(0);
		
		if(c == null){
			System.out.println("Error! No companion found to render in Player.java!");
			return;
		}
		c.render(playerX, playerY);

		Map map = Map.getInstance();
		double x = map.getCanvasX(map.getGridX(playerX+16));
		double y = map.getCanvasY(map.getGridY(playerY+16));
		
		StdDraw.setPenColor(StdDraw.YELLOW);
		StdDraw.square(x, y, 32);
		
		m_hitBox.render();
	}
	
}
