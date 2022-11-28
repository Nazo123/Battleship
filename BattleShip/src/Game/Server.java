package Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.Timer;

public class Server implements ActionListener {
	ArrayList<GameHolder> games = new ArrayList<GameHolder>();
	ArrayList<ClientHolder> waiting = new ArrayList<ClientHolder>();
	ArrayList<ClientHolder> toRemove = new ArrayList<ClientHolder>();
	HashMap<String,ClientHolder> queue = new HashMap<String,ClientHolder>();
	ServerSocket join;
	JFrame f;
	JPanel p;
	JLabel l;
	 Font font = new Font("Courier", Font.PLAIN,20);

	
	Timer t = new Timer(10, this);
	public Server() {
		f = new JFrame();
		p = new JPanel();
		l = new JLabel();
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setTitle("Server");
		l.setText("<html>The current unqueued players are:<br> The current unqueued players are: <br> The current unqueued players are:");
		p.add(l);
		f.add(p);
		f.pack();
		t.start();
				try {
					join = new ServerSocket(2245);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				/*
				while(true) {
					
					Socket p1;
					Socket p2;
					try {
						p1 = join.accept();
						p2 = join.accept();

					} catch (IOException e) {
						continue;
					}
					Game g = new Game(p1,p2);
					Thread t = new Thread(g);
					t.start();
					games.add(new GameHolder(g,t));	
				
				}
				*/
				
				while(true) {
					try {
						ClientHolder w = new ClientHolder(join.accept());
						waiting.add(w);
						w.os.writeUTF("The Current Games To Join Are: "+queue.keySet()+". To Create a new game just write whatever you like as the name or join an existing one");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
	}
	@Override
	public void actionPerformed(ActionEvent e) {

			for(ClientHolder h : queue.values()) {
				try {
					h.os.writeUTF("$");
				} catch (IOException e1) {
					e1.printStackTrace();
					System.out.println("Failed ping :(");
					toRemove.add(h);
				}
			}
			for(int i = 0; i < waiting.size();i++) {
			try {
				waiting.get(i).os.writeUTF("$");
			} catch (IOException e1) {
				toRemove.add(waiting.get(i));
			}
			}
			for(int i = 0; i < toRemove.size();i++) {
				if(!waiting.remove(toRemove.get(i))) {
					for(int j = 0; j < queue.keySet().size();j++) {
						System.out.println("Trying to remove with key: "+((String) queue.keySet().toArray()[j]));
						if(queue.get(((String) queue.keySet().toArray()[j]))==toRemove.get(i)){
							System.out.println("Active remove");
							queue.remove(((String) queue.keySet().toArray()[j]));
							break;
						}
					}
				}
			}
			for(int i = 0; i < games.size(); i++) {
				if(games.get(i).g.live == false) {
					games.remove(i);
				}
			}
		for(int i = 0; i < waiting.size();i++) {
			String game = "";
			try {
				
				if(waiting.get(i).is.available()>0) {
					
					if(waiting.get(i).name!=null) {
					game = waiting.get(i).is.readUTF().replaceAll("\\$", "");
					game = game.replaceAll("$", "");
					if(!game.equals("")) {
					if(queue.get(game) == null) {
						queue.put(game, waiting.get(i));
						waiting.get(i).os.writeUTF("wait");
					} else {
						Game g = new Game(queue.get(game).s,waiting.get(i).s,game,queue.get(game).name,waiting.get(i).name);
						Thread t = new Thread(g);
						t.start();
						games.add(new GameHolder(g,t));
						queue.get(game).os.writeUTF("start");
						waiting.get(i).os.writeUTF("start");
						toRemove.add(waiting.get(i));
						toRemove.add(queue.get(game));
					}
				} else if (waiting.get(i).is.available() == -1) {
					toRemove.add(waiting.get(i));
					waiting.get(i).s.close();
					queue.remove(game);
				}
					} else {
						String name = waiting.get(i).is.readUTF().replaceAll("\\$", "");
						name = name.replaceAll("$", "");					
						waiting.get(i).name = name;
					}
				}
			} catch (IOException e1) {
				toRemove.add(waiting.get(i));
				queue.remove(game);
				try {
					waiting.get(i).s.close();
				} catch (IOException e2) {
					System.out.println("Edge Case nearly impossible");
					System.exit(0);
				}
			}
		}
		
		System.out.println("Games: " + games);
		System.out.println("Queue: " + queue);
		System.out.println("Waiting: " + waiting);
		String playerst = "<br>";
		String queuet = "<br>";
		String gamest = "<br>";
		for(ClientHolder h : waiting) {	
			playerst += h.name+"<br>";
		
		}
		for(String h : queue.keySet()) {	
			queuet += h+"("+queue.get(h).name+")<br>";
		}
		for(GameHolder h : games) {	
			gamest += h.g.name+"("+h.g.p1name +" vs "+h.g.p2name+") <br>";
		}
		
		l.setText(makeLabel(playerst,queuet,gamest));
		f.pack();
		
	}
	String makeLabel(String players, String queue, String games) {
		return "<html>" + "The non-playing  players are: " + players+ "<br>"+"The games in queue are: " + queue+ "<br>"+ "The current active games are: "+games;			
	}
}
