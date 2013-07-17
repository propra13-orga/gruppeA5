import game.Constants;
import game.GSCredits;
import game.GSDefeat;
import game.GSGame;
import game.GSMainMenu;
import game.GSMultiplayerHost;
import game.GSMultiplayerJoin;
import game.GSTransition;
import game.combat.GSCombat;
import game.interaction.GSInteraction;
import game.inventory.GSInventory;
import game.quest.GSQuest;
import game.shop.GSShop;
import gamestate.GameStates;
import gamestate.GlobalGameState;
import gamestate.IGameState;
import std.StdDraw;
import std.StdIO;
import std.StdWin;
import std.anim.GlobalAnimQueue;

/*
 * http://opengameart.org/content/dungeon-crawl-32x32-tiles
 * http://www.photoshopstar.com/web-design/war-game-style-navigation/
 * http://corrupteddevelopment.com/old-curled-paper-texture/
 * http://archnophobia.deviantart.com/art/Free-Background-psd-72672793
 * http://forum.unity3d.com/threads/60284-Free-2d-sprite-animations
 * 
 */

public class Main {
	static boolean s_shutdown=false;
	
	public static void main(String[] args) {
		//Initialisiert ein Fenster mit der Standardgröße von 800x600 pixel.
		StdWin.init();
		
		//Erstelle alle game states
		GlobalGameState.associateGameState( GameStates.MAIN_MENU, new GSMainMenu()	 );
		GlobalGameState.associateGameState( GameStates.GAME, 	  new GSGame() 		 );
		GlobalGameState.associateGameState( GameStates.COMBAT, 	  new GSCombat() 	 );
		GlobalGameState.associateGameState( GameStates.TRANSITION,new GSTransition() );
		GlobalGameState.associateGameState( GameStates.SHOP,	  new GSShop() 		 );
		GlobalGameState.associateGameState( GameStates.INVENTORY, new GSInventory()	 );
		GlobalGameState.associateGameState( GameStates.QUEST,	  new GSQuest()	 );
		GlobalGameState.associateGameState( GameStates.DEFEAT,	  new GSDefeat()	 );
		GlobalGameState.associateGameState( GameStates.INTERACT,  new GSInteraction());
		GlobalGameState.associateGameState( GameStates.CREDITS,	  new GSCredits()	 );
		GlobalGameState.associateGameState( GameStates.MULTI_HOST,new GSMultiplayerHost()	);
		GlobalGameState.associateGameState( GameStates.MULTI_JOIN,new GSMultiplayerJoin()	);
		
		GlobalGameState.initiateGlobalGameState( GameStates.MAIN_MENU );

		//Der game loop:
		while(!s_shutdown){
			StdDraw.clear(StdDraw.BLACK);
			StdIO.sendAllEvents();
		
			IGameState igs = GlobalGameState.getActiveGameState();
			
			igs.update();
			GlobalAnimQueue.update();
			
			igs.render();
			GlobalAnimQueue.render();
		
			
			//Warte 16 frames, before das nächste Bild gerendert werden soll (=> 60 fps)
			StdDraw.show( Constants.FRAME_TIME );
			StdDraw.clear();
		}
		
		//Falls der loop zuende ist (also shutdown == true), wird das Fenster geschlossen.
		StdDraw.close();
	}

}
