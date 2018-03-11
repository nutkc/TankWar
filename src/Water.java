public class Water extends Block { 

	public Water(int x, int y) {
		super(x, y);
		passable = false;
		shootable = true;
		breakable = false;
		life = 100;
	}
	
	public void attacked() {

	}
}