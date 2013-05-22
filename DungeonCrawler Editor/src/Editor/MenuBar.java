package Editor;
import java.awt.FileDialog;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import map.EditorTileList;
import map.LevelSaver;
import std.StdWin;

public class MenuBar {

	private static class NewLevelAL implements ActionListener{
		private EditorMap m_map;
	
		public void actionPerformed(ActionEvent e) {
	        FileDialog chooser = new FileDialog(StdWin.getFrame(), "Select a name for the new file.", FileDialog.SAVE);
	        chooser.setVisible(true);
	        String filename = chooser.getFile();
	        if (filename != null) {
	        	if(!filename.endsWith(".txt")) filename += ".txt";
	        
	        	String path = chooser.getDirectory() /*+ File.separator*/ + filename;
	            LevelSaver ls = new LevelSaver();
	            ls.createBlankLevel(path);
	            m_map.loadLevel(path);
	        }
	    }
	    
	    public NewLevelAL(EditorMap m){
	    	m_map = m;
	    }
    }
    
	private static class LoadLevelAL implements ActionListener{
		private EditorMap m_map;
	
		public void actionPerformed(ActionEvent e) {
	        FileDialog chooser = new FileDialog(StdWin.getFrame(), "Select a level.", FileDialog.LOAD);
	        chooser.setVisible(true);
	        String filename = chooser.getFile();
	        if (filename != null) {
	            m_map.loadLevel(chooser.getDirectory() /*+ File.separator*/ + chooser.getFile());
	            
	        }
	    }
	    
	    public LoadLevelAL(EditorMap m){
	    	m_map = m;
	    }
    }
    
	private static class SaveAL implements ActionListener{
		private EditorMap m_map;
	
		public void actionPerformed(ActionEvent e) {
		
			if(m_map.getCurrentLevel() != null)
				m_map.getCurrentLevel().saveToFile( m_map.getCurrentLevelName() );
		
            //ActionListener al = new SaveAsAL(m_map);
            //al.actionPerformed(e);

	    }
	    
	    public SaveAL(EditorMap m){
	    	m_map = m;
	    }
    }
    
	private static class SaveAsAL implements ActionListener{
		private EditorMap m_map;
	
		public void actionPerformed(ActionEvent e) {
			if(m_map.getCurrentLevel() == null)
				return;
		
	        FileDialog chooser = new FileDialog(StdWin.getFrame(), "Use a .png or .jpg extension", FileDialog.SAVE);
	        chooser.setVisible(true);
	        String filename = chooser.getFile();
	        if (filename != null) {
	        	if(!filename.endsWith(".txt")) filename += ".txt";
	        	m_map.getCurrentLevel().saveToFile(chooser.getDirectory() /*+ File.separator*/ + filename);
	        }
	    }
	    
		public SaveAsAL(EditorMap m){
			m_map = m;
		}
    }
    
    private static class GenerateTilesTXTAL implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if( EditorTileList.getInstance().saveToTilesTXT("data/tiles.txt") )
				JOptionPane.showMessageDialog(null, "New tiles.txt successfully generated.");
			else
				JOptionPane.showMessageDialog(null, "New tiles.txt could not be generated!");
		}
    }

	private static void addItem(JMenu menu, String title, int accelerator, ActionListener al) {
        JMenuItem menuItem1 = new JMenuItem(title);
        menuItem1.addActionListener(al);
        menuItem1.setAccelerator(KeyStroke.getKeyStroke(accelerator,
                                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menu.add(menuItem1);
        
    }

	public MenuBar(EditorMap map){
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
	
		addItem( fileMenu, "New Level", KeyEvent.VK_N, new NewLevelAL(map) );
		addItem( fileMenu, "Load Existing Level", KeyEvent.VK_L, new LoadLevelAL(map) );
		addItem( fileMenu, "Save", KeyEvent.VK_S, new SaveAL(map) );
		addItem( fileMenu, "Save As..", KeyEvent.VK_T, new SaveAsAL(map) );
		
		
		JMenu tileMenu = new JMenu("Tiles");
		menuBar.add(tileMenu);
		
		addItem( tileMenu, "Generate tiles.txt", 0, new GenerateTilesTXTAL() );
		
		
		StdWin.getFrame().setJMenuBar(menuBar);
        StdWin.getFrame().pack();
	}
}
