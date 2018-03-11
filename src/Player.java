import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class Player {
	public URL shootSound = GameFrame.class.getClassLoader().getResource("sounds/shoot.wav");
	AudioClip shootClip = Applet.newAudioClip(shootSound);
	public boolean alive = true;
	public boolean up = false;
	public boolean down = false;
	public boolean left = false;
	public boolean right = false;
	public boolean exploded = false;
	public boolean killAll = false;
	public int x ,y ,dir ,shootCD=0 ,life = 1000 ,score = 0 ,speed = 1;
	public ArrayList<Block> blocks = null;
	Player(ArrayList<Block> blocks) {
		this.blocks = blocks;
	}
	
	//被攻击随机减去血量
	public void attacked() {
		life -= 100;
//		life -= 150 + (int)(Math.random()*300);
		if(life<=0) {
			life = 0;
			alive = false;
		}
	}
	
	
	@Override
	public String toString() {
		// TODO 自动生成的方法存根
		return "p"+","+x+","+y+","+dir+","+life+","+System.currentTimeMillis()+",";
	}
	
	//重新设定非广播端玩家位置血量等信息
		public void nReset() {
			killAll = false;
			exploded = false;
			alive = true;
			x = 431;
			y = 38;
			dir = 1;
			life = 1000;
			score = 0;
		}
	
	//重新设定位置血量等信息
	public void reset() {
		killAll = false;
		exploded = false;
		alive = true;
		x = 431;
		y = 688;
		dir = 0;
		life = 1000;
		score = 0;
	}
	
	//定义shoot方法在监听到射击时间时调用
	public Bullet shoot() {
		if(alive && !killAll) {
			shootClip.play();
			Bullet thisBullet = new Bullet(x,y,dir);
			GameFrame.sendMsg("b," + x + "," + y + "," + dir + ",");
			return thisBullet;
		}
		return new Bullet(0,0,0);		
	}
	
	//定义move方法在画面刷新时调用
	public void move() {

		if(up) {
			dir = 0;
			if( moveable(x,y-speed)) {
				y -= speed;
			}
			GameFrame.sendMsg(this.toString());
		}
		if(down) {
			dir = 1;
			if( moveable(x,y+speed)) {
				y += speed;
			}
			GameFrame.sendMsg(this.toString());
		}
		if(left) {
			dir = 2;
			if(moveable(x-speed,y)) {
				x -= speed;
			}
			GameFrame.sendMsg(this.toString());
		}
		if(right) {
			dir = 3;
			if(moveable(x+speed,y)) {
				x += speed;	
			}
			GameFrame.sendMsg(this.toString());
		}
	}
	
	private boolean moveable(int nextX ,int nextY) {
		try {
			for(Block thisBlock:blocks) {
				if(!thisBlock.passable) {
					if(thisBlock.x<nextX+50 &&
							thisBlock.x+36>nextX &&
							thisBlock.y<nextY+50 &&
							thisBlock.y+36>nextY) {
						return false;
					}
				}
			}
		} catch(Exception e) {
			
		}
		
		if(nextX<6 || nextX>856 || nextY<28 || nextY>698) {
			return false;
		}
		return true;
	}
}
