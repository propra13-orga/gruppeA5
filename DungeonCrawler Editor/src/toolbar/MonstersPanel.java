package toolbar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import Editor.EditorMap;

import map.Coordinate;
import map.Monster;


public class MonstersPanel {
	EditorMap m_map;
	JPanel m_groupPanel;
	
	JTextArea m_headerLabel;
	JTextField m_iconField;
	JTextField m_monstersField;
	
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
	
	public void setMonsterData(int x, int y, Monster monster){
		m_selectedX = x;
		m_selectedY = y;
		m_headerLabel.setText("Monster: X = "+x+"\tY = "+y+": ");
	
		if(monster == null){
			m_monstersField.setEnabled(false);
			m_iconField.setEnabled(false);
			
			m_createBtn.setEnabled(true);
			m_deleteBtn.setEnabled(false);
			m_saveBtn.setEnabled(false);
		}
		else{
			m_iconField.setText( monster.mIcon );
			m_monstersField.setText( monster.mMonsters );
		
			m_monstersField.setEnabled(true);
			m_iconField.setEnabled(true);
			
			m_createBtn.setEnabled(false);
			m_deleteBtn.setEnabled(true);
			m_saveBtn.setEnabled(true);
		}
		
	}
	
	public MonstersPanel(JPanel motherPanel, int topX, int topY, int width, int height){
		m_groupPanel = new JPanel(null);
		m_groupPanel.setBounds(topX, topY, width, height);
		motherPanel.add(m_groupPanel);
	
		m_headerLabel = new JTextArea("Currently selected: None");
		m_headerLabel.setBackground(null);
		m_headerLabel.setEditable(false);
		m_headerLabel.setBounds(10,0,200,30);
		m_groupPanel.add(m_headerLabel);
		
		
		m_monstersField = new JTextField();
		m_monstersField.setBounds(15,50,250,18);
		m_monstersField.setEditable(true);
		m_monstersField.setEnabled(false);
        m_groupPanel.add(m_monstersField);
        
        m_iconField = new JTextField();
        m_iconField.setBounds(15,30,250,18);
        m_iconField.setEditable(true);
        m_iconField.setEnabled(false);
        m_groupPanel.add(m_iconField);
        
        m_createBtn = new JButton("Create Mnstr");
        m_createBtn.setBounds(120, 90, 110, 20);
        m_createBtn.setEnabled(false);
        
        m_createBtn.addActionListener( 
        	new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
				
					Monster ci = m_map.getMonsterInfo(m_selectedX, m_selectedY);
					
					if(ci != null){
						m_iconField.setText( ci.mIcon );
						m_monstersField.setText( ci.mMonsters );
					}else{
						ci = new Monster();
						m_map.getCurrentLevel().getAllMonsterInfo().put( new Coordinate(m_selectedX, m_selectedY), ci );
					}
					
					setMonsterData(m_selectedX, m_selectedY, ci);
				}} 
		);
		
        m_groupPanel.add(m_createBtn);
        
        m_deleteBtn = new JButton("Delete Mnstr");
        m_deleteBtn.setBounds(120, 120, 110, 20);
        m_deleteBtn.setEnabled(false);
        
        m_deleteBtn.addActionListener( 
        	new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
						m_map.getCurrentLevel().getAllMonsterInfo().remove(new Coordinate(m_selectedX,m_selectedY));

					setMonsterData(m_selectedX, m_selectedY, null);
				}} 
    	);
        
        m_groupPanel.add(m_deleteBtn);
        
        m_saveBtn = new JButton("Save Mnstr");
        m_saveBtn.setBounds(120, 150, 110, 20);
        m_saveBtn.setEnabled(false);
        
        m_saveBtn.addActionListener( 
        	new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
				
					Monster ci = m_map.getMonsterInfo(m_selectedX, m_selectedY);
					ci.mMonsters = m_monstersField.getText();
					ci.mIcon = m_iconField.getText();
					
					setMonsterData(m_selectedX, m_selectedY, ci);
				}} 
    	);
        
        m_groupPanel.add(m_saveBtn);
	}
}
