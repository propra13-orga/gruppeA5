package toolbar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;

import Editor.EditorMap;

import map.CellInfo;
import map.Coordinate;


public class EventsPanel {
	EditorMap m_map;
	JPanel m_groupPanel;
	
	JTextArea m_headerLabel;
	JSpinner m_eventIdField;
	
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
	
	public void setEventData(int x, int y, CellInfo cellInfo){
		m_selectedX = x;
		m_selectedY = y;
		m_headerLabel.setText("Currently selected:\n X = "+x+"\n Y = "+y+"\n Event ID: ");
	
	
		if(cellInfo == null || !cellInfo.mHasEvent){
			m_eventIdField.setEnabled(false);
			m_eventIdField.setValue( new Integer(0) );
			
			m_createBtn.setEnabled(true);
			m_deleteBtn.setEnabled(false);
			m_saveBtn.setEnabled(false);
		}
		else{
			m_eventIdField.setEnabled(true);
			m_eventIdField.setValue( Integer.valueOf( cellInfo.mEventID ) );
			
			m_createBtn.setEnabled(false);
			m_deleteBtn.setEnabled(true);
			m_saveBtn.setEnabled(true);
		}
		
	}
	
	public EventsPanel(JPanel motherPanel, int topX, int topY, int width, int height){
		m_groupPanel = new JPanel(null);
		m_groupPanel.setBounds(topX, topY, width, height);
		motherPanel.add(m_groupPanel);
	
		m_headerLabel = new JTextArea("Currently selected:\n None");
		m_headerLabel.setBackground(null);
		m_headerLabel.setEditable(false);
		m_headerLabel.setBounds(10,0,200,70);
		m_groupPanel.add(m_headerLabel);
		
		
		SpinnerNumberModel numberModel = new SpinnerNumberModel(
            new Integer(0), // value
            new Integer(0), // min
            new Integer(255), // max
            new Integer(1) // step
		);
		m_eventIdField = new JSpinner(numberModel);
		m_eventIdField.setBounds(50,70,40,20);
		m_eventIdField.setEnabled(false);
		
        m_groupPanel.add(m_eventIdField);
        
        m_createBtn = new JButton("Create Event");
        m_createBtn.setBounds(120, 70, 110, 20);
        m_createBtn.setEnabled(false);
        
        m_createBtn.addActionListener( 
        	new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
				
					CellInfo ci = m_map.getCellInfo(m_selectedX, m_selectedY); //There could be a Teleporter on it
					
					if(ci != null){
						ci.mHasEvent = true;
						ci.mEventID = ((Integer)m_eventIdField.getValue()).intValue();
					}else{
						ci = new CellInfo();
						ci.mHasEvent = true;
						ci.mEventID = ((Integer)m_eventIdField.getValue()).intValue();
						m_map.getCurrentLevel().getAllCellInfo().put( new Coordinate(m_selectedX, m_selectedY), ci );
					}
					
					setEventData(m_selectedX, m_selectedY, ci);
				}} 
		);
		
        m_groupPanel.add(m_createBtn);
        
        m_deleteBtn = new JButton("Delete Event");
        m_deleteBtn.setBounds(120, 100, 110, 20);
        m_deleteBtn.setEnabled(false);
        
        m_deleteBtn.addActionListener( 
        	new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
				
					CellInfo ci = m_map.getCellInfo(m_selectedX, m_selectedY);
					
					if(ci.mHasTeleporter == false){
						m_map.getCurrentLevel().getAllCellInfo().remove(new Coordinate(m_selectedX,m_selectedY));
					}else{
						ci.mEventID = 0;
						ci.mHasEvent = false;
					}

					setEventData(m_selectedX, m_selectedY, null);
				}} 
    	);
        
        m_groupPanel.add(m_deleteBtn);
        
        m_saveBtn = new JButton("Save Event");
        m_saveBtn.setBounds(120, 130, 110, 20);
        m_saveBtn.setEnabled(false);
        
        m_saveBtn.addActionListener( 
        	new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
				
					CellInfo ci = m_map.getCellInfo(m_selectedX, m_selectedY);
					ci.mEventID = ((Integer)m_eventIdField.getValue()).intValue();
					
					setEventData(m_selectedX, m_selectedY, ci);
				}} 
    	);
        
        m_groupPanel.add(m_saveBtn);
	}
}
