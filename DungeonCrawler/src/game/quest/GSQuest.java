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

/**
 * 
 * @author 
 *
 */
public class GSQuest implements IGameState, StdIO.IKeyListener {
	
	private ArrayList<String> sphinxRiddle = new ArrayList<String>();
	private ArrayList<String> solutions = new ArrayList<String>();
	private ArrayList<String> response = new ArrayList<String>();
	private double rectY = 428;
	private int questCount = 0, questState;
	
	/**
	 * draws texts from the ArrayLists sphinxRiddle/response on the screen 
	 * depending on the active questState<br>
	 * when in questState 3 it draws a rectangle for selecting an answer
	 */
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
	public void update(){}
	
	/**
	 * when entering the QUEST-gameState<br>
	 * - the keyListener is added<br>
	 * - the default questState is initialized<br>
	 * - the required txt.files are stored in ArrayLists
	 */
	@Override
	public void onEnter() {
		System.out.println("enter");
		StdIO.addKeyListener(this, KeyEventType.KeyPressed);

		sphinxRiddle = readDialog(sphinxRiddle, "data/quest/riddle.txt");
		solutions = readDialog(solutions, "data/quest/solution.txt");
		response = readDialog(response, "data/quest/response.txt");

		if (questCount == sphinxRiddle.size()/21){
			questState = 2;
		}
		else questState = 3;
	}
	
	/**
	 * when leaving the QUEST-gameState:<br>
	 * - the keyListener is removed
	 */
	@Override
	public void onExit() {
		StdIO.removeKeyListener(this, KeyEventType.KeyPressed);
	}
	 
	/**
	 * waits for a keyboard input by the user <br>
	 * allows the following keys:<br>
	 * "Enter", "W", "S", "UP", "DOWN"
	 * depending on the pressed key one of the following options kicks in:<br>
	 * - the QUEST-gameState is left<br>
	 * - the rectangle is moved vertically<br>
	 * - the selcted answer is checked for correctness
	 */
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
	
	/**
	 * reads out a txt.file line by line and stores the lines as Strings in an ArrayList 
	 * @param list the name of the ArrayList in which the Strings are saved
	 * @param path the path where the txt.file lies
	 * @return
	 */
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
	
	/**
	 * takes an IntegerElement from the ArrayList solutions and checks if it
	 * equals to the y.coordinate of the rectangle<br>
	 * if it does, the method returns "true"
	 * else it returns "false"
	 * @param solutions the ArrayList in which the solutions for the riddles are saved
	 * @return
	 */
	public boolean getSolution(ArrayList<String> solutions){
		int solution = Integer.parseInt(solutions.get(questCount));
		if (solution == rectY){return true;}
		else return false;
	}
	
}