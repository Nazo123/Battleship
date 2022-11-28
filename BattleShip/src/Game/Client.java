package Game;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;
import javax.swing.Timer;

public class Client implements ActionListener {
	boolean inQueue;
	Socket w;
	DataInputStream is;
	DataOutputStream os;
	Timer t;
	Timer p;
	String name;
	String ip;
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
		t = new Timer(10, this);
		p = new Timer(100, this);
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
		t.start();
		p.start();
		inQueue = true;
	}
	public void quit() {
		t.stop();
		JOptionPane.showMessageDialog(null, "Connection Lost");
		System.exit(0);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == p) {
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
				System.out.print(msg);
				if(!msg.equals("wait")&&!msg.equals("start")&&msg.contains("The Cur")) {
				os.writeUTF(JOptionPane.showInputDialog(msg));
				inQueue = true;
				} else if(msg.equals("start")){
					inQueue = false;
				}
			} 
		} catch (IOException e1) {
			quit();
		}
	}
		
	}
}
