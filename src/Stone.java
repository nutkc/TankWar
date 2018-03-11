public class Stone extends Block { 

	public Stone(int x, int y) {
		super(x, y);
		passable = false;
		shootable = false;
		breakable = false;
		life = 100;
	}
	
	public void attacked() {

	}
}