package map;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Set;

public class LevelSaver {
	static final int MAP_HEIGHT = 16;
	static final int MAP_LENGTH = 16;

	public void createBlankLevel(String filePath){
		try {
			BufferedWriter br = new BufferedWriter(new FileWriter(filePath));
			
			
			br.write("# Map\n\n");
			
			String line = "";
			
			for(int i=0; i<MAP_LENGTH;i++){
				line += "0";
			}
			line += "\n";
			
			for(int y = 0; y < MAP_HEIGHT; y++){
				br.write(line); // 16x 0
			}
			
			
			br.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void saveToFile(String filePath, char [][] grid, Level level){

		try {
			BufferedWriter br = new BufferedWriter(new FileWriter(filePath));
			
			int maxHeight = grid.length;
			int maxLength = grid[0].length;
			
			br.write("# Map\n\n");
			
			for(int y = 0; y < maxHeight; y++){
				for(int x=0; x<maxLength; x++){
					br.write(grid[x][y]+'0');
				}
				br.newLine();
			}
			
			br.write("\n# Events\n\n");
			
			Set<Entry<Coordinate, CellInfo>> cellSet = level.getAllCellInfo().entrySet();
			
			for (java.util.Map.Entry<Coordinate, CellInfo> entry : cellSet) {
			    Coordinate c = entry.getKey();
			    CellInfo ce = entry.getValue();
			    
			    if(ce.mHasEvent)
		        	br.write(c.mX + " " + c.mY + " " + ce.mEventID + "\n");
			}
			
			br.write("\n# Teleporters\n\n");
			
			for (java.util.Map.Entry<Coordinate, CellInfo> entry : cellSet) {
			    Coordinate c = entry.getKey();
			    CellInfo ce = entry.getValue();
			    
			    if(ce.mHasTeleporter)
		        	br.write(c.mX + " " + c.mY + " \"" + ce.mNewMap + "\" " + ce.mNewPosition.mX + " " + ce.mNewPosition.mY + "\n");
			}
			
			br.write("\n# MonsterGroups\n\n");
			
			for (java.util.Map.Entry<Coordinate, Monster> entry : level.getAllMonsterInfo().entrySet()) {
			    Coordinate c = entry.getKey();
			    Monster ce = entry.getValue();
			    
			    if( !ce.mIcon.equals("") && !ce.mMonsters.equals("") )
			    	br.write(c.mX + " " + c.mY + " \"" + ce.mIcon + "\" " + ce.mMonsters + "\n");
			}
			
			br.write("\n# Pickups\n\n");
			
			for (java.util.Map.Entry<Coordinate, Item> entry : level.getAllItemInfo().entrySet()) {
			    Coordinate c = entry.getKey();
			    Item ce = entry.getValue();
			    
			    if( !ce.mType.equals("") )
			    	br.write(c.mX + " " + c.mY + " " + ce.mType + "\n");
			}
			
			
			br.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
}
