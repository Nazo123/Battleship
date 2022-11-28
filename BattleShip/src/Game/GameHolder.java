package Game;

public class GameHolder {
	public Thread t;
	public Game g;
	
	public GameHolder(Game g,Thread t) {
		this.g = g;
		this.t = t;
	}
}
