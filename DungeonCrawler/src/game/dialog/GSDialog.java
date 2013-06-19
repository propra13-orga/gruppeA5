package game.dialog;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import std.StdDraw;
import std.StdIO;
import std.StdIO.KeyEventType;
import gamestate.GameStates;
import gamestate.GlobalGameState;
import gamestate.IGameState;


public class GSDialog implements IGameState, StdIO.IKeyListener {

	private ArrayList<String> sphinxRiddle = new ArrayList<String>();
	private double rectY = 348;
	
	@Override
	public void render() {
		StdDraw.setPenColor(StdDraw.WHITE);
		
		double y = 100;
		for (int i=0;i<sphinxRiddle.size();i++){
			StdDraw.text(400, y, sphinxRiddle.get(i));	
			y += 20;
		}
		StdDraw.rectangle(325,rectY,150,20);
	}

	@Override
	public void update(){}
	
	@Override
	public void onEnter() {
		System.out.println("enter");
		StdIO.addKeyListener(this, KeyEventType.KeyPressed);
		sphinxRiddle = readDialog(sphinxRiddle, "data/dialog/riddle1.txt");
	}

	@Override
	public void onExit() {
		StdIO.removeKeyListener(this, KeyEventType.KeyPressed);
	}

	@Override
	public void receiveEvent(KeyEvent e) {
		if( e.getKeyCode() == KeyEvent.VK_ENTER ){
			if (rectY == 428){
				GlobalGameState.setActiveGameState( GameStates.GAME );
			}
			/*
			if ( getSolution("data/dialog/solution.txt") ){
				//korrekte Antwort
			}
			else{
				//falsche Antwort
			}
			*/
		}
		if( e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP && rectY>348 ){
			rectY -= 20;
		}
		if( e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN && rectY<428){
			rectY += 20;
		}
	}
	
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
	
	//noch nicht gebraucht
	public boolean getSolution(String path){
		Scanner scanner = new Scanner(path);
		int solution = scanner.nextInt();
		scanner.close();
		if (solution == rectY){return true;}
		else return false;
	}
	
}