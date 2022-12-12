package Game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
	boolean turn = false;
	boolean inGame = false;
	float ratio;
	public float h = 600;
	public float w = 800;
	ArrayList<BattleShip> ships;
	ArrayList<BattleShip> sunk;
	ArrayList<ArrayList<Tile>> board;
	ArrayList<String> guesses;
	ArrayList<String> hits;

	Font f;
	Font f2;
	String fire;
	public DisplayPanel(ArrayList<BattleShip> ships, ArrayList<ArrayList<Tile>> board, ArrayList<String> guesses, ArrayList<String> hits, ArrayList<BattleShip> sunk) {
		fire = null;
		setBackground(new Color(174, 175, 176));
		this.sunk = sunk;
		this.ships = ships;
		this.board = board;
		this.guesses = guesses;
		this.hits = hits;
		addMouseListener(this);
		addKeyListener(this);
		setFocusable(true);
		for(int i = 0; i < 10;i++) {
			board.add(new ArrayList<Tile>());
			for(int j = 0; j < 10;j++) {
			board.get(i).add(new Tile());
			}
		}
		f = new Font("Courier", Font.PLAIN,15);
		f2 = new Font("Courier", Font.PLAIN,50);
	}
	public void paint(Graphics g) {
		Point p = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(p,this);
		if(w/800<h/600) {
		ratio = w/800;
		} else {
		ratio = h/600;
		}
		f2 = new Font("Courier", Font.PLAIN,(int)(50*ratio));
		f = new Font("Courier", Font.PLAIN,(int)(15*ratio));
		Dimension d = new Dimension();
		d.setSize(ratio*800, ratio*600);
		setSize(d);
		g.setColor(Color.BLUE);
		g.fillRect((int)(10*ratio),(int)(10*ratio),(int)(500*ratio),(int)(500*ratio));
		g.setColor(Color.black);
			for(int i = 0; i < board.size(); i++) {
			for(int j = 0; j < board.get(i).size(); j++) {
			
			g.setColor(Color.gray);
			g.drawRect((int)((10+i*50)*ratio),((int)((10+j*50)*ratio)),(int)(50*ratio),(int)(50*ratio));
			}
			if(turn) {
				int xTile = (int)(p.x/ratio-10)/50;
				int yTile = (int)(p.y/ratio-10)/50;
				if(xTile > -1 && xTile < 10 && yTile > -1 && yTile < 10) {	
					boolean draw = true;
					
					for(int m = 0; m < guesses.size();m++) {
						if((xTile+","+yTile).contentEquals(guesses.get(m))) {
							draw = false;
							break;
					}
					}
					if(draw) {
					for(int m = 0; m < hits.size();m++) {
						if((xTile+","+yTile).contentEquals(guesses.get(m))) {
							draw = false;
							break;
					}
					}
					}
					if(draw) {
					g.setColor(Color.GRAY);
					g.fillOval((int)((25+xTile*50)*ratio),((int)((25+yTile*50)*ratio)),(int)(20*ratio),(int)(20*ratio));
					g.setColor(Color.black);
					}
				}
				}
		}
		
		if(inGame) {
			g.setFont(f2);
			if(turn) {
				g.setColor(Color.black);
				g.drawString("Your turn", (int)(100*ratio),(int)(550*ratio));
			} else {
				g.setColor(Color.gray);
				g.drawString("Opponent's turn", (int)(100*ratio),(int)(550*ratio));
			}
			g.setColor(Color.blue);
			g.fillRect((int)(540*ratio),(int)(50*ratio), (int)(200*ratio), (int)(200*ratio));
			g.setColor(Color.gray);
			for(int i = 0; i < board.size(); i++) {
				for(int j = 0; j < board.get(i).size(); j++) {
					g.drawRect((int)((540+i*20)*ratio),((int)((50+j*20)*ratio)),(int)(20*ratio),(int)(20*ratio));
				}
			}
			for(int i = 0; i < ships.size();i++) {
				g.setColor(Color.gray);
				if(ships.get(i).horizontal) {
					g.fillRect((int)(ratio*((ships.get(i).x-10)/50*20 + 540)),(int)(ratio*((ships.get(i).y-10)/50*20+50)),(int)(ships.get(i).length*20*ratio),(int)(20*ratio));
					g.setColor(Color.black);
					g.drawRect((int)(ratio*((ships.get(i).x-10)/50*20 + 540)),(int)(ratio*((ships.get(i).y-10)/50*20+50)),(int)(ships.get(i).length*20*ratio),(int)(20*ratio));
				} else {
					g.fillRect((int)(ratio*((ships.get(i).x-10)/50*20 + 540)),(int)(ratio*((ships.get(i).y-10)/50*20+50)),(int)(20*ratio),(int)(ships.get(i).length*20*ratio));
					g.setColor(Color.black);
					g.drawRect((int)(ratio*((ships.get(i).x-10)/50*20 + 540)),(int)(ratio*((ships.get(i).y-10)/50*20+50)),(int)(20*ratio),(int)(ships.get(i).length*20*ratio));
				}
			}
			for(int i = 0; i < sunk.size();i++) {
				g.setColor(Color.gray);
				if(sunk.get(i).horizontal) {
					g.fillRect((int)(ratio*sunk.get(i).x),(int)(ratio*sunk.get(i).y),(int)(sunk.get(i).length*50*ratio),(int)(50*ratio));
					g.setColor(Color.black);
					g.drawRect((int)(ratio*sunk.get(i).x),(int)(ratio*sunk.get(i).y),(int)(sunk.get(i).length*50*ratio),(int)(50*ratio));
				} else {
					g.fillRect((int)(ratio*sunk.get(i).x),(int)(ratio*sunk.get(i).y),(int)(50*ratio),(int)(sunk.get(i).length*50*ratio));
					g.setColor(Color.black);
					g.drawRect((int)(ratio*sunk.get(i).x),(int)(ratio*sunk.get(i).y),(int)(50*ratio),(int)(sunk.get(i).length*50*ratio));
				}
			}

			  //Found this on Stackoverflow
			  Graphics2D g2 = (Graphics2D) g;
			  g2.setStroke(new BasicStroke(2));
		
			  
			for(int i = 0; i < board.size(); i++) {
				for(int j = 0; j < board.get(i).size(); j++) {
					if(board.get(i).get(j).hit) {
						g.setColor(Color.gray);
					if(board.get(i).get(j).containsShip) {
						g.setColor(Color.red);
					}
					g2.drawLine((int)(ratio*(545+i*20)),(int)(ratio*(55+j*20)),(int)(ratio*(535+(i+1)*20)),(int)(ratio*(45+(j+1)*20)));
					g2.drawLine((int)(ratio*(535+(i+1)*20)),(int)(ratio*(55+j*20)),(int)(ratio*(545+i*20)),(int)(ratio*(45+(j+1)*20)));

					}
				}
			}
			g2.setStroke(new BasicStroke(5));
			g2.setColor(Color.gray);
			for(int i = 0; i < guesses.size();i++) {
				int xTile = Integer.parseInt(guesses.get(i).substring(0,1));
				int yTile = Integer.parseInt(guesses.get(i).substring(2,3));
				g2.drawLine((int)(ratio*(xTile*50+22.5)), (int)(ratio*(yTile*50+22.5)), (int)(ratio*((xTile+1)*50-2.5)), (int)(ratio*((yTile+1)*50-2.5)));
				g2.drawLine((int)(ratio*((xTile+1)*50-2.5)), (int)(ratio*(yTile*50+22.5)), (int)(ratio*(xTile*50+22.5)), (int)(ratio*((yTile+1)*50-2.5)));
			}
			g2.setColor(Color.red);
			for(int i = 0; i < hits.size();i++) {
				int xTile = Integer.parseInt(hits.get(i).substring(0,1));
				int yTile = Integer.parseInt(hits.get(i).substring(2,3));
				g2.drawLine((int)(ratio*(xTile*50+22.5)), (int)(ratio*(yTile*50+22.5)), (int)(ratio*((xTile+1)*50-2.5)), (int)(ratio*((yTile+1)*50-2.5)));
				g2.drawLine((int)(ratio*((xTile+1)*50-2.5)), (int)(ratio*(yTile*50+22.5)), (int)(ratio*(xTile*50+22.5)), (int)(ratio*((yTile+1)*50-2.5)));
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
			if(!inGame) {
			if(!ships.get(i).placed) {
				ships.get(i).horizontal = true;
				ships.get(i).x = 525;
				ships.get(i).y = 10+i*100;
				g.setColor(Color.gray);
				g.fillRect((int)(ratio*ships.get(i).x),(int)(ratio*ships.get(i).y),(int)(ships.get(i).length*50*ratio),(int)(50*ratio));
				g.setColor(Color.black);
				g.drawRect((int)(ratio*ships.get(i).x),(int)(ratio*ships.get(i).y),(int)(ships.get(i).length*50*ratio),(int)(50*ratio));
			} else {
				g.setColor(Color.gray);
				if(ships.get(i).horizontal) {
					g.fillRect((int)(ratio*ships.get(i).x),(int)(ratio*ships.get(i).y),(int)(ships.get(i).length*50*ratio),(int)(50*ratio));
					g.setColor(Color.black);
					g.drawRect((int)(ratio*ships.get(i).x),(int)(ratio*ships.get(i).y),(int)(ships.get(i).length*50*ratio),(int)(50*ratio));
				} else {
					g.fillRect((int)(ratio*ships.get(i).x),(int)(ratio*ships.get(i).y),(int)(50*ratio),(int)(ships.get(i).length*50*ratio));
					g.setColor(Color.black);
					g.drawRect((int)(ratio*ships.get(i).x),(int)(ratio*ships.get(i).y),(int)(50*ratio),(int)(ships.get(i).length*50*ratio));
				}
				
			}
			}
			}
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		if(turn) {
			int xTile = ((int)Math.floor((int)(e.getX()/ratio-10)/50));
			int yTile = ((int)Math.floor((e.getY()/ratio-10)/50));
			if(xTile>-1&&xTile<10&&yTile>-1&&yTile<10) {
			fire = xTile+","+yTile;
			}
		}
		if(!inQueue&&e.getX() > 10*ratio && e.getX() < 80*ratio && e.getY() > 520*ratio && e.getY() < 550*ratio) {
			chat = !chat;
		} else if(ready&&setUp&&e.getX() > 100*ratio && e.getX() < 170*ratio && e.getY() > 520*ratio && e.getY() < 550*ratio) {
			setUp = false;
		} else if(setUp){
			for(int i = 0; i < ships.size(); i++) {
				if(ships.get(i).horizontal) {
				if(e.getX() > ships.get(i).x*ratio && e.getX() < (ships.get(i).x*ratio+(ships.get(i).length*ratio*50)) && e.getY() > ships.get(i).y*ratio && e.getY() < ships.get(i).y*ratio + ratio*50) {
					ships.get(i).wasHorizontal = ships.get(i).horizontal;
					ships.get(i).held = true;
					if(ships.get(i).placed) {
						for(int j = 0; j < ships.get(i).length;j++) {
							board.get((int)Math.floor((ships.get(i).x-10)/50)+j).get((int)Math.floor((ships.get(i).y-10)/50)).containsShip = false;
							board.get((int)Math.floor((ships.get(i).x-10)/50)+j).get((int)Math.floor((ships.get(i).y-10)/50)).ship = null;
						}
					}
				}
				} else {
					if(setUp && e.getX() > ships.get(i).x*ratio && e.getX() < (ratio*50) + ships.get(i).x*ratio && e.getY() > ships.get(i).y*ratio && e.getY() < (ships.get(i).y*ratio + ships.get(i).length*ratio*50)) {
						ships.get(i).wasHorizontal = ships.get(i).horizontal;
						ships.get(i).held = true;
						if(ships.get(i).placed) {
							for(int j = 0; j < ships.get(i).length;j++) {
								board.get((int)Math.floor((ships.get(i).x-10)/50)).get((int)Math.floor((ships.get(i).y-10)/50)+j).containsShip = false;
								board.get((int)Math.floor((ships.get(i).x-10)/50)).get((int)Math.floor((ships.get(i).y-10)/50)+j).ship = null;
							}
						}
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
				if(ships.get(i).horizontal) {
					boolean legal = true;
					int x = (int) ((e.getX()/ratio-(50*ships.get(i).length/2)));
					int xTile = ((int)Math.floor((int)(x-10)/50));
					int yTile = ((int)Math.floor((e.getY()/ratio-10)/50));
					if(x > 0 && x < 520 && e.getY() > 0 && e.getY()/ratio < 520) {
					for(int j = 0; j < ships.get(i).length; j++ ) {
					if((xTile+j) < 10 && (j + xTile) >-1 && yTile > -1 && (yTile) < 10 && !board.get(xTile+j).get(yTile).containsShip) {
					legal = true;
					} else {
						legal = false;
						break;
					}
					}
					if(legal) {
						ships.get(i).x = 10 + 50*xTile;
						ships.get(i).y = 10 + 50*yTile;
						ships.get(i).wasHorizontal = true;
						ships.get(i).placed = true;
						for(int j = 0; j < ships.get(i).length;j++) {
							board.get((int)Math.floor((ships.get(i).x-10)/50)+j).get((int)Math.floor((ships.get(i).y-10)/50)).containsShip = true;
							board.get((int)Math.floor((ships.get(i).x-10)/50)+j).get((int)Math.floor((ships.get(i).y-10)/50)).ship = ships.get(i);
						}
					}
					ships.get(i).held = false;
					ships.get(i).horizontal = ships.get(i).wasHorizontal;
					if(ships.get(i).placed) {
					if(ships.get(i).horizontal) {
						for(int j = 0; j < ships.get(i).length;j++) {
							board.get((int)Math.floor((ships.get(i).x-10)/50)+j).get((int)Math.floor((ships.get(i).y-10)/50)).containsShip = true;
							board.get((int)Math.floor((ships.get(i).x-10)/50)+j).get((int)Math.floor((ships.get(i).y-10)/50)).ship = ships.get(i);
						}
					} else {
						for(int j = 0; j < ships.get(i).length;j++) {
						board.get((int)Math.floor((ships.get(i).x-10)/50)).get((int)Math.floor((ships.get(i).y-10)/50)+j).containsShip = true;
						board.get((int)Math.floor((ships.get(i).x-10)/50)).get((int)Math.floor((ships.get(i).y-10)/50)+j).ship = ships.get(i);
						}
					}
					}
					return;
					}
				} else {
					boolean legal = true;
					int y = (int) ((e.getY()/ratio-(50*ships.get(i).length/2)));
					int xTile = ((int)Math.floor((e.getX()/ratio-10)/50));
					int yTile = ((int)Math.floor((int)(y-10)/50));
					if(y > 0 && y < 520 && e.getX() > 0 && e.getX()/ratio < 520) {
					for(int j = 0; j < ships.get(i).length; j++ ) {
					if((yTile+j) < 10 && (y + xTile) >-1 && xTile > -1 && xTile < 10 && !board.get(xTile).get(yTile+j).containsShip) {
					legal = true;
					} else {
						legal = false;
						break;
					}
					}
					if(legal) {
						ships.get(i).x = 10 + 50*xTile;
						ships.get(i).y = 10 + 50*yTile;
						ships.get(i).wasHorizontal = false;
						ships.get(i).placed = true;
						for(int j = 0; j < ships.get(i).length;j++) {
							board.get((int)Math.floor((ships.get(i).x-10)/50)).get((int)Math.floor((ships.get(i).y-10)/50)+j).containsShip = true;
							board.get((int)Math.floor((ships.get(i).x-10)/50)).get((int)Math.floor((ships.get(i).y-10)/50)+j).ship = ships.get(i);

						}
					}
					ships.get(i).horizontal = ships.get(i).wasHorizontal;
					ships.get(i).held = false;
					if(ships.get(i).placed) {
					if(ships.get(i).horizontal) {
						for(int j = 0; j < ships.get(i).length;j++) {
							board.get((int)Math.floor((ships.get(i).x-10)/50)+j).get((int)Math.floor((ships.get(i).y-10)/50)).containsShip = true;
							board.get((int)Math.floor((ships.get(i).x-10)/50)+j).get((int)Math.floor((ships.get(i).y-10)/50)).ship = ships.get(i);
						}
					} else {
						for(int j = 0; j < ships.get(i).length;j++) {
						board.get((int)Math.floor((ships.get(i).x-10)/50)).get((int)Math.floor((ships.get(i).y-10)/50)+j).containsShip = true;
						board.get((int)Math.floor((ships.get(i).x-10)/50)).get((int)Math.floor((ships.get(i).y-10)/50)+j).ship = ships.get(i);
						}
						}
					}
					return;
				}
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
