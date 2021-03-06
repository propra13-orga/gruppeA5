package game.inventory;
import java.awt.event.KeyEvent;
import java.util.List;

import entity.Companion;
import entity.UnitStats;

import std.StdDraw;
import std.StdIO;
import std.StdIO.KeyEventType;
import game.item.ItemInstance;
import game.player.Player;
import gamestate.GameStates;
import gamestate.GlobalGameState;
import gamestate.IGameState;


public class GSInventory implements IGameState, StdIO.IKeyListener {
	private Inventory m_inv;
	private List<Companion> m_comp;
	
	private int m_selX = 0;
	private int m_selY = 0;
	
	private int m_selCompanion = 0;
	private int m_maxCompanions;
	
	private enum State{INVENTORY, COMPANION};
	private State m_state;

	private void renderInventory(double baseX, double baseY){
		for(int y = 0; y < 4; y++){
			for(int x = 0; x < 4; x++){
				StdDraw.picture(baseX + x*32, baseY + y*32, "data/slot.png");
				
				ItemInstance ii = m_inv.getItem(x,y);
				if(ii != null)
					StdDraw.picture(baseX + x*32, baseY + y*32, ii.getIcon());
			}
		}
		
		StdDraw.picture(baseX + m_selX*32, baseY + m_selY*32, "data/cursor.png");
	}
	
	private void renderItemInfo(double baseX, double baseY){
		ItemInstance ii = m_inv.getItem(m_selY*4 + m_selX);
		
		if(ii == null) return;
		
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.rectangle(baseX-5, baseY-12, 250, 200);
		
		StdDraw.setAlpha(0.15f);
		StdDraw.filledRectangle(baseX-5, baseY-12, 250, 200);
		StdDraw.setAlpha(1.0f);
		
		StdDraw.setPenColor(StdDraw.WHITE);
		StdDraw.textLeft(baseX, baseY, 		ii.getName());
		
		if( ii.isEquipable() ){
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.textRight(baseX+240, baseY, ii.getEquipInfo().getEquipSlot().toString());
		}
		
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.textLeftFmt(baseX, baseY + 20, ii.getDescription());
		
		//StdDraw.textLeft(baseX, baseY + 90, (ii.isUsable() ? "Can" : "Cannot") + " be used.");
		//StdDraw.textLeft(baseX, baseY + 110, (ii.isEquipable() ? "Can" : "Cannot") + " be equipped.");
	}
	
	private void renderEquipmentSlot(double x, double y, Equipment eq, EquipSlot slot){
		StdDraw.picture(x, y, "data/slot.png"); 
		
		ItemInstance ii = eq.getEquippedItem(slot);
		if(ii != null)
			StdDraw.picture(x, y, ii.getIcon());
	}
	
	private void renderCompanion(double baseX, double baseY, Companion c, boolean selected){
		
		
		if(selected && m_state == State.COMPANION){
			StdDraw.setPenColor( StdDraw.RED  );
			
			StdDraw.setAlpha(0.05f);
			StdDraw.filledRectangle(baseX-2, baseY-12, 260, 125 );
			StdDraw.setAlpha(1.0f);
			
			StdDraw.rectangle(baseX-2, baseY-12, 260, 125 );
		}
		
		Equipment eq = c.getEquipment();

		//Equipment slots
		renderEquipmentSlot(baseX + 154, baseY + 0, eq, EquipSlot.HEAD);
		
		renderEquipmentSlot(baseX + 100, baseY + 36, eq, EquipSlot.HAND);
		renderEquipmentSlot(baseX + 136, baseY + 36, eq, EquipSlot.COAT);
		renderEquipmentSlot(baseX + 172, baseY + 36, eq, EquipSlot.TORSO);
		renderEquipmentSlot(baseX + 208, baseY + 36, eq, EquipSlot.OFFHAND);
		
		renderEquipmentSlot(baseX + 136, baseY + 72, eq, EquipSlot.BOOTS);
		renderEquipmentSlot(baseX + 172, baseY + 72, eq, EquipSlot.LEGS);
		
		
		StdDraw.setPenColor( StdDraw.WHITE  );
		
		StdDraw.textLeft(baseX, baseY, c.getName());
		c.render(baseX, baseY + 10);
		StdDraw.textLeft(baseX, baseY + 55, "Human male");
		StdDraw.textLeft(baseX, baseY + 75, "Level 1");
		StdDraw.textLeft(baseX, baseY + 95, "Physical: " + c.getStats().mArmor);
		StdDraw.textLeft(baseX, baseY + 115, "Fire: " + c.getStats().mFireResist);
		StdDraw.textLeft(baseX, baseY + 135, "Ice: " + c.getStats().mIceResist);


		//Draw health bar
		final UnitStats s = c.getStats();
		double healthRatio = s.mCurrHealth / (double)s.mMaxHealth;
		double healthBarLength = 32. * healthRatio;

		StdDraw.setPenColor(StdDraw.GREEN);
		StdDraw.filledRectangle(baseX+35, baseY+10,healthBarLength,4);
		
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.rectangle(baseX+35, baseY+10,32,4);
		
	}
	private void renderCompanions(double baseX, double baseY){
		double y = 0;
		int i = 0;
		for(Companion c : m_comp){
		
			renderCompanion(baseX, baseY + y, c, i == m_selCompanion);
			
			i += 1;
			y += 200;
		}
	}

