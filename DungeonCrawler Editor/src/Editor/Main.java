package Editor;

import std.StdDraw;
import std.StdIO;
import std.StdWin;
import toolbar.Toolbar;


public class Main {

	static final int MAP_HEIGHT = 16;
	static final int MAP_LENGTH = 16;
	
	static final double SIZE = 32.00d;
		
	static boolean shutdown=false;
	
	private static Toolbar s_toolbar;
	
	public static void main(String[] args) {
	
		StdWin.init();
		StdWin.setTitle("No level loaded.");
		
		s_toolbar = new Toolbar();
		
        EditorMap map = new EditorMap(144,44, "data/tiles.txt", s_toolbar);
        
        s_toolbar.supplyMap(map);
		
		new MenuBar(map);
		
		while(!shutdown){
			StdDraw.clear(StdDraw.BLACK);
			StdIO.sendAllEvents();
			
			map.update();
			
			map.render();
			
			StdDraw.show(33);
		}
		
		StdDraw.close();
		
	}


}