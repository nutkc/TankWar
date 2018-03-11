
public class Wall extends Block {

	public Wall(int x, int y) {
		super(x, y);
		passable = false;
		shootable = false;
		breakable = true;
		life = 100;
	}
	
	public void attacked() {
		life -= 40;
		if(life <= 0) {
			alive = false;
		}
	}
}
