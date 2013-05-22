package map;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * TileList is mostly used for internal management. Users should not be required to access it by themselves.
 * It contains a listing of all tile types and their associated information (file path to the image that represents the tile,
 * pathability information, etc).
 */
public class EditorTileList {
	public class TileVariation{
		public char mId;
		public int mWeight;
		public String mFilePath;
	}

	public class EditorTileType{
		public char mId;
		public String mFilePath;
		public String mName;
		public boolean mPathable;
		public ArrayList<TileVariation> mVariations = null;
		
		public String toString(){
			return "[" + (int)mId + ", " + mFilePath + ", " + mName + ", " + mPathable + "]";
		}
	}
	
	HashMap<String, HashMap<String, ArrayList<EditorTileType>>> m_tiles;

	private BufferedReader m_reader = null;
	private String m_line = null;
	private final static Pattern s_regex = Pattern.compile("([^\"]\\S*|\".*?\")\\s*"); //"([^\"\\s]\\S*|\".+?\")\\s*" to remove all whitespace at the start
	
	private static EditorTileList s_instance = null;
	
	public static EditorTileList getInstance(){
		return s_instance;
	}
	
	private ArrayList<String> splitLine(String line){
		ArrayList<String> list = new ArrayList<String>();
		Matcher m = s_regex.matcher(m_line);
	
		while (m.find())
		    list.add(m.group(1));
		    
		return list;
	}
	
	private void nextLine(){
		try {
			do{
				m_line = m_reader.readLine(); 
			} while(m_line != null && (m_line.trim().isEmpty() || m_line.equals("") || m_line.startsWith("//")) );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(m_line != null)
			m_line = m_line.trim();
	}

	private boolean readTileVariation(EditorTileType tileType){
		if(m_line.startsWith("#"))
			return false;
	
		nextLine();
		ArrayList<String> list = splitLine(m_line);
		boolean continueAfterwards = false;
		
		if( list.size() == 4 && list.get(3).equals("&&") )
			continueAfterwards = true;
		else if( list.size() != 3 ){
			System.out.println("Error, invalid TileType Variation line." + m_line);
			return false;
		}
		
		TileVariation tv = new TileVariation();
		tv.mId = (char) Integer.parseInt( list.get(0) );
		tv.mWeight = Integer.parseInt( list.get(1) );
		tv.mFilePath = list.get(2).replace("\"", "");
	
		if(tileType.mVariations.size() > 0)
			tv.mWeight += tileType.mVariations.get(tileType.mVariations.size()-1).mWeight; //Add weights together
	
		tileType.mVariations.add( tv );
	
		if(continueAfterwards)
			return readTileVariation(tileType);
		else
			return true;
	}

	private boolean readTileType(ArrayList<EditorTileType> subSet){
		if(m_line.startsWith("#"))
			return false;
	
		ArrayList<String> list = splitLine(m_line);
		
		if(list.size() != 4){
			System.out.println("Error, invalid TileType line: " + m_line);
			return false;
		}
		
		EditorTileType t = new EditorTileType();
	
		//If we have a TileType Variation..
		if(list.get(3).equals("=")){
			t.mId = 0;
			t.mPathable = list.get(0).equals("0") ? true : false;
			t.mName = list.get(1).replace("\"", "");
			t.mFilePath = list.get(2).replace("\"", "");
			
			t.mVariations = new ArrayList<TileVariation>();
			
			if( readTileVariation(t) ){
				subSet.add(t);
				return true;
			}else
				return false;
				
		}
	
		//Else: A normal TileType
		t.mId = (char) Integer.parseInt( list.get(0) );
		t.mPathable = list.get(1).equals("0") ? true : false;
		t.mName = list.get(2).replace("\"", "");
		t.mFilePath = list.get(3).replace("\"", "");
		
		subSet.add(t);
		
		return true;
	}

	private boolean readSubset(HashMap<String, ArrayList<EditorTileType>> set){
		ArrayList<String> list = splitLine(m_line);
		
		if( list.size() != 3 || !m_line.startsWith("# Subset") ){
			System.out.println("Error, expected \"# Subset\" command.");
			return false;
		}
		
		nextLine();
		
		ArrayList<EditorTileType> subSet = new ArrayList<EditorTileType>();
		
		while(m_line != null && !m_line.startsWith("# Subset") ){
			if( !readTileType( subSet ) )
				break;
			nextLine();
		}
		
		set.put(list.get(2).replace("\"", ""), subSet);
		
		return true;
	}

	private boolean readSet(){
		ArrayList<String> list;

		list = splitLine(m_line);

		if( list.size() != 3 || !m_line.startsWith("# Set") ){
			System.out.println("Error, expected \"# Set\" command.");
			return false;
		}

		nextLine();

		HashMap<String, ArrayList<EditorTileType>> set = new HashMap<String, ArrayList<EditorTileType>>();

		while(m_line != null && !m_line.startsWith("# Set") )
			if( !readSubset( set ) )
				break;
		
		m_tiles.put( list.get(2).replace("\"", ""), set );

		return true;
	}

	/**
	 * Loads the tile list from a text file with the name path
	 */
	public void loadFromFile(String path){
		
		try {
			m_reader = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		nextLine();
	
		m_tiles = new HashMap<String, HashMap<String, ArrayList<EditorTileType>>>();
	
		while(m_line != null)
			if( !readSet() )
				break;
		
	/*
		for( Entry<String, HashMap<String, ArrayList<EditorTileType>>> set : m_tiles.entrySet() ){
			for( Entry<String, ArrayList<EditorTileType>> subset : set.getValue().entrySet() ){
				for( EditorTileType tt : subset.getValue() ){
					System.out.println( set.getKey() + " -> " + subset.getKey() + " -> " + tt );
				}
			} 
		}
	*/
	}

	public boolean saveToTilesTXT(String fileName){
	
		ArrayList<EditorTileType> list = new ArrayList<EditorTileType>();
	
		for( Entry<String, HashMap<String, ArrayList<EditorTileType>>> set : m_tiles.entrySet() ){
			for( Entry<String, ArrayList<EditorTileType>> subset : set.getValue().entrySet() ){
				for( EditorTileType tt : subset.getValue() ){
				
					if( tt.mVariations == null )
						list.add( tt );
					else{
						for( TileVariation tv : tt.mVariations ){
							EditorTileType t = new EditorTileType();
							t.mId = tv.mId;
							t.mName = tt.mName;
							t.mPathable = tt.mPathable;
							t.mFilePath = tv.mFilePath;
							list.add(t);
						}
					}
				}
			} 
		}
		
		//Sort entries by id so we can just print them in one go.
		Collections.sort( list, new Comparator<EditorTileType>() {
			@Override
			public int compare(EditorTileType o1, EditorTileType o2) {
				return o1.mId - o2.mId;
			}
		} );
		
		try {
			BufferedWriter br = new BufferedWriter(new FileWriter(fileName));
			
			
			for( EditorTileType t : list ){
				br.write( (t.mPathable ? "0" : "1") + " \"" + t.mName + "\" \"" + t.mFilePath + "\"\n");
			}
					
			br.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	public EditorTileList(){
		s_instance = this;
	}

	public HashMap<String, ArrayList<EditorTileType>> getSet(String setName){
		return m_tiles.get(setName);
	}
	public HashMap<String, HashMap<String, ArrayList<EditorTileType>>> getAllSets(){
		return m_tiles;
	}
}
