
public class Explode {
	public int x,y,num = 0,paintNum = 0;
	public boolean exist = false;
	Explode(int x,int y) {
		this.x = x;
		this.y = y;
		exist = true;
	}
	public void next() {
		num++;
		if(num>5) {
			exist = false;
		}
	}
}
