package game.shop;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;

import std.StdDraw;
import std.StdIO;
import std.StdIO.KeyEventType;
import game.inventory.Inventory;
import game.item.ItemInstance;
import game.item.ItemType;
import game.player.Player;
import gamestate.GameStates;
import gamestate.GlobalGameState;
import gamestate.IGameState;


public class GSShop implements IGameState, StdIO.IKeyListener {
	private Inventory m_inv;
	private int m_currSelected;
	private Player m_player;

	private ArrayList<ItemType> m_purchasables = new ArrayList<>();

	public GSShop(){
		Set<Entry<String, ItemType>> k = ItemType.getItemTypeList();
		
		for( Entry<String, ItemType> e : k ){
			m_purchasables.add( e.getValue() );
		}
	}

	private void renderShop(double baseX, double baseY){
		double y = 0;
		int i = 0;
		
		for( ItemType it : m_purchasables ){
		
			if( i== m_currSelected )
				StdDraw.setPenColor(StdDraw.RED);
			else
				StdDraw.setPenColor(StdDraw.BLACK);
			
			StdDraw.rectangle(baseX, baseY+y, 250, 43);
			
			StdDraw.setAlpha(0.15f);
			StdDraw.filledRectangle(baseX, baseY+y, 250, 43);
			StdDraw.setAlpha(1.0f);
			
			//StdDraw.picture(baseX, baseY + y, "data/slot.png");
			StdDraw.picture(baseX, baseY + y + 6, it.getIcon());
			
			int price = m_purchasables.get(i).getPrice();
			
			if( m_player.getGold() >= price )
				StdDraw.setPenColor(StdDraw.WHITE);
			else
				StdDraw.setPenColor(StdDraw.RED);
			StdDraw.textLeft( baseX+38, baseY + y + 16, it.getName() );
			StdDraw.textLeft( baseX+38, baseY + y + 34, price + " Gold" );
			
			y += 44;
			i += 1;
		}
		
	}

	private void renderInventory(double baseX, double baseY){
		for(int y = 0; y < 4; y++){
			for(int x = 0; x < 4; x++){
				StdDraw.picture(baseX + x*32, baseY + y*32, "data/slot.png");
				
				ItemInstance ii = m_inv.getItem(x,y);
				if(ii != null)
					StdDraw.picture(baseX + x*32, baseY + y*32, ii.getIcon());
			}
		}

	}

	@Override
	public void render() {
		StdDraw.picture(0, 0, "data/ui/background.png");
		StdDraw.text(400, 50, "Current gold: " + m_player.getGold());
		renderShop(30,30);
		renderInventory(400,300);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void receiveEvent(KeyEvent e) {
	
		switch(e.getKeyCode()){
		
		case KeyEvent.VK_DOWN:
			m_currSelected += 1;
			m_currSelected %= m_purchasables.size();
			break;
			
			
		case KeyEvent.VK_UP:
			m_currSelected -= 1;
			m_currSelected = (m_currSelected>=0 ? m_currSelected : m_purchasables.size()-1);
			break;
			
		case KeyEvent.VK_ENTER:	
			int price = m_purchasables.get(m_currSelected).getPrice();
			if( !m_inv.isFull() && m_player.getGold() >= price ){
				m_player.removeGold( price );
				m_inv.addItem( new ItemInstance(m_purchasables.get(m_currSelected)) );
			}
				
			break;
			
		case KeyEvent.VK_ESCAPE:
			GlobalGameState.setActiveGameState( GameStates.GAME );
			break;
			
		
		}

	}
	
	@Override
	public void onEnter() {
		m_player = Player.getInstance();
		m_inv = m_player.getInventory();
		m_currSelected = 0;
	
		StdIO.addKeyListener(this, KeyEventType.KeyReleased);
	}

	@Override
	public void onExit() {
		StdIO.removeKeyListener(this, KeyEventType.KeyReleased);
	}
}
