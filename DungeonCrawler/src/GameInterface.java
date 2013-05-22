import javax.swing.JOptionPane;

import std.StdDraw;


public class GameInterface {
	public void ui(){
		//loadpics();
		String hearts = JOptionPane.showInputDialog("Hallo mutiger Krieger. Willkommen bei deinem Abenteuer. Wie viele Herzen möchtest du haben? Umso weniger du wählst, desto schwerer wird es!");
		JOptionPane.showMessageDialog(null, ("Hier sind noch 3 Manatränke"));
		
		//int hcounter = Integer.parseInt(hearts);
		StdDraw.textLeft( 10, 20, "Leben:" );
		StdDraw.textLeft( 150, 20, "Mana:" );
		StdDraw.textLeft( 300, 20, "Item:");
	}
}
	