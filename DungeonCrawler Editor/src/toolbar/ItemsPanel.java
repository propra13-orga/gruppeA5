package toolbar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import Editor.EditorMap;

import map.Coordinate;
import map.Item;


public class ItemsPanel {
	EditorMap m_map;
	JPanel m_groupPanel;
	
	JTextArea m_headerLabel;
	JTextField m_nameField;
	
	JButton m_createBtn;
	JButton m_deleteBtn;
	JButton m_saveBtn;
	
	int m_selectedX;
	int m_selectedY;
	
	void setMap(EditorMap map){
		m_map = map;
	}
	
	public void setVisible(boolean visible){
		m_groupPanel.setVisible(visible);
	}
	
	public void setItemData(int x, int y, Item item){
		m_selectedX = x;
		m_selectedY = y;
		m_headerLabel.setText("Item: X = "+x+"\tY = "+y+": ");
	
		if(item == null){
			m_nameField.setEnabled(false);
			
			m_createBtn.setEnabled(true);
			m_deleteBtn.setEnabled(false);
			m_saveBtn.setEnabled(false);
		}
		else{
			m_nameField.setText( item.mType );
		
			m_nameField.setEnabled(true);
			
			m_createBtn.setEnabled(false);
			m_deleteBtn.setEnabled(true);
			m_saveBtn.setEnabled(true);
		}
		
	}
	
	public ItemsPanel(JPanel motherPanel, int topX, int topY, int width, int height){
		m_groupPanel = new JPanel(null);
		m_groupPanel.setBounds(topX, topY, width, height);
		motherPanel.add(m_groupPanel);
	
		m_headerLabel = new JTextArea("Currently selected: None");
		m_headerLabel.setBackground(null);
		m_headerLabel.setEditable(false);
		m_headerLabel.setBounds(10,0,200,30);
		m_groupPanel.add(m_headerLabel);
		
        
        m_nameField = new JTextField();
        m_nameField.setBounds(15,30,150,18);
        m_nameField.setEditable(true);
        m_nameField.setEnabled(false);
        m_groupPanel.add(m_nameField);
        
        m_createBtn = new JButton("Create Item");
        m_createBtn.setBounds(120, 90, 110, 20);
        m_createBtn.setEnabled(false);
        
        m_createBtn.addActionListener( 
        	new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
				
					Item ci = m_map.getItemInfo(m_selectedX, m_selectedY);
					
					if(ci != null){
						m_nameField.setText( ci.mType );
					}else{
						ci = new Item();
						m_map.getCurrentLevel().getAllItemInfo().put( new Coordinate(m_selectedX, m_selectedY), ci );
					}
					
					setItemData(m_selectedX, m_selectedY, ci);
				}} 
		);
		
        m_groupPanel.add(m_createBtn);
        
        m_deleteBtn = new JButton("Delete Item");
        m_deleteBtn.setBounds(120, 120, 110, 20);
        m_deleteBtn.setEnabled(false);
        
        m_deleteBtn.addActionListener( 
        	new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
						m_map.getCurrentLevel().getAllItemInfo().remove(new Coordinate(m_selectedX,m_selectedY));

					setItemData(m_selectedX, m_selectedY, null);
				}} 
    	);
        
        m_groupPanel.add(m_deleteBtn);
        
        m_saveBtn = new JButton("Save Item");
        m_saveBtn.setBounds(120, 150, 110, 20);
        m_saveBtn.setEnabled(false);
        
        m_saveBtn.addActionListener( 
        	new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
				
					Item ci = m_map.getItemInfo(m_selectedX, m_selectedY);
					ci.mType = m_nameField.getText();
					
					setItemData(m_selectedX, m_selectedY, ci);
				}} 
    	);
        
        m_groupPanel.add(m_saveBtn);
	}
}
