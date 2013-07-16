package game.quest;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import std.StdDraw;
import std.StdIO;
import std.StdIO.KeyEventType;
import game.player.Player;
import gamestate.GameStates;
import gamestate.GlobalGameState;
import gamestate.IGameState;


public class GSQuest implements IGameState, StdIO.IKeyListener {

	private ArrayList<String> sphinxRiddle = new ArrayList<String>();
	private ArrayList<String> solutions = new ArrayList<String>();
	private ArrayList<String> response = new ArrayList<String>();
	private double rectY = 428;
	private int questCount = 0, questState;
	
	@Override
	public void render() {
		StdDraw.setPenColor(StdDraw.WHITE);
		double y = 100;
		
		switch (questState){
		
			case 0: 
				for (int i = questState*18;i<(questState+1)*18;i++){
					StdDraw.text(400, y, response.get(i));	
					y += 20;
				}
			break;
				
			case 1:	
				for (int i = questState*18;i<(questState+1)*18;i++){
					StdDraw.text(400, y, response.get(i));	
					y += 20;
				}
			break;
				
			case 2:	
				for (int i = questState*18;i<(questState+1)*18;i++){
					StdDraw.text(400, y, response.get(i));	
					y += 20;
				}
			break;
				
			case 3:
				StdDraw.rectangle(325,rectY,150,20);
				for (int i = questCount*21;i<(questCount+1)*21;i++){
					StdDraw.text(400, y, sphinxRiddle.get(i));	
					y += 20;
				}
			break;
		}
	}

	@Override
	
	public void update(){
	}
	
	@Override
	public void onEnter() {
		System.out.println("enter");
		StdIO.addKeyListener(this, KeyEventType.KeyPressed);

		if (questCount == 3){
			questState = 2;
		}
		else questState = 3;
		sphinxRiddle = readDialog(sphinxRiddle, "data/quest/riddle.txt");
		solutions = readDialog(solutions, "data/quest/solution.txt");
		response = readDialog(response, "data/quest/response.txt");
	}

	@Override
	public void onExit() {
		StdIO.removeKeyListener(this, KeyEventType.KeyPressed);
	}
	 
	@Override
	public void receiveEvent(KeyEvent e) {
		if( e.getKeyCode() == KeyEvent.VK_ENTER ){
			switch (questState){
			case 0: case 1: case 2:
				GlobalGameState.setActiveGameState( GameStates.GAME);
				break;
			case 3:
				if (rectY == 428){
					GlobalGameState.setActiveGameState( GameStates.GAME );
				}
				
				else if ( getSolution(solutions) ){
					Player.getInstance().addGold(100);
					questCount++;
					questState = 0;
				}
				else {
					questCount++;
					questState = 1;
				}
			}
		}
		if( (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) && rectY>348 ){
			rectY -= 20;
		}
		if( (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) && rectY<428){
			rectY += 20;
		}
	}
	
	// reads .txt-file into ArrayList
	public ArrayList<String> readDialog (ArrayList<String> list, String path){
		try {
			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);
			String line;
			ArrayList<String> lineList = new ArrayList<String>();
			while ((line = br.readLine()) != null){
				lineList.add(line);
			}
			list = lineList;
			br.close();
		}
		catch (FileNotFoundException e){ }
		catch (IOException e){  }	
		return list;
	}
	
	// checks file for the required value for rectY
	public boolean getSolution(ArrayList<String> solutions){
		int solution = Integer.parseInt(solutions.get(questCount));
		if (solution == rectY){return true;}
		else return false;
	}
	
}