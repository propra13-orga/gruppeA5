package map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class' sole purpose is to serve as a factory for Level objects.
 * The loading from a text file is a non-trivial task, so it has been outsourced
 * to this file.
 * The LevelLoader is used for internal management, and users should not be required to use it by themselves.
 */
class LevelLoader {
	//Expected map height and length.
	static final int MAP_HEIGHT = 16;
	static final int MAP_LENGTH = 16;
	
	private BufferedReader m_reader = null;
	private String m_line = null;
	
	private char[][] m_grid;
	
	private HashMap<Coordinate, CellInfo> m_tileInfo;
	
	/**
	 * Returns the char[][] that holds the grid data.
	 */
	public char[][] getGrid(){
		return m_grid;
	}
	
	/**
	 * Returns the HashMap that contains the CellInfo classes for all cells.
	 */
	public HashMap<Coordinate, CellInfo> getCellData(){
		return m_tileInfo;
	}
	
	private void nextLine(){
		try {
			do{
				m_line = m_reader.readLine();
			} while(m_line != null && (m_line.equals("") || m_line.startsWith("//")) );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean loadMapData(){
		int counter = 0;
	
		while(true){
			nextLine();
			
			if(m_line == null ||m_line.startsWith("#"))
				break;
		
			if(counter>=MAP_HEIGHT){
				System.out.println("Error, too many lines in grid.");
				return false;
			}
		
			if(m_line.length() != MAP_LENGTH){
				System.out.println("Error, bad line length: " + m_line);
				return false;
			}
			
	        for(int x=0; x<MAP_LENGTH; x++)
	        	m_grid[x][counter] = (char) (m_line.charAt(x) - '0');
	        	
			counter++;
		}
		
		if(counter != MAP_HEIGHT){
			System.out.println("Error, grid not entirely filled");
			return false;
		}
		
		return true;
	}

	private boolean loadEvents(){
		while(true){
			nextLine();
			
			if(m_line == null ||m_line.startsWith("#"))
				break;
			
			String[] ar = m_line.split(" ");
			
			if(ar.length != 3){
				System.out.println("Error, bad event line: " + m_line);
				return false;
			}
			
			try{
				Coordinate c = new Coordinate();
				c.mX = Integer.parseInt(ar[0]);
				c.mY = Integer.parseInt(ar[1]);
				
				CellInfo ti;
			
				if(m_tileInfo.containsKey(c)){
					ti = m_tileInfo.get(c);
					ti.mHasEvent = true;
					ti.mEventID = Integer.parseInt(ar[2]);
				}else{
					ti = new CellInfo();
					ti.mHasEvent = true;
					ti.mEventID = Integer.parseInt(ar[2]);
					m_tileInfo.put(c, ti);
				}
				
			} catch(NumberFormatException e) {
				System.out.println(e);
				return false;
			}
		}

		return true;
	}
	
	private boolean loadTeleporters(){
		Pattern regex = Pattern.compile("([^\"]\\S*|\".+?\")\\s*");
		
		while(true){
			nextLine();
			
			if(m_line == null || m_line.startsWith("#"))
				break;
			
			ArrayList<String> list = new ArrayList<String>();
			Matcher m = regex.matcher(m_line);
			
			while (m.find())
			    list.add(m.group(1));
			
			if(list.size() != 5){
				System.out.println("Error, bad teleporter line: " + m_line);
				return false;
			}
			
			try{
				Coordinate c = new Coordinate();
				c.mX = Integer.parseInt(list.get(0));
				c.mY = Integer.parseInt(list.get(1));
				
				CellInfo ti;
			
				if(m_tileInfo.containsKey(c)){
					ti = m_tileInfo.get(c);
					ti.mHasTeleporter = true;
					ti.mNewMap = list.get(2).replace("\"", "");
					ti.mNewPosition = new Coordinate();
					ti.mNewPosition.mX = Integer.parseInt(list.get(3));
					ti.mNewPosition.mY = Integer.parseInt(list.get(4));
				}else{
					ti = new CellInfo();
					ti.mHasTeleporter = true;
					ti.mNewMap = list.get(2).replace("\"", "");
					ti.mNewPosition = new Coordinate();
					ti.mNewPosition.mX = Integer.parseInt(list.get(3));
					ti.mNewPosition.mY = Integer.parseInt(list.get(4));
					m_tileInfo.put(c, ti);
				}
			} catch(NumberFormatException e) {
				System.out.println(e);
				return false;
			}
			
		}
		
		return true;
	}

	/**
	 * Loads a level from a text file with the name fileName. Returns true if successful.
	 */
	public boolean load(String fileName){
		m_grid = new char[MAP_LENGTH][MAP_HEIGHT];
		m_tileInfo = new HashMap<Coordinate, CellInfo>();
		boolean status = true;
		
		try{
			m_reader = new BufferedReader(new FileReader(fileName));
	        nextLine();
	        
	        while(m_line != null){
	        
	        	if( m_line.equals("# Map") ){
		        	status = status && loadMapData();
		        } else if( m_line.equals("# Events") ){
		        	status = status && loadEvents();
		        } else if( m_line.equals("# Teleporters") ){
		        	status = status && loadTeleporters();
		        } else {
		        	System.out.println("Error, unrecognized line: " + m_line);
		        	break;
		        }
		        
	        }

		} catch(Exception e){
			System.out.println("Error: " + e);
			status = false;
		}
	
		return status;
	}
}
