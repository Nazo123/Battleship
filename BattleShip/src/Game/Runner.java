package Game;

import javax.swing.JOptionPane;

public class Runner {
	
	public static void main(String... args) {
		String[] a = {"Host Local Server","Join Local Server","Quit"};
		int result = JOptionPane.showOptionDialog(null,"What would you like to do?", "BattleShip", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,    a,null);
		if(result == 0) {
		Server s = new Server();
		} else if(result == 1) {
		Client s = new Client(JOptionPane.showInputDialog("What is your username?"));
		}
	}
}
