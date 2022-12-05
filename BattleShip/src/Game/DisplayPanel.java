package Game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
public class DisplayPanel extends JPanel implements MouseListener{
	boolean inQueue = true;
	boolean setUp = false;
	boolean chat = false;
	boolean ready = false;
	float ratio;
	public float h = 600;
	public float w = 600;
	Tile[][] board = new Tile[10][10];
	Font f;
	public DisplayPanel() {
		addMouseListener(this);
		for(int i = 0; i < board.length;i++) {
			for(int j = 0; j < board[i].length;j++) {
				board[i][j] = new Tile();
			}
		}
		f = new Font("Courier", Font.PLAIN,15);
	}
	public void paintComponent(Graphics g) {
		Point p = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(p,this);
		if(w<h) {
		ratio = w/600;
		} else {
		ratio = h/600;
		}
		Dimension d = new Dimension();
		d.setSize(ratio*600, ratio*600);
		setSize(d);
		g.setColor(Color.BLUE);
		g.fillRect((int)(10*ratio),(int)(10*ratio),(int)(500*ratio),(int)(500*ratio));
		g.setColor(Color.black);
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[i].length; j++) {
			if(!setUp) {
			if(!board[i][j].containsShip && p.x > (int)((10+i*50)*ratio) && p.x < (int)((10+(i+1)*50)*ratio) && p.y > (int)((10+j*50)*ratio) && p.y < (int)((10+(j+1)*50)*ratio)) {
				g.setColor(Color.GRAY);
				g.fillOval((int)((25+i*50)*ratio),((int)((25+j*50)*ratio)),(int)(20*ratio),(int)(20*ratio));
				g.setColor(Color.black);
			}
			}
			g.drawRect((int)((10+i*50)*ratio),((int)((10+j*50)*ratio)),(int)(50*ratio),(int)(50*ratio));
			}
		}
		if(!inQueue) {
			if(setUp) {
				g.setColor(Color.green);
				g.fillRect((int)(100*ratio), (int)(520*ratio), (int)(70*ratio), (int)(30*ratio));
				g.setColor(Color.black);
				g.setFont(f);
				g.drawString("Start", (int)(105*ratio), (int)(540*ratio));
			}
			g.setColor(Color.black);
		g.fillRect((int)(10*ratio), (int)(520*ratio), (int)(70*ratio), (int)(30*ratio));
		g.setColor(Color.white);
		g.setFont(f);
		g.drawString("Chat", (int)(15*ratio), (int)(540*ratio));
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		if(!inQueue&&e.getX() > (int)(10*ratio) && e.getX() < (int)(80*ratio) && e.getY() > (int)(520*ratio) && e.getY() < (int)(550*ratio)) {
			System.out.println("Chat!");
			chat = !chat;
		}
		if(ready&&setUp&&e.getX() > (int)(100*ratio) && e.getX() < (int)(170*ratio) && e.getY() > (int)(520*ratio) && e.getY() < (int)(550*ratio)) {
			setUp = false;
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
