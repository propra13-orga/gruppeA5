package game.item;

import game.effect.EquipEffectArmor;
import game.effect.EquipEffectBasicAttack;
import game.effect.EquipEffectMaxMana;
import game.effect.IEquipEffect;
import game.inventory.EquipSlot;
import game.skill.DamageType;
import game.skill.Skill;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ItemTypeLoader {
	private BufferedReader m_reader = null;
	private String m_line = null;

	private final Pattern m_argumentSplit = Pattern.compile("([^\"]\\S*|\".+?\")\\s*");

	private HashMap<String, ItemType> m_itemTypes = new HashMap<>();
	
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
	
	private boolean loadSkill(){
		String identifier = "";
		int price = 0;
		String icon = "";
		String name = "";
		String desc = "";
		
		boolean isUsable = false;
		String usableSkillName = "";
		
		boolean isEquipable = false;
		EquipSlot equipSlot = EquipSlot.TORSO;
		String equipAppearance = "";
		IEquipEffect equipEffect = null;
		
		String[] words = m_line.split(" ");
		if(words.length != 3)
			return false;
			
		identifier = words[2];
		
		while(true){
			nextLine();
			
			if(m_line == null ||m_line.startsWith("#"))
				break;
			
			ArrayList<String> list = splitArguments(m_line);

			switch(list.get(0)){
			case "icon": icon = list.get(2).replace("\"", ""); break;
			case "name": name = list.get(2).replace("\"", ""); break;
			case "description": desc = list.get(2).replace("\"", ""); break;
			case "cost": price = Integer.parseInt(list.get(2)); break;
			case "makeUsable":
				isUsable = true;
				usableSkillName = list.get(1).replace("\"", "");
				break;
			case "makeEquipable":
				isEquipable = true;
				equipSlot = EquipSlot.valueOf( list.get(1) );
				equipAppearance = list.get(2).replace("\"", "");
				
				String ee = list.get(3);
				
				switch(ee){
					case "EquipEffectArmor":
						equipEffect = new EquipEffectArmor( Integer.parseInt(list.get(4)), DamageType.valueOf( list.get(5).toUpperCase() ) ); 
						break;
					case "EquipEffectBasicAttack":
						equipEffect = new EquipEffectBasicAttack( Integer.parseInt(list.get(4)), DamageType.valueOf( list.get(5).toUpperCase() ) ); 
						break;
					case "EquipEffectMaxMana":
						equipEffect = new EquipEffectMaxMana( Integer.parseInt(list.get(4)) ); 
						break;
					default:
						System.out.println("ItemTypeLoader.java: Error, EquipEffect type not found.");
						
				}
				
				break;	
				
			default: return false;
			}
		}
		
		ItemType t = new ItemType(icon, name, desc, price);
		
		if(isUsable)
			t.makeUsable( Skill.getSkill(usableSkillName) );
		if(isEquipable)
			t.makeEquipable(equipSlot, equipAppearance, equipEffect);
		
		m_itemTypes.put(identifier.toLowerCase(), t);
		
		return true;

	}
	
	public HashMap<String, ItemType> getItemTypeList(){
		return m_itemTypes;
	}

	/**
	 * Loads all item types from a textfile
	 * @param fileName	Relative path of text file.
	 * @return
	 */
	public boolean load(String fileName){
		boolean status = true;
		
		try{
			m_reader = new BufferedReader(new FileReader(fileName));
	        nextLine();
	        
	        while(m_line != null){
	        
	        	if( m_line.startsWith("#") ){
		        	status = status && loadSkill();
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
