package toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import map.EditorTileList;
import map.EditorTileList.EditorTileType;

public class TilesPanel {
	JPanel m_groupPanel;
	Palette m_selectedPalette;
	JComboBox<ComboBoxEntry> m_setSelection;
	
	ArrayList<Palette> m_palettes = new ArrayList<Palette>();
	
	public char getSelectedTileTypeId(){
		return m_selectedPalette.getCurrentlySelectedTileId();
	}
	
	public void setVisible(boolean visible){
		m_groupPanel.setVisible(visible);
	}
	
	public void receiveSelectionEvent(Palette sender){
		m_selectedPalette = sender;
		for( Palette p : m_palettes ){
			if(p != sender)
				p.deselect();
		}
	}
	
	private class ComboBoxEntry{
		private String m_name;
		private HashMap<String, ArrayList<EditorTileType>> m_set;
		
		public ComboBoxEntry( Entry<String, HashMap<String, ArrayList<EditorTileType>>> entry ) {
			m_name = entry.getKey();
			m_set = entry.getValue();
		}
		
		public HashMap<String, ArrayList<EditorTileType>> getSet(){
			return m_set;
		}
		
		public String toString(){
			return m_name;
		}
	}
	
	public void displaySet( HashMap<String, ArrayList<EditorTileType>> set ){
		Set<Entry<String, ArrayList<EditorTileType>>> entrySet = set.entrySet();
		
		int additionalPalettes = entrySet.size() - m_palettes.size();
		
		while(additionalPalettes > 0){
			m_palettes.add( new Palette(m_groupPanel, this) );
			additionalPalettes -= 1;
		}
	
		int y = 25;
		int i = 0;
		for( Entry<String, ArrayList<EditorTileType>> subset : entrySet ){
			Palette p = m_palettes.get(i);
			
			p.setPalette(subset.getKey(), 10, y, 7, subset.getValue() );
			y += p.getHeight();
			i += 1;
		}
	}
	
	public TilesPanel(JPanel motherPanel, int topX, int topY, int width, int height){
		m_groupPanel = new JPanel(null);
		m_groupPanel.setBounds(topX, topY, width, height);
		motherPanel.add(m_groupPanel);
	
		EditorTileList etl = new EditorTileList();
		etl.loadFromFile("data/editortiles.txt");
		
		m_setSelection = new JComboBox<ComboBoxEntry>();
		m_setSelection.setBounds(10, 0, 150, 20);
		
		for( Entry<String, HashMap<String, ArrayList<EditorTileType>>> set : etl.getAllSets().entrySet() ){
		
			m_setSelection.addItem( new ComboBoxEntry(set) );
			
		}
		
		m_setSelection.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent evt) {
		    	  JComboBox<ComboBoxEntry> cb = (JComboBox<ComboBoxEntry>) evt.getSource();
		    	  ComboBoxEntry entry = (ComboBoxEntry) cb.getSelectedItem();
		    	  displaySet( entry.getSet() );
		      }
		});
		
		m_groupPanel.add(m_setSelection);
		
		ComboBoxEntry entry = (ComboBoxEntry) m_setSelection.getSelectedItem();
  	  	displaySet( entry.getSet() );

	}
}
