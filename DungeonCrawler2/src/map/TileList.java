package map;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TileList is mostly used for internal management. Users should not be required to access it by themselves.
 * It contains a listing of all tile types and their associated information (file path to the image that represents the tile,
 * pathability information, etc).
 */
public class TileList {

	public class TileType{
		public String mFilePath;
		public String mName;
		public boolean mPathable;
	}
	
	ArrayList<TileType> m_tileTypes = new ArrayList<TileType>();

	private BufferedReader m_reader = null;
	private String m_line = null;
	private final static Pattern s_regex = Pattern.compile("([^\"]\\S*|\".+?\")\\s*");
	
	
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

	private void process(){
		ArrayList<String> list = new ArrayList<String>();
		Matcher m = s_regex.matcher(m_line);
	
		while (m.find())
		    list.add(m.group(1));
		    
		if(list.size() != 3){
			System.out.println("Error, bad line length.");
			return;
		}
		
		TileType t = new TileType();
		
		t.mPathable = list.get(0).equals("0") ? true : false;
		t.mName = list.get(1).replace("\"", "");
		t.mFilePath = list.get(2).replace("\"", "");
		
		m_tileTypes.add(t);
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
	
		while(m_line != null){
			process();
			nextLine();
		}
	
	}

	/**
	 *  Returns the number of tile types that this TileList contains
	 */
	public int size(){
		return m_tileTypes.size();
	}
	
	/**
	 * Returns the TileType object at position index
	 */
	public TileType get(int index){
		return m_tileTypes.get(index);
	}

}
