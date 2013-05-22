import javax.swing.*;

import std.StdWin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class MainMenu extends JPanel implements ActionListener{

	private boolean spielStarten; //spielStarten wird auf true gesetzt, sobald der Starten button gedrückt wird
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
		
		starten = new JButton ("Spiel starten"); /*Button wird erstellt auf der Oberfläche*/
		starten.setBounds(320, 80, 160, 40);
		starten.addActionListener(this);
		add(starten);
		
		beenden = new JButton ("Spiel beenden"); /*Button wird erstellt auf der Oberfläche*/
		beenden.setBounds(320, 180, 160, 40);
		beenden.addActionListener(this);
		add(beenden);
		
		showMenu(); //Zeige das Spielmenü an
	}

	public void actionPerformed(ActionEvent a) {
	
		if (a.getSource() == starten) {
			spielStarten = true; /*Wenn starten Button angeklickt wird das Programm gestartet*/
			hideMenu(); //verstecke das Spielmenü wieder
		} 
		if (a.getSource() == beenden) { /*Wenn beenden Button angeklickt wird wird Hauptmenü geschlossen*/
			System.exit(0);
		}
	}

/*
	public static void spielmenü(){
		JFrame spielmenü = new JFrame("DungeonCrawler");
		spielmenü.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		spielmenü.setSize(800,600); //Größe des Spielfelds
		spielmenü.setVisible(true); //sichtbar
	
	}
*/
}