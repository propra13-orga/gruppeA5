package toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import map.CellInfo;
import map.Coordinate;
import Editor.EditorMap;

public class TeleportersPanel {
	EditorMap m_map;
	JPanel m_groupPanel;
	
	JTextArea m_headerLabel;
	
	JTextField m_mapName;
	JSpinner m_xCoordinate;
	JSpinner m_yCoordinate;

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
	
	public void setTeleporterData(int x, int y, CellInfo cellInfo){
		m_selectedX = x;
		m_selectedY = y;
		m_headerLabel.setText("Currently selected:\n X = "+x+"\n Y = "+y+"\n Level: \n\nNew X: \n\nNew Y: ");
	
	
		if(cellInfo == null || !cellInfo.mHasTeleporter){
			m_mapName.setEnabled(false);
			m_mapName.setText("");
			m_xCoordinate.setEnabled(false);
			m_xCoordinate.setValue( new Integer(0) );
			m_yCoordinate.setEnabled(false);
			m_yCoordinate.setValue( new Integer(0) );
			
			m_createBtn.setEnabled(true);
			m_deleteBtn.setEnabled(false);
			m_saveBtn.setEnabled(false);
		}
		else{
			m_mapName.setEnabled(true);
			m_mapName.setText(cellInfo.mNewMap);
			m_xCoordinate.setEnabled(true);
			m_xCoordinate.setValue( Integer.valueOf( cellInfo.mNewPosition.mX ) );
			m_yCoordinate.setEnabled(true);
			m_yCoordinate.setValue( Integer.valueOf( cellInfo.mNewPosition.mY ) );
			
			m_createBtn.setEnabled(false);
			m_deleteBtn.setEnabled(true);
			m_saveBtn.setEnabled(true);
		}
		
	}
	
	public TeleportersPanel(JPanel motherPanel, int topX, int topY, int width, int height){
		m_groupPanel = new JPanel(null);
		m_groupPanel.setBounds(topX, topY, width, height);
		motherPanel.add(m_groupPanel);
	
		m_headerLabel = new JTextArea("Currently selected:\n None\n\n Level: \n\nNew X: \n\nNew Y: ");
		m_headerLabel.setBackground(null);
		m_headerLabel.setEditable(false);
		m_headerLabel.setBounds(10,0,110,160);
		m_groupPanel.add(m_headerLabel);
		
		m_mapName = new JTextField("insert map");
		m_mapName.setBounds(130, 50, 120, 20);
		m_mapName.setEnabled(false);
		m_groupPanel.add(m_mapName);
		
		SpinnerNumberModel xNumberModel = new SpinnerNumberModel(
            new Integer(0), // value
            new Integer(0), // min
            new Integer(15), // max
            new Integer(1) // step
		);
		
		SpinnerNumberModel yNumberModel = new SpinnerNumberModel(
	            new Integer(0), // value
	            new Integer(0), // min
	            new Integer(15), // max
	            new Integer(1) // step
			);
		
		m_xCoordinate = new JSpinner(xNumberModel);
		m_xCoordinate.setBounds(130,82,40,20);
		m_xCoordinate.setEnabled(false);
		m_groupPanel.add(m_xCoordinate);
		
		m_yCoordinate = new JSpinner(yNumberModel);
		m_yCoordinate.setBounds(130,114,40,20);
		m_yCoordinate.setEnabled(false);
		m_groupPanel.add(m_yCoordinate);
		
		
        m_createBtn = new JButton("Create Telep.");
        m_createBtn.setBounds(120, 170, 110, 20);
        m_createBtn.setEnabled(false);
        
        m_createBtn.addActionListener( 
        	new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
				
					CellInfo ci = m_map.getCellInfo(m_selectedX, m_selectedY); //There could be an Event on it
					
					if(ci != null){
						ci.mHasTeleporter = true;
						ci.mNewMap = m_mapName.getText();
						Coordinate pos = new Coordinate();
						pos.mX = ((Integer)m_xCoordinate.getValue()).intValue();
						pos.mY = ((Integer)m_yCoordinate.getValue()).intValue();
						ci.mNewPosition = pos;
					}else{
						ci = new CellInfo();
						
						ci.mHasTeleporter = true;
						ci.mNewMap = m_mapName.getText();
						Coordinate pos = new Coordinate();
						pos.mX = ((Integer)m_xCoordinate.getValue()).intValue();
						pos.mY = ((Integer)m_yCoordinate.getValue()).intValue();
						ci.mNewPosition = pos;
						
						m_map.getCurrentLevel().getAllCellInfo().put( new Coordinate(m_selectedX, m_selectedY), ci );
					}
					
					setTeleporterData(m_selectedX, m_selectedY, ci);
				}} 
		);
		
        m_groupPanel.add(m_createBtn);
        
        m_deleteBtn = new JButton("Delete Telep.");
        m_deleteBtn.setBounds(120, 200, 110, 20);
        m_deleteBtn.setEnabled(false);
        
        m_deleteBtn.addActionListener( 
        	new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
				
					CellInfo ci = m_map.getCellInfo(m_selectedX, m_selectedY);
					
					if(ci.mHasEvent == false){
						m_map.getCurrentLevel().getAllCellInfo().remove(new Coordinate(m_selectedX,m_selectedY));
					}else{
						ci.mHasTeleporter = false;
						ci.mNewMap = "";
						ci.mNewPosition = new Coordinate(0,0);
					}

					setTeleporterData(m_selectedX, m_selectedY, null);
				}} 
    	);
        
        m_groupPanel.add(m_deleteBtn);
        
        m_saveBtn = new JButton("Save Telep.");
        m_saveBtn.setBounds(120, 230, 110, 20);
        m_saveBtn.setEnabled(false);
        
        m_saveBtn.addActionListener( 
        	new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
				
					CellInfo ci = m_map.getCellInfo(m_selectedX, m_selectedY);
					
					ci.mNewMap = m_mapName.getText();
					Coordinate pos = new Coordinate();
					pos.mX = ((Integer)m_xCoordinate.getValue()).intValue();
					pos.mY = ((Integer)m_yCoordinate.getValue()).intValue();
					ci.mNewPosition = pos;
					
					setTeleporterData(m_selectedX, m_selectedY, ci);
				}} 
    	);
        
        m_groupPanel.add(m_saveBtn);
	}
}