package toolbar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import map.EditorTileList.EditorTileType;
import map.EditorTileList.TileVariation;

public class Palette {
	private final static int ICON_SIZE = 32;
	private final static int PANEL_BORDER_LEFT = 10;
	private final static int PANEL_BORDER_RIGHT = 10;
	private final static int PANEL_BORDER_TOP = 16;
	private final static int PANEL_BORDER_BOT = 10;

	private int m_maxRowLength;
	private int m_maxColHeight;
	private JPanel m_panel;

	private TilesPanel m_tilesPanel;
	private ArrayList<JButton> m_tileButtons = new ArrayList<JButton>();

	private JButton m_currentlySelected = null;
	private EditorTileType m_currentlySelectedTileType = null;

	Random m_random = new Random( Double.doubleToLongBits(Math.random()) ); //Seed RNG

	public char getCurrentlySelectedTileId(){
		if(m_currentlySelectedTileType.mVariations == null)
			return m_currentlySelectedTileType.mId;
		else{
			ArrayList<TileVariation> vars = m_currentlySelectedTileType.mVariations;
			int maxWeight = vars.get( vars.size() - 1 ).mWeight;
			
			int randWeight = m_random.nextInt(maxWeight);
			
			for(int i = 0; i < vars.size(); i++){
				if( randWeight < vars.get(i).mWeight )
					return vars.get(i).mId;
			}
			//Error.. kinda
			return m_currentlySelectedTileType.mVariations.get(0).mId;
		}
	}
	
	public void deselect(){
		if(m_currentlySelected != null)
  		  m_currentlySelected.setBorderPainted(false);
	}

	private void addIcon(int x, int y, final EditorTileType tt){
		final JButton btn = new JButton( new ImageIcon(tt.mFilePath) );
		//final int id = tt.mId;
		final Palette localThis = this;

		btn.setBorderPainted(false);
		btn.setOpaque(true);
		Border thickBorder = new LineBorder(Color.RED, 2);
		btn.setBorder(thickBorder);
		btn.setBounds(new Rectangle(new Point(PANEL_BORDER_LEFT+x,PANEL_BORDER_TOP+y), new Dimension(32,32)));
		btn.setToolTipText(tt.mName);
		
		btn.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent evt) {
		    	  
		    	  if(m_currentlySelected != null)
		    		  m_currentlySelected.setBorderPainted(false);
		    	  
		    	  m_currentlySelected = (JButton) evt.getSource();
		    	  m_currentlySelected.setBorderPainted(true);
		    	  m_currentlySelectedTileType = tt;
		    	  
		    	  m_tilesPanel.receiveSelectionEvent(localThis);
		      }
		});
		
		m_panel.add(btn);
		m_tileButtons.add(btn);
	}
	
	public Palette(JPanel panel, TilesPanel tilesPanel){
		m_tilesPanel = tilesPanel;
		JPanel p = new JPanel(null);
		m_panel = p;
		
		p.setVisible(true);
		panel.add(p);
	}
	
	public void setPalette(String name, int x, int y, int length, ArrayList<EditorTileType> arrayList){
		int height = arrayList.size() / length + 1;
		
		m_maxRowLength = length;
		m_maxColHeight = height;
		
		m_panel.setBounds(x, y, length*ICON_SIZE + PANEL_BORDER_LEFT + PANEL_BORDER_RIGHT, height*ICON_SIZE + PANEL_BORDER_TOP + PANEL_BORDER_BOT);
		
		m_panel.setBorder(null);
		TitledBorder b = BorderFactory.createTitledBorder(name);
		m_panel.setBorder(b);
		
		for( JButton btn : m_tileButtons ){
			ActionListener[] listeners = btn.getActionListeners();
			
			if(listeners != null && listeners.length > 0)
				btn.removeActionListener( btn.getActionListeners()[0] );
				
			m_panel.remove(btn);
		}
		
		int tx=0;
		int ty=0;		
		
		for(int i = 0; i < arrayList.size(); ++i){
			addIcon(tx*32, ty*32, arrayList.get(i));
			
			tx++;
			if(tx>=m_maxRowLength){tx=0; ty++;}
			if(ty>=m_maxColHeight) break;
		}
		m_panel.repaint();
	}
	
	public int getHeight(){
		return m_maxColHeight * ICON_SIZE + PANEL_BORDER_TOP + PANEL_BORDER_BOT;
	}

}
