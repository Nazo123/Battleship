package Game;

public class BattleShip {
	public boolean horizontal = true;
	public boolean wasHorizontal = true;
	boolean held = false;
	public String type;
	public int length;
	public boolean placed = false;
	public boolean dead = false;
	public int x = -1;
	public int y = -1;
	public BattleShip(String type) {
		switch (type) {
		case "Destroyer":
			length = 2;
			break;
		case "Cruiser":
			length = 3;
			break;
		case "Submarine":
			length = 3;
			break;
		case "Battleship":
			length = 4;
			break;
		case "Carrier":
			length = 5;
			break;
		}
		this.type = type;
	}
}
