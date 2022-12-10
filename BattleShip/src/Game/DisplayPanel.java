package Game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
public class DisplayPanel extends JPanel implements MouseListener, KeyListener{
	boolean inQueue = true;
	boolean setUp = false;
	boolean chat = false;
	boolean ready = false;
	float ratio;
	public float h = 600;
	public float w = 800;
	ArrayList<BattleShip> ships;
	Tile[][] board = new Tile[10][10];
	Font f;
	public DisplayPanel(ArrayList<BattleShip> ships) {
		this.ships = ships;
		addMouseListener(this);
		addKeyListener(this);
		setFocusable(true);
		for(int i = 0; i < board.length;i++) {
			for(int j = 0; j < board[i].length;j++) {
				board[i][j] = new Tile();
			}
		}
		f = new Font("Courier", Font.PLAIN,15);
	}

	public void paint(Graphics g) {
		Point p = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(p,this);
		if(w/800<h/600) {
		ratio = w/800;
		} else {
		ratio = h/600;
		}
		Dimension d = new Dimension();
		d.setSize(ratio*800, ratio*600);
		setSize(d);
		g.setColor(Color.BLUE);
		g.fillRect((int)(10*ratio),(int)(10*ratio),(int)(500*ratio),(int)(500*ratio));
		g.setColor(Color.black);
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[i].length; j++) {
			if(!setUp&&!inQueue) {
			if(!board[i][j].containsShip && p.x > (int)((10+i*50)*ratio) && p.x < (int)((10+(i+1)*50)*ratio) && p.y > (int)((10+j*50)*ratio) && p.y < (int)((10+(j+1)*50)*ratio)) {
				g.setColor(Color.GRAY);
				g.fillOval((int)((25+i*50)*ratio),((int)((25+j*50)*ratio)),(int)(20*ratio),(int)(20*ratio));
				g.setColor(Color.black);
			}
			}
			g.setColor(Color.gray);
			if(board[i][j].containsShip) {
				g.setColor(Color.red);
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
		for(int i = 0; i < ships.size();i++) {
			if(ships.get(i).held) {
				g.setColor(Color.gray);
				if(ships.get(i).horizontal) {
				g.fillRect((int)(p.getX()-0.5*ships.get(i).length*50*ratio),(int)(p.getY()-25*ratio),(int)(ships.get(i).length*50*ratio),(int)(50*ratio));
				g.setColor(Color.black);
				g.drawRect((int)(p.getX()-0.5*ships.get(i).length*50*ratio),(int)(p.getY()-25*ratio),(int)(ships.get(i).length*50*ratio),(int)(50*ratio));
				} else {
				g.fillRect((int)(p.getX()-25*ratio),(int)(p.getY()-0.5*ships.get(i).length*50*ratio),(int)(50*ratio),(int)(ships.get(i).length*50*ratio));
				g.setColor(Color.black);
				g.drawRect((int)(p.getX()-25*ratio),(int)(p.getY()-0.5*ships.get(i).length*50*ratio),(int)(50*ratio),(int)(ships.get(i).length*50*ratio));
				}
				continue;
				}
			if(!ships.get(i).placed) {
				ships.get(i).horizontal = true;
				ships.get(i).x = (int)((525)*ratio);
				ships.get(i).y = (int)((10+i*100)*ratio);
				g.setColor(Color.gray);
				g.fillRect(ships.get(i).x,ships.get(i).y,(int)(ships.get(i).length*50*ratio),(int)(50*ratio));
				g.setColor(Color.black);
				g.drawRect(ships.get(i).x,ships.get(i).y,(int)(ships.get(i).length*50*ratio),(int)(50*ratio));
			} else {
				g.setColor(Color.gray);
				if(ships.get(i).horizontal) {
					g.fillRect((int)(ratio*ships.get(i).x),(int)(ratio*ships.get(i).y),(int)(ships.get(i).length*50*ratio),(int)(50*ratio));
					g.setColor(Color.black);
					g.drawRect((int)(ratio*ships.get(i).x),(int)(ratio*ships.get(i).y),(int)(ships.get(i).length*50*ratio),(int)(50*ratio));
				}
				
			}
		}
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		if(!inQueue&&e.getX() > (int)(10*ratio) && e.getX() < (int)(80*ratio) && e.getY() > (int)(520*ratio) && e.getY() < (int)(550*ratio)) {
			chat = !chat;
		} else if(ready&&setUp&&e.getX() > (int)(100*ratio) && e.getX() < (int)(170*ratio) && e.getY() > (int)(520*ratio) && e.getY() < (int)(550*ratio)) {
			setUp = false;
		} else {
			for(int i = 0; i < ships.size(); i++) {
				/// WORK ON IT HERE
				if(ships.get(i).horizontal) {
				if(e.getX() > ships.get(i).x && e.getX() < (ships.get(i).x+(ships.get(i).length*ratio*50)) && e.getY() > ships.get(i).y && e.getY() < ships.get(i).y + ratio*50) {
					System.out.println("True");
					ships.get(i).wasHorizontal = ships.get(i).horizontal;
					ships.get(i).held = true;
				}
				} else {
					if(setUp && e.getX() > ships.get(i).x && e.getX() < (ratio*50) + ships.get(i).x && e.getY() > ships.get(i).y && e.getY() < (ships.get(i).y + ships.get(i).length*ratio*50)) {
						ships.get(i).wasHorizontal = ships.get(i).horizontal;
						ships.get(i).held = true;
					}
				}
				
			}
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		for(int i = 0; i < ships.size(); i++) {
			if(ships.get(i).held) {
				//Do the math here later for locking on
				if(ships.get(i).horizontal) {
					System.out.println("Horizontal");
					boolean legal = true;
					int x = (int) ((e.getX()-(ratio*50*ships.get(i).length/2)));
					System.out.println(e.getX());
					System.out.println("OMG X:" + x);
					System.out.println("Ship x is:" + x +"  Ship y is:" + ratio*e.getY());
					System.out.println("TileX:" + ((int)Math.floor((int)(x-10)/50)) +"  TileY:" + ((int)Math.floor((e.getY()-10)/50)));
					if(x > 0 && x < 520*ratio && e.getY() > 0 && e.getY() < 520*ratio) {
					for(int j = 0; j < ships.get(i).length; j++ ) {
					if((int) (Math.floor((((x-10)/50)))+j) < 10 
							&& j + (int)(Math.floor((int)(x-10)/50)) >-1 
							&& ((int)Math.floor(((e.getY()-10)/50))) > -1 
							&& ((int)Math.floor(((e.getY()-10)/50))) < 10
							&& !board[(int) (Math.floor(((int)((x-10)/50)))+j)][((int)Math.floor(((e.getY()-10)/50)))].containsShip) {
						
					legal = true;
					} else {
						legal = false;
						break;
					}
					}
					if(legal) {
						if(ships.get(i).placed) {
							for(int j = 0; j < ships.get(i).length;j++) {
								board[(int)Math.floor((ships.get(i).x-10)/50)+j][(int)Math.floor((ships.get(i).y-10)/50)].containsShip = false;
							}
						}
						ships.get(i).x = (int) (10 + 50*(Math.floor((x-10)/50)));
						ships.get(i).y = (int) (10 + 50*(Math.floor(e.getY()-10)/50));
						ships.get(i).wasHorizontal = true;
						ships.get(i).placed = true;
						for(int j = 0; j < ships.get(i).length;j++) {
							board[(int)Math.floor((ships.get(i).x-10)/50)+j][(int)Math.floor((ships.get(i).y-10)/50)].containsShip = true;
						}
					}
					ships.get(i).held = false;
					return;
					}
				} else {
					
				}
				ships.get(i).held = false;
				ships.get(i).horizontal = ships.get(i).wasHorizontal;
			}
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println(e.getKeyChar());
		if(e.getKeyChar() == 'r') {
		for(int i = 0; i < ships.size();i++) {
			if(ships.get(i).held) {
				ships.get(i).horizontal = !ships.get(i).horizontal;
			}
		}
	} else if(e.getKeyChar() == 'c' && !inQueue){
		chat = !chat;
	}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
