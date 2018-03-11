import java.util.ArrayList;
import java.util.Iterator;

public class Foe {
	public boolean alive = true;
	public int x ,y ,dir ,moveCount = 0 ,shootCount = 0 , life = 100;
	private Player player = null;
	private int setTimes = 0;
	static int speed = 2;
	static int sType = 4;
	boolean setSuccess = true;
	ArrayList<Foe> foes =null;
	ArrayList<Block> blocks=null;
	Foe(ArrayList<Foe> foes ,ArrayList<Block> blocks,Player player) {
		//��Ļ����Ϸ����λ��Ϊ(6,28)��(906,748) ͬʱ��Ҫע��̹�����50*50 ��x���ȡ��856
		//ͨ��ʹ��Math.random()����ʵ��̹�����ˢ��
		this.foes = foes;
		this.blocks = blocks;
		this.player = player;
		setFoe();
	}
	
	public void setFoe() {
		dir = (int)(Math.random() * 4);
		int setX = (int)(Math.random() * 850) + 6;
		int setY = (int)(Math.random() * 670) +28;
		while((!moveable(setX,setY)) || (!setable(setX,setY))) {
			if(setTimes < 100000) {
				setX = (int)(Math.random() * 850) + 6;
				setY = (int)(Math.random() * 670) +28;
				setTimes++;
			} else {
				setTimes = 0;
				setSuccess = false;
				break;
			}
		}
		x = setX;
		y = setY;
		moveCount = (int)(Math.random() * 150);
	}
	
	//�ж��Ƿ񽫵��˳�ʼ��������ҵ�����
	public boolean setable(int nextX , int nextY) {
		if(player.x<nextX+100 &&
				player.x+100>nextX &&
				player.y<nextY+100 &&
				player.y+100>nextY) {
			return false;
		}
		return true;
	}
	
	
	
 	public void move() {
		if (moveCount < 150) {
			switch(dir) {
			case 0:
				if(moveable(x,y-speed)) {
					if(moveCount%sType == 0) {
						y -= speed;
					}
				} else {
					moveCount = 149;
				}
				moveCount++;
				break;
			case 1:
				if(moveable(x,y+speed)) {
					if(moveCount%sType == 0) {
						y += speed;
					}
				} else {
					moveCount = 149;
				}
				moveCount++;
				break;
			case 2:
				if(moveable(x-speed,y)) {
					if(moveCount%sType == 0) {
						x -= speed;
					}
				} else {
					moveCount = 149;
				}
				moveCount++;
				break;
			case 3:
				if(moveable(x+speed,y)) {
					if(moveCount%sType == 0) {
						x += speed;
					}
				} else {
					moveCount = 149;
				}
				moveCount++;
				break;
			}
		} else {
			int nextX = x;
			int nextY = y;
			do {
				dir = (int)(Math.random() * 4);
				switch(dir) {
				case 0:
					nextX = x;
					nextY = y-1;
					break;
				case 1:
					nextX = x;
					nextY = y+1;
					break;
				case 2:
					nextX = x-1;
					nextY = y;
					break;
				case 3:
					nextX = x+1;
					nextY = y;
					break;
				}
			} while(!this.moveable(nextX, nextY));
			moveCount = 0;
			shootCount++;
		}
	
	}
	
	public void attacked() {
		life -= 20;
		if(life<=0) {
			alive = false;
		}
	}
	public Bullet shoot() {
		return new Bullet(x,y,dir);
	}
	//�ж��Ƿ����õ��ƶ�
	public boolean moveable(int nextX ,int nextY) {
		//�ж�Ҫ�ƶ��ĵ��Ƿ�����Ϸ������
		if(nextX<6 || nextX>856 || nextY<28 || nextY>698) {
			return false;
		}
		//�ж��Ƿ���̹��
		if(foes.size() != 0) {
			Iterator<Foe> i = foes.iterator();
			while(i.hasNext()) {
				Foe thisFoe = i.next();
				if(thisFoe == this) {
					continue;
				}
				if(thisFoe.x<nextX+60 &&
						thisFoe.x+60>nextX &&
						thisFoe.y<nextY+60 &&
						thisFoe.y+60>nextY) {
					return false;
				}
			}
		}
		//�ж��Ƿ����ϰ���
		if(blocks.size() != 0) {
			Iterator<Block> i = blocks.iterator();
			while(i.hasNext()) {
				Block thisBlock = i.next();
				if(!thisBlock.passable) {
					if(thisBlock.x<nextX+50 &&
							thisBlock.x+36>nextX &&
							thisBlock.y<nextY+50 &&
							thisBlock.y+36>nextY) {
						return false;
					}
				}
			}
		}
		//���ϰ��ﷵ��true
		return true;
	}
}
