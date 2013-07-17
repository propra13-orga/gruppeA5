package toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Editor.EditorMap;


public class Toolbar {

	private JFrame m_frame;
	private JPanel m_panel;

	private TilesPanel m_tilesPanel;
	private EventsPanel m_eventsPanel;
	private TeleportersPanel m_teleportersPanel;
	private MonstersPanel m_monstersPanel;
	private ItemsPanel m_itemsPanel;
	
	private LayerType m_currentActiveLayer;
	
	public enum LayerType{
		Tiles,
		Events,
		Teleporters,
		Items,
		Monsters
	}
	private static final int NUM_OF_LAYERS = 5;
	private JCheckBox m_layerBoxes[] = new JCheckBox[NUM_OF_LAYERS];
	
	public LayerType getCurrentActiveLayer(){
		return m_currentActiveLayer;
	}
	
	public TilesPanel getTilesPanel(){
		return m_tilesPanel;
	}
	
	public EventsPanel getEventsPanel(){
		return m_eventsPanel;
	}
	
	public TeleportersPanel getTeleportersPanel(){
		return m_teleportersPanel;
	}
	
	public ItemsPanel getItemsPanel(){
		return m_itemsPanel;
	}
	
	public MonstersPanel getMonstersPanel(){
		return m_monstersPanel;
	}

	public boolean isLayerVisible(LayerType type){
		return m_layerBoxes[type.ordinal()].isSelected();
	}
	
	private void LayerBoxes(){
		JCheckBox check = new JCheckBox("Tiles");
		m_layerBoxes[LayerType.Tiles.ordinal()] = check;
		check.setBounds(5, 30, 70, 20);
		check.setEnabled(true);
		check.setSelected(true);
		m_frame.add(check);
		
		JCheckBox check2 = new JCheckBox("Events");
		m_layerBoxes[LayerType.Events.ordinal()] = check2;
		check2.setBounds(75, 30, 70, 20);
		m_frame.add(check2);
		
		JCheckBox check3 = new JCheckBox("Teleporters");
		m_layerBoxes[LayerType.Teleporters.ordinal()] = check3;
		check3.setBounds(145, 30, 100, 20);
		m_frame.add(check3);
		
		JCheckBox check4 = new JCheckBox("Items");
		m_layerBoxes[LayerType.Items.ordinal()] = check4;
		check4.setBounds(5, 50, 70, 20);
		m_frame.add(check4);
		
		JCheckBox check5 = new JCheckBox("Monsters");
		m_layerBoxes[LayerType.Monsters.ordinal()] = check5;
		check5.setBounds(75, 50, 90, 20);
		m_frame.add(check5);
	}

	public Toolbar(){
		m_frame = new JFrame("Toolbar");
		m_frame.setSize(300,600);
		
		m_panel = new JPanel();
		m_panel.setSize(300,600);
		m_panel.setLayout(null);
		m_panel.setOpaque(true);
		
		m_frame.setContentPane(m_panel);
		m_frame.setLocation(810,000);
		
		LayerBoxes();
		
		m_tilesPanel = new TilesPanel(m_panel, 0, 80, 300, 500);
		m_eventsPanel = new EventsPanel(m_panel, 0, 80, 300, 500);
		m_teleportersPanel = new TeleportersPanel(m_panel, 0, 80, 300, 500);
		m_itemsPanel = new ItemsPanel(m_panel, 0, 80, 300, 500);
		m_monstersPanel = new MonstersPanel(m_panel, 0, 80, 300, 500);
		
		m_tilesPanel.setVisible(true);
		m_eventsPanel.setVisible(false);
		m_teleportersPanel.setVisible(false);
		m_itemsPanel.setVisible(false);
		m_monstersPanel.setVisible(false);

		JComboBox<String> cb = new JComboBox<String>();
		cb.addItem("Tiles");
		cb.addItem("Events");
		cb.addItem("Teleporters");
		cb.addItem("Items");
		cb.addItem("Monsters");
		cb.setBounds(5,5,150,20);
		
		cb.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent evt) {
		    	  
		    	  JComboBox<String> cb = (JComboBox<String>) evt.getSource();
		          String name = (String) cb.getSelectedItem();
		    	  
		    	  boolean tiles = name.equals("Tiles") ? true : false;
		    	  boolean events = name.equals("Events") ? true : false;
		    	  boolean teles = name.equals("Teleporters") ? true : false;
		    	  boolean itms = name.equals("Items") ? true : false;
		    	  boolean mons = name.equals("Monsters") ? true : false;
		    	  
		    	  m_tilesPanel.setVisible(tiles);
		    	  m_eventsPanel.setVisible(events);
		    	  m_teleportersPanel.setVisible(teles);
		    	  m_itemsPanel.setVisible(itms);
		    	  m_monstersPanel.setVisible(mons);
		    	  
		    	  m_currentActiveLayer = LayerType.valueOf(name);
		    	  m_layerBoxes[m_currentActiveLayer.ordinal()].setSelected(true);
		      }
		});
		
		m_frame.add(cb);
		
		
		m_currentActiveLayer = LayerType.Tiles;
		

        //Display the window.
		m_frame.setResizable(false);
		m_frame.setVisible(true);
	}

	public void supplyMap(EditorMap map) {
		m_eventsPanel.setMap(map);
		m_teleportersPanel.setMap(map);
		m_itemsPanel.setMap(map);
		m_monstersPanel.setMap(map);
	}

}
