package map;
import java.util.HashMap;

public class Level {

	private char [][] m_grid;
	private HashMap<Coordinate, CellInfo> m_cellInfo;
	
	public HashMap<Coordinate, CellInfo> getAllCellInfo(){
		return m_cellInfo;
	}
	
	/**
	 * Returns the char value of the cell at (x,y)
	 */
	public char getCellType(int x, int y){
		return m_grid[x][y];
	}
	
	/**
	 * Sets the char value of the cell at (x,y) to c
	 */
	public void setCell(int x, int y, char c){
		m_grid[x][y] = c;
	}
	
	/**
	 * Returns the TileInfo of the cell at (x,y)
	 */
	public CellInfo getCellInfo(int x, int y){
		Coordinate c = new Coordinate(x,y);
		
		return m_cellInfo.get(c);
	}
	
	/**
	 * Loads a level from a text file with the name levelName.
	 * Returns true if loading was successful. Returns false if an error occured.
	 */
	public boolean loadFromFile(String levelName){
		LevelLoader lvl = new LevelLoader();
		
		if( !lvl.load(levelName) )
			return false;
			
		m_grid = lvl.getGrid();
		m_cellInfo = lvl.getCellData();
	
		return true;
	}
	
	public void saveToFile(String levelName){
		map.LevelSaver ls = new map.LevelSaver();
		ls.saveToFile(levelName, m_grid, m_cellInfo);
	}

	/**
	 * Checks if the level can be completely displayed using the TileList tileList.
	 * Returns true if so, returns false if not.
	 * If verify fails, the level will not be rendered correctly.
	 */
	public boolean verify(TileList tileList) {
		char max = m_grid[0][0];
		
		for(char[] row : m_grid){
			for(char c : row){
				if(c>max)
					max = c;
			}
		}
		
		if(tileList.size() > max)
			return true;
		else{
			System.out.println("Error: bad tile type in grid.");
			return false;
		}
	}
}
