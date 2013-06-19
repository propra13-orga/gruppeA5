package game.skill;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SkillLoader {
	private BufferedReader m_reader = null;
	private String m_line = null;

	private final Pattern m_argumentSplit = Pattern.compile("([^\"]\\S*|\".+?\")\\s*");

	private HashMap<String, Skill> m_skills = new HashMap<>();
	
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
		String type = "";
		String identifier = "";
		int manaCost = 0;
		String name = "";
		String sfx = null; //important
		String desc = "";
		int amount = 0;
		
		String[] words = m_line.split(" ");
		if(words.length != 3)
			return false;
			
		type = words[1];
		identifier = words[2];
		
		while(true){
			nextLine();
			
			if(m_line == null ||m_line.startsWith("#"))
				break;
			
			ArrayList<String> list = splitArguments(m_line);
			
			if( list.size() != 3 || !list.get(1).equals("=") ){
				System.out.println("Error, Skills line: " + m_line);
				return false;
			}
			
			switch(list.get(0)){
			case "name": name = list.get(2).replace("\"", ""); break;
			case "description": desc = list.get(2).replace("\"", ""); break;
			case "amount": amount = Integer.parseInt(list.get(2)); break;
			case "cost": manaCost = Integer.parseInt(list.get(2)); break;
			case "sfx": sfx = list.get(2).replace("\"", ""); break;
			default: return false;
			}
		}
		
		Skill sk;
		
		switch(type){
		case "SingleTargetDmgSkill": 
			sk = new SingleTargetDmgSkill(name, desc, amount,manaCost); 
			if(sfx != null) sk.setSfx(sfx);
			break;
			
		case "MultiTargetDmgSkill": 
			sk = new MultiTargetDmgSkill(name, desc, amount,manaCost); 
			if(sfx != null) sk.setSfx(sfx);
			break;
			
		case "SingleTargetHealSkill": 
			sk = new SingleTargetHealSkill(name, desc, amount,manaCost); 
			if(sfx != null) sk.setSfx(sfx);
			break;
			
		case "SelfHealSkill":
			sk = new SelfHealSkill(name, desc, amount, manaCost);
			if(sfx != null) sk.setSfx(sfx);
			break;
		
		case "SelfManaSkill":
			sk = new SelfManaSkill(name, desc, amount, manaCost);
			if(sfx != null) sk.setSfx(sfx);
			break;
			
		default:
			System.out.println("Error. Unrecognized skill type.");
			return false;
			
		}
		
		m_skills.put(identifier.toLowerCase(), sk);
		return true;

	}
	
	public HashMap<String, Skill> getSkillList(){
		return m_skills;
	}

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
