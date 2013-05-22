package Editor;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import std.StdDraw;
import std.StdIO;
import std.StdIO.MouseEventType;
import std.StdWin;
import toolbar.Toolbar;
import toolbar.Toolbar.LayerType;
import map.*;


public class EditorMap extends Map {

	private String m_currentLevelName = "";
	private Toolbar m_toolbar;
	
	private boolean m_mouseDown = false;
	private int m_currentX = 0;
	private int m_currentY = 0;

	public Level getCurrentLevel(){
		return m_currentLevel;
	}
	
	public String getCurrentLevelName(){
		return m_currentLevelName;
	}
	
	public boolean loadLevel(String levelName){
		boolean b =  super.loadLevel(levelName);
		
		if(b){
			m_currentLevelName = levelName;
			StdWin.setTitle("Level: " + levelName);
		}	
		return b;
	}

	public void processMouseDown(MouseEvent e){
		if(m_currentLevel == null)
			return;

		if(e.getButton() != MouseEvent.BUTTON1)
			return;
			
		m_mouseDown = true;
		
		int x = getGridX( StdIO.mouseX() );
		int y = getGridY( StdIO.mouseY() );
		
		if( isInsideMap(x,y) ){
			LayerType t = m_toolbar.getCurrentActiveLayer();
			if( t == LayerType.Tiles )
				setCell(x,y, m_toolbar.getTilesPanel().getSelectedTileTypeId() );
			else if( t == LayerType.Events ){
				m_toolbar.getEventsPanel().setEventData( x, y, getCellInfo(x,y) );
			}else if( t == LayerType.Teleporters ){
				m_toolbar.getTeleportersPanel().setTeleporterData(x, y, getCellInfo(x,y) );
			}
		}
	}
	
	public void processMouseUp(MouseEvent e){
		if(m_currentLevel == null)
			return;

		if(e.getButton() != MouseEvent.BUTTON1)
			return;
			
		m_mouseDown = false;
	}

	public void update(){
		if(m_currentLevel == null)
			return;
	
		int x = getGridX( StdIO.mouseX() );
		int y = getGridY( StdIO.mouseY() );
		m_currentX = x;
		m_currentY = y;
		
		boolean insideMap = isInsideMap(x,y);
		
		if(m_mouseDown){
			if(insideMap){
				LayerType t = m_toolbar.getCurrentActiveLayer();
				if( t == LayerType.Tiles )
					setCell(x,y, m_toolbar.getTilesPanel().getSelectedTileTypeId() );
			}
		}
		
	}

	public void render(){
	
		if(m_currentLevel == null)
			return;
	
		if(	m_toolbar.isLayerVisible(Toolbar.LayerType.Tiles) ){
			for(int y = 0; y < MAP_HEIGHT; y++){
				for(int x = 0; x < MAP_LENGTH; x++){
					StdDraw.picture(m_x + x*SIZE, m_y + y*SIZE, m_tileList.get(m_currentLevel.getCellType(x,y)).mFilePath, 32, 32);
				}
			}
			
			int x =  getGridX( StdIO.mouseX() );
			int y =  getGridY( StdIO.mouseY() );
			
			if(isInsideMap(x,y)){
				StdDraw.setPenColor(StdDraw.RED);
				StdDraw.square(getCanvasX(x), getCanvasY(y), SIZE);
			}
			
		}
		HashMap<Coordinate, CellInfo> m = m_currentLevel.getAllCellInfo();
		
		final double CIRCLE_SIZE = 8;
		final double EVT_X = 0;
		final double EVT_Y = SIZE - CIRCLE_SIZE;
		final double TP_X = SIZE - CIRCLE_SIZE;
		final double TP_Y = SIZE - CIRCLE_SIZE;
		
		if(	m_toolbar.isLayerVisible(Toolbar.LayerType.Teleporters) )
			for (java.util.Map.Entry<Coordinate, CellInfo> entry : m.entrySet()) {
			    Coordinate c = entry.getKey();
			    CellInfo ce = entry.getValue();
			    
		        if(ce.mHasTeleporter){
		        	StdDraw.setPenColor(StdDraw.BLUE);
		        	StdDraw.filledCircle(m_x + c.mX*SIZE + TP_X, m_y + c.mY*SIZE + TP_Y, CIRCLE_SIZE);
		        }
			    
			}
		
		if( m_toolbar.isLayerVisible(Toolbar.LayerType.Events) )
			for (java.util.Map.Entry<Coordinate, CellInfo> entry : m.entrySet()) {
			    Coordinate c = entry.getKey();
			    CellInfo ce = entry.getValue();
			    
			    if(ce.mHasEvent){
		        	StdDraw.setPenColor(StdDraw.YELLOW);
		        	StdDraw.filledCircle(m_x + c.mX*SIZE + EVT_X, m_y + c.mY*SIZE + EVT_Y, CIRCLE_SIZE);
		        }
			    
			}
		
		if( isInsideMap(m_currentX, m_currentY) ){
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.textLeft(10, 580, "X: " + m_currentX + "  Y: " + m_currentY);
		}
	}

	public EditorMap(double x, double y, String tileListPath, Toolbar toolbar) {
		super(x, y, tileListPath);
		m_toolbar = toolbar;
		
		StdIO.addMouseListener( new StdIO.MouseListener() {
			public void receiveEvent(MouseEvent e) {
				processMouseDown(e);
			}
		}, MouseEventType.MousePressed );
		
		StdIO.addMouseListener( new StdIO.MouseListener() {
			public void receiveEvent(MouseEvent e) {
				processMouseUp(e);
			}
		}, MouseEventType.MouseReleased );
		
		
	}

}