	@Override
	public void render() {
		StdDraw.picture(0, 0, "data/ui/background.png"); 
		StdDraw.text(400, 20, "I N V E N T O R Y");
	
		renderCompanions(5,50);
		renderInventory(300, 300);
		renderItemInfo(435, 310);
	}

	@Override
	public void update() {
		
	}
	
	private int mod(int n, int mod){
		int r = n % mod;
		if (r < 0)
		{
		    r += mod;
		}
		return r;
	}

	/**
	 * Listener method that is called during Companion selection when equipping items
	 * @param e		KeyEvent to be looked at
	 */
	public void processEventCompanion(KeyEvent e){
		switch(e.getKeyCode()){ 
		case KeyEvent.VK_DOWN: m_selCompanion 	= mod(m_selCompanion+1, m_maxCompanions ); break;
		case KeyEvent.VK_UP: m_selCompanion  	= mod(m_selCompanion-1, m_maxCompanions ); break;
		case KeyEvent.VK_ESCAPE:
			m_state = State.INVENTORY;
			break;
		case KeyEvent.VK_I:
			GlobalGameState.setActiveGameState( GameStates.GAME );
			break;
		}
	
		if( e.getKeyCode() == KeyEvent.VK_ENTER ){
			
			Companion c = m_comp.get(m_selCompanion);
			
			ItemInstance it = m_inv.getItem(m_selX, m_selY);
			//if(it == null || it.isEquipable() == false) return;
			
			ItemInstance i = c.getEquipment().equipItem( it, it.getEquipInfo().getEquipSlot() );
			m_inv.removeItem(m_selX, m_selY);
			if(i != null) m_inv.addItem(i);
		
			m_state = State.INVENTORY;
		
		}
	}

	/**
	 * Key listener method that is called during inventory browsing
	 * @param e
	 */
	public void processEventInventory(KeyEvent e) {
		switch(e.getKeyCode()){ 
		case KeyEvent.VK_DOWN: m_selY 	= mod(m_selY+1, m_inv.getHeight() ); break;
		case KeyEvent.VK_UP: m_selY  	= mod(m_selY-1, m_inv.getHeight() ); break;
		case KeyEvent.VK_RIGHT: m_selX  = mod(m_selX+1, m_inv.getWidth()  ); break;
		case KeyEvent.VK_LEFT: m_selX  	= mod(m_selX-1, m_inv.getWidth()  ); break;
		case KeyEvent.VK_ESCAPE:
		case KeyEvent.VK_I:
			GlobalGameState.setActiveGameState( GameStates.GAME );
			break;
			
		case KeyEvent.VK_ENTER:
			ItemInstance it = m_inv.getItem(m_selX, m_selY);
			if(it == null || it.isEquipable() == false) 
			return;
		
			m_state = State.COMPANION; 
			
			break;
		}
	
	}
	
	@Override
	public void receiveEvent(KeyEvent e) {
		if(m_state == State.INVENTORY)
			processEventInventory(e);
		else
			processEventCompanion(e);
	}
	
	@Override
	public void onEnter() {
		m_inv = Player.getInstance().getInventory();
		m_comp = Player.getInstance().getCompanions();
		m_state = State.INVENTORY;
		
		m_selCompanion = 0;
		m_maxCompanions = m_comp.size();
	
		StdIO.addKeyListener(this, KeyEventType.KeyReleased);
	}

	@Override
	public void onExit() {
		StdIO.removeKeyListener(this, KeyEventType.KeyReleased);
	}
}
