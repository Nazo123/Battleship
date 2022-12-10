package Game;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;

public class Client implements ActionListener, WindowListener, KeyListener {
	ArrayList<BattleShip> ships = new ArrayList<BattleShip>();
	boolean inQueue;
	boolean setUp;
	boolean waiting;
	Socket w;
	DataInputStream is;
	DataOutputStream os;
	Timer t;
	Timer ping;
	String name;
	String ip;
	JFrame f;
	DisplayPanel p;
	JFrame chat;
	JTextArea chatdis;
	JScrollPane s;
	JTextField text;
	JButton send;
	JPanel format;
	JPanel holder;
	String gameName;
	String opponent;
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
		ships.add(new BattleShip(2, "Destroyer"));
		ships.add(new BattleShip(3, "Cruiser"));
		ships.add(new BattleShip(3, "Submarine"));
		ships.add(new BattleShip(4, "Battleship"));
		ships.add(new BattleShip(5, "Carrier"));
		inQueue = true;
		setUp = false;
		f = new JFrame();
		p = new DisplayPanel(ships);
		f.setMinimumSize(new Dimension(800,620));
		f.add(p);
		p.setMinimumSize(new Dimension(800,600));
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chat = new JFrame();
		chat.setMinimumSize(new Dimension(300,600));
		chatdis = new JTextArea();
		chatdis.setMinimumSize(new Dimension(300,500));
		chatdis.setMaximumSize(new Dimension(300,500));
		chatdis.setSize(new Dimension(300,500));
		chatdis.setEditable(false);
		chatdis.setVisible(true);
		chatdis.setLineWrap(true);
		chatdis.setWrapStyleWord(true);
		s = new JScrollPane(chatdis);
		s.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		s.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		s.setMinimumSize(new Dimension(300,500));
		s.setSize(new Dimension(300,500));
		s.setMaximumSize(new Dimension(300,500));
		send = new JButton("Send");
		text = new JTextField();
		text.setSize(250,30);
		text.setMaximumSize(new Dimension(250,30));
		text.setText("Gaming");
		text.addKeyListener(this);
		format = new JPanel();
		holder = new JPanel();
		chat.addWindowListener(this);
		chat.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		format.add(s);
		holder.setLayout(new BoxLayout(holder, BoxLayout.X_AXIS));
		holder.add(text);
		holder.add(send);
		format.setLayout(new BoxLayout(format, BoxLayout.Y_AXIS));
		format.add(s);
		format.add(holder);
		chat.add(format);
		chat.setMaximumSize(new Dimension(300,600));
		send.addActionListener(this);
		
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
		if(e.getSource()==send) {
			if(!text.getText().equals("")) {
			chatdis.append("\n You said: "+text.getText() + "\n");
			s.getVerticalScrollBar().setValue(s.getVerticalScrollBar().getHeight());
			try {
				os.writeUTF("msg:"+text.getText());
			} catch (IOException e1) {
				quit();
			}
			text.setText("");
			}
		return;
		}
		if(p.chat != chat.isVisible()) {
			chat.setSize(new Dimension(300,600));
			chat.setVisible(p.chat);
		}
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
		return;
		}
		if(inQueue) {
		try {
			if(is.available()>0) {
				String msg = is.readUTF().replaceAll("\\$", "");
				if(msg.startsWith("The Cur")) {
				gameName = JOptionPane.showInputDialog(msg);
				os.writeUTF(gameName);
				inQueue = true;
				f.setTitle("Waiting for players to join: "+gameName);
				f.setVisible(true);
				} else if(msg.startsWith("start:")){
					inQueue = false;
					p.inQueue = false;
					p.setUp = true;
					setUp = true;
					opponent = msg.substring(6);
					System.out.println(opponent);
					chat.setTitle("You vs "+opponent);
					f.setTitle("You are currently setting up your ships. Click the button to enter");
				}
			}
		} catch (IOException e1) {
			quit();
		}
	} else {
		try {
			if(is.available()>0) {
				String msg = is.readUTF().replaceAll("\\$", "");
				if(msg.startsWith("msg:")) {
					chatdis.append("\n "+opponent +" said: "+msg.substring(4) + "\n");
				}
			}
		} catch (IOException e1) {
			quit();
		}

	}
		if(setUp) {
		if(!p.setUp) {
			setUp = false;
			waiting = true;
			f.setTitle("Your ships are setup, waiting for opponent");
			try {
				os.writeUTF("Waiting");
			} catch (IOException e1) {
				quit();
			}
			return;
		}
		boolean f = true;
		for(int i = 0; i < ships.size();i++) {
		f = ships.get(i).placed;
		if(!f) {
			break;
		}
		} 
		p.ready = f;
		}
		
	}
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosing(WindowEvent e) {
		p.chat = false;
		chat.setVisible(false);
	}
	@Override
	public void windowClosed(WindowEvent e) {
		p.chat = false;
		chat.setVisible(false);
	}
	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyChar()==10 && e.getSource() == text) {
			if(!text.getText().equals("")) {
				chatdis.append("\n You said: "+text.getText() + "\n");
				try {
					os.writeUTF("msg:"+text.getText());
				} catch (IOException e1) {
					quit();
				}
				text.setText("");
				}
		}
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
