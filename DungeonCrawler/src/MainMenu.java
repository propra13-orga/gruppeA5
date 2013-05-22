import javax.swing.*;

import std.StdWin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class MainMenu extends JPanel implements ActionListener{

	private boolean spielStarten; //spielStarten wird auf true gesetzt, sobald der Starten button gedr�ckt wird
	private JButton beenden; /*2 Buttons zum Starten und Beenden*/
	private JButton starten;

	public boolean wantStartGame(){
		return spielStarten;
	}
	
	public void showMenu(){
		//Ordert StdWin dieses Menu anzuzeigen, anstelle der normalen Spiels
		spielStarten = false;
		StdWin.setContentPane(this);
	}
	public void hideMenu(){
		//Ordert StdWin wiedes das normale Spiel anzuzeigen.
		StdWin.setContentPane(null);
	}

	public MainMenu(String title) {
	     
		super(null);
		
		spielStarten = false;
		
		starten = new JButton ("Spiel starten"); /*Button wird erstellt auf der Oberfl�che*/
		starten.setBounds(320, 80, 160, 40);
		starten.addActionListener(this);
		add(starten);
		
		beenden = new JButton ("Spiel beenden"); /*Button wird erstellt auf der Oberfl�che*/
		beenden.setBounds(320, 180, 160, 40);
		beenden.addActionListener(this);
		add(beenden);
		
		showMenu(); //Zeige das Spielmen� an
	}

	public void actionPerformed(ActionEvent a) {
	
		if (a.getSource() == starten) {
			spielStarten = true; /*Wenn starten Button angeklickt wird das Programm gestartet*/
			hideMenu(); //verstecke das Spielmen� wieder
		} 
		if (a.getSource() == beenden) { /*Wenn beenden Button angeklickt wird wird Hauptmen� geschlossen*/
			System.exit(0);
		}
	}

/*
	public static void spielmen�(){
		JFrame spielmen� = new JFrame("DungeonCrawler");
		spielmen�.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		spielmen�.setSize(800,600); //Gr��e des Spielfelds
		spielmen�.setVisible(true); //sichtbar
	
	}
*/
}