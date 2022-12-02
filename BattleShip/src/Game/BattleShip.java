package Game;

public class BattleShip {
	public boolean horizontal = true;
	public int length;
	public boolean placed = false;
	public boolean dead = false;
	public int x = -1;
	public int y = -1;
	public BattleShip(int length) {
		this.length = length;
	}
}
