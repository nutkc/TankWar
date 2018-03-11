
public class Bullet {
	int x ,y ,dir;
	Bullet(int x ,int y ,int dir) {
		this.dir = dir;
		switch(dir) {
		case 0:
			this.x = x + 20;
			this.y = y - 10;
			break;
		case 1:
			this.x = x + 20;
			this.y = y + 50;
			break;
		case 2:
			this.x = x - 10;
			this.y = y + 20;
			break;
		case 3:
			this.x = x + 50;
			this.y = y + 20;
			break;
		}
	}

	public void move() {
		switch(dir) {
			case 0:
				y -= 4;
				break;
			case 1:
				y += 4;
				break;
			case 2:
				x -= 4;
				break;
			case 3:
				x += 4;
				break;
		}
	}
}
