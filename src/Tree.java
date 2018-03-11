
public class Tree extends Block { 

	public Tree(int x, int y) {
		super(x, y);
		passable = true;
		shootable = true;
		breakable = false;
		life = 100;
	}
	
	public void attacked() {

	}
}

