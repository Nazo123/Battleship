package Game;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Client implements ActionListener {
	ArrayList<BattleShip> ships = new ArrayList<BattleShip>();
	boolean inQueue;
	Socket w;
	DataInputStream is;
	DataOutputStream os;
	Timer t;
	Timer ping;
	String name;
	String ip;
	JFrame f;
	DisplayPanel p;
	String gameName;
	public Client(String name, String ip) {
		if(name.equals("") || name.equals(null)) {
			JOptionPane.showMessageDialog(null, "Invalid username");
			System.exit(0);
		}
		if(ip.equals("")) {
			this.ip = ip;
		} else {
			try {
				this.ip = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				JOptionPane.showMessageDialog(null, "Local IP could not be found");
				System.exit(0);
			}
		}
		this.name = name;
		t = new Timer(11, this);
		ping = new Timer(100, this);
		try {
			w = new Socket(this.ip,2245);
			is = new DataInputStream(w.getInputStream());
			os = new DataOutputStream(w.getOutputStream());
			os.writeUTF(name);
		} catch (UnknownHostException e) {
			quit();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "There is no server running at this IP. If non-local make sure this IP is valid/correct");
			t.stop();
			System.exit(0);
		}
		ships.add(new BattleShip(2));
		ships.add(new BattleShip(3));
		ships.add(new BattleShip(3));
		ships.add(new BattleShip(4));
		ships.add(new BattleShip(5));
		inQueue = true;
		f = new JFrame();
		p = new DisplayPanel();
		f.setMinimumSize(new Dimension(500,800));
		f.add(p);
		p.setMinimumSize(new Dimension(500,800));
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		t.start();
		ping.start();
}
	public void quit() {
		t.stop();
		JOptionPane.showMessageDialog(f, "Connection Lost");
		System.exit(0);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		p.h = f.getHeight();
		p.w = f.getWidth();
		f.repaint();
		p.repaint();
		if(e.getSource() == ping) {
		try {
			os.writeUTF("$");
			if(is.available()==-1 || w.isClosed()) {
				quit();
			}
		} catch (IOException e1) {
			quit();
		}
		}
		if(inQueue) {
		try {
			if(is.available()>0) {
				String msg = is.readUTF().replaceAll("\\$", "");
				if(!msg.equals("wait")&&!msg.equals("start")&&msg.contains("The Cur")) {
				gameName = JOptionPane.showInputDialog(msg);
				os.writeUTF(gameName);
				inQueue = true;
				f.setTitle("Waiting for players to join: "+gameName);
				f.setVisible(true);
				} else if(msg.equals("start")){
					inQueue = false;
					f.setTitle("You are currently setting up your ships. Click the button to enter");
				}
			}
		} catch (IOException e1) {
			quit();
		}
	}
		
		
	}
}
