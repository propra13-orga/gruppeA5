package monster;

import entity.UnitStats;
import game.skill.DamageType;
import game.skill.SingleTargetDmgSkill;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MonsterTypeLoader {
	private BufferedReader m_reader = null;
	private String m_line = null;

	private final Pattern m_argumentSplit = Pattern.compile("([^\"]\\S*|\".+?\")\\s*");

	private HashMap<String, MonsterType> m_monsterTypes = new HashMap<>();
	
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
	
	private ArrayList<String> splitArguments(String line){
		ArrayList<String> list = new ArrayList<String>();
		Matcher m = m_argumentSplit.matcher(line);

		while (m.find())
		    list.add(m.group(1));
		   
		return list;
	}
	
	private boolean loadMonsterType(){
		String identifier = "";
		String icon = "";
		String name = "";
		int damage = 0;
		DamageType dmgType = DamageType.PHYSICAL;
		
		UnitStats stats = new UnitStats();
		stats.mMaxHealth = 1;
		
		String[] words = m_line.split(" ");
		if(words.length != 3)
			return false;
		identifier = words[2];
		
		while(true){
			nextLine();
			
			if(m_line == null ||m_line.startsWith("#"))
				break;
		
			ArrayList<String> list = splitArguments(m_line);
			
			if( !list.get(1).equals("=") ){
				System.out.println("Error, MonsterType line: " + m_line);
				return false;
			}
			
			switch(list.get(0)){
			case "icon": icon = list.get(2).replace("\"", ""); break;
			case "name": name = list.get(2).replace("\"", ""); break;
			case "damage": 
				damage = Integer.parseInt(list.get(2)); 
				dmgType = DamageType.valueOf(list.get(3).toUpperCase());
				break;
			case "health": stats.mMaxHealth = Integer.parseInt(list.get(2)); break;
			case "mana": stats.mMaxMana = Integer.parseInt(list.get(2)); break;
			case "phyDef": stats.mArmor = Integer.parseInt(list.get(2)); break;
			case "iceDef": stats.mIceResist = Integer.parseInt(list.get(2)); break;
			case "fireDef": stats.mFireResist = Integer.parseInt(list.get(2)); break;
			default: return false;
			}
			
		}
		
		MonsterType m = new MonsterType(stats, icon, name, new SingleTargetDmgSkill("Attack", "", damage, dmgType, 0));
		m_monsterTypes.put(identifier, m);
		return true;

	}
	
	public HashMap<String, MonsterType> getMonsterTypes(){
		return m_monsterTypes;
	}

	public boolean load(String fileName){
		boolean status = true;
		
		try{
			m_reader = new BufferedReader(new FileReader(fileName));
	        nextLine();
	        
	        while(m_line != null){
	        
	        	if( m_line.startsWith("# MonsterType") ){
		        	status = status && loadMonsterType();
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
