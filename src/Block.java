
 public abstract class Block {
	public int x,y,life;
	public boolean alive = true;
	public boolean passable;
	public boolean shootable;
	public boolean breakable;

	public Block(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public abstract void attacked();
}
