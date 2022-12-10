package Game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.Timer;

public class Game implements Runnable, ActionListener{
	
	public boolean live;
	public String name;
	public String p1name;
	public String p2name;
	ServerSocket join;
	Socket p1;
	DataOutputStream os1;
	DataInputStream is1;
	Socket p2;
	DataOutputStream os2;
	DataInputStream is2;
	Timer t;
	Timer p;
	boolean p1Ready;
	boolean p2Ready;
	public Game(Socket p1, Socket p2, String name, String p1name, String p2name) {
		this.name = name;
		this.p1 = p1;
		this.p2 = p2;
		this.p1name = p1name;
		this.p2name = p2name;
		live = true;
		p1Ready = false;
		p2Ready = false;
		try {
			os1 = new DataOutputStream(p1.getOutputStream());
			is1 = new DataInputStream(p1.getInputStream());
			os2 = new DataOutputStream(p2.getOutputStream());
			is2 = new DataInputStream(p2.getInputStream());
		} catch (IOException e) {
e.printStackTrace();		
	}
		
	}
	
	
	
	public void run() {
		t = new Timer(20, this);
		p = new Timer(100,this);
		live = true;
		t.start();
		p.start();
	}
	void quit() {
		live = false;
		try {
			p1.close();
			p2.close();
			os1.close();
			os2.close();
			is1.close();
			is2.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		t.stop();
	}
	public void actionPerformed(ActionEvent e) {
		if(p1Ready && p2Ready&& e.getSource() == t) {
			p1Ready = false;
			p2Ready = false;
			System.out.println("Gaming");
			try {
				os1.writeUTF("Game:");
				os2.writeUTF("Game:");
				os1.writeUTF("Turn:");
				
			} catch (IOException e1) {
				quit();
			}
		}
			try {
				if(is1.available()>0) {
					String w = is1.readUTF().replaceAll("\\$", "");
					if(w.startsWith("msg:")) {
						os2.writeUTF(w);
					} else if(w.startsWith("Waiting")) {
						p1Ready = true;
						System.out.println("p1 is ready");
					} else if(w.startsWith("Fire:")) {
						os2.writeUTF(w);
					} else if(w.startsWith("Hit")||w.startsWith("Miss")) {
						os2.writeUTF(w);
						os1.writeUTF("Turn:");
					}
				}
				if(is2.available()>0) {
					String w = is2.readUTF().replaceAll("\\$", "");
					if(w.startsWith("msg:")) {
						os1.writeUTF(w);
					} else if(w.startsWith("Waiting")) {
						p2Ready = true;
						System.out.println("p2 is ready");
					} else if(w.startsWith("Fire:")) {
						os1.writeUTF(w);
					} else if(w.startsWith("Hit")||w.startsWith("Miss")) {
						os1.writeUTF(w);
						os2.writeUTF("Turn:");
					}
				}
			} catch (IOException e2) {
				quit();
			}
			try {
				if(e.getSource()==p) {
				os1.writeUTF("$");
				os2.writeUTF("$");
				}
				if(is1.available() == -1 || is2.available() == -1 || p1.isClosed() || p2.isClosed()) {
					quit();
				}
			} catch (IOException e1) {
				quit();
			}	
	}

}
