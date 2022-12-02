package Game;

import java.awt.HeadlessException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class Runner {
	
	public static void main(String... args) {
		String[] a = {"Host A Server","Join Local Server","Join Non-Local Server","Quit"};
		int result = JOptionPane.showOptionDialog(null,"What would you like to do?", "BattleShip", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,    a,null);
		if(result == 0) {
		try {
			JOptionPane.showMessageDialog(null, "Server is being run at this ip: "+InetAddress.getLocalHost().getHostAddress()+" on port 2245");
		}catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Unable to retrieve IP");
			System.exit(0);
		}
		Server s = new Server();
		} else if(result == 1) {
		Client s = new Client(""+JOptionPane.showInputDialog("What is your username?"),"");
		} else if(result == 2) {
			Client s = new Client(""+JOptionPane.showInputDialog("What is your username?"),JOptionPane.showInputDialog("What is the IP of the server you would like to join?"));
		} else {
			System.exit(0);
		}
	}
}
