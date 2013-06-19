package map;
import game.item.PickupPool;

import java.util.HashMap;


import map.TileList;
import monster.MonsterPool;

import std.StdDraw;



public class Map {
	//Some constants
	static final int MAP_HEIGHT = 16;
	static final int MAP_LENGTH = 16;
	static final double SIZE = 32.00d;
	
	//The x/y position of the top-left corner of the map.
	double m_x;
	double m_y;
		
	//Holds all the levels already loaded
	private HashMap<String, Level> m_levels = new HashMap<String, Level>();
	
	private static Map m_this = null;

	//Currently selected level
	private Level m_currentLevel;
	private String m_currentLevelName;
	
	private TileList m_tileList;
	
	public static Map getInstance(){
		return m_this;
	}
	
	/**
	 * Loads (and displays) a level from a text file with the name levelName. Returns true if successful.
	 */
	public boolean loadLevel(String levelName){
		levelName = levelName.toLowerCase(); //We do not care about case-sensitivity
	
		if(m_levels.containsKey(levelName)){
			m_currentLevel = m_levels.get(levelName);
		}else{
			Level lvl = new Level();
			if( !lvl.loadFromFile(levelName) || !lvl.verify(m_tileList) ){
				System.out.println("Error, could not load level.");
				return false;
			}
			m_levels.put(levelName, lvl);
			m_currentLevel = lvl;
			m_currentLevelName = levelName;
		}
		
		return true;
	}
	
	public String getCurrentLevelname(){
		return m_currentLevelName;
	}
	
	public MonsterPool getMonsterPool(){
		return m_currentLevel.getMonsterPool();
	}
	
	public PickupPool getPickupPool(){
		return m_currentLevel.getPickupPool();
	}
	
	/**
	 * Returns true if (x,y) is walkable. False if not walkable.
	 */
	public boolean isPathable(int x, int y){
		if( isInsideMap(x,y) )
			return m_tileList.get(m_currentLevel.getCellType(x,y)).mPathable;
		else
			return false;
	}
	
	/**
	 * Returns the CellInfo object attached to the cell at (x,y).
	 * Returns null if no CellInfo object is attached to this cell.
	 */
	public CellInfo getCellInfo(int x, int y){
		return m_currentLevel.getCellInfo(x,y);
	}
	
	 /**
	  * Sets the char value of the cell at (x,y) to c
	  */
	public void setCell(int x, int y, char c){
		m_currentLevel.setCell(x,y,c);
	}
	
	/**
	 * Returns true if the map coordinate (x,y) is inside the map.
	 */
	public boolean isInsideMap(int x, int y){
		return x>=0 && y>=0 && x<MAP_LENGTH && y<MAP_HEIGHT;
	}
	
	/**
	 * Converts the canvas coordinate x to its map coordinate.
	 */
	public int getGridX(double x){
		return (int) ((x-m_x) / SIZE);
	}
	
	/**
	 * Converts the canvas coordinate x to its map coordinate.
	 */
	public int getGridY(double y){
		return (int) ((y-m_y) / SIZE);
	}
	
	/**
	 * Converts the map coordinate x to canvas coordinate.
	 */
	public double getCanvasX(int x){
		return m_x + x*SIZE;
	}
	
	/**
	 * Converts the map coordinate y to canvas coordinate.
	 */
	public double getCanvasY(int y){
		return m_y + y*SIZE;
	}
	
	/**
	 * Displays the currently selected level on screen.
	 */
	public void render(){
	
		for(int y = 0; y < MAP_HEIGHT; y++){
			for(int x = 0; x < MAP_LENGTH; x++){
				StdDraw.picture(m_x + x*SIZE, m_y + y*SIZE, m_tileList.get(m_currentLevel.getCellType(x,y)).mFilePath, 32, 32);
			}
		}
		
		m_currentLevel.getMonsterPool().renderAll();
		m_currentLevel.getPickupPool().renderAll(this);
	}
	
	/**
	 * Constructs a map at position (x,y) and loads the corresponding TileList from tileListPath
	 */
	public Map(double x, double y, String tileListPath){
		m_this = this;
	
		m_tileList = new TileList();
		
		//TODO: Check if loading successful.
		m_tileList.loadFromFile(tileListPath);
		
		m_x = x;
		m_y = y;
	}
	
}
