import java.util.ArrayList;

public class GameMap {
	private ArrayList<Block> blocks = null;
	GameMap(ArrayList<Block> blocks ) {
		this.blocks = blocks;
	}
	
	public void setMap(int index) {
		blocks.clear();
		switch(index) {
		case 0:
			selMap0();
			break;
		case 1:
			selMap1();
			break;
		}
	}
	
	
	private void selMap1() {
		blockSet(0,3,25,5,1);
		blockSet(0,8,8,3,0);
		blockSet(8,8,9,3,2);
		blockSet(17,8,8,3,3);
		blockSet(0,11,25,7,1);
	}
	private boolean blockSet(int x,int y,int width, int height,int type) {
		if(type<0 || type>3) {
			return false;
		}
		switch(type) {
		case 0:
			for(int i=0; i<width; i++) {
				for(int j=0; j<height; j++) {
					blocks.add(new Tree(6+36*x+36*i,28+36*y+36*j));
				}
			}
			break;
		case 1:
			for(int i=0; i<width; i++) {
				for(int j=0; j<height; j++) {
					blocks.add(new Water(6+36*x+36*i,28+36*y+36*j));
				}
			}
			break;
		case 2:
			for(int i=0; i<width; i++) {
				for(int j=0; j<height; j++) {
					blocks.add(new Wall(6+36*x+36*i,28+36*y+36*j));
				}
			}
			break;
		case 3:
			for(int i=0; i<width; i++) {
				for(int j=0; j<height; j++) {
					blocks.add(new Stone(6+36*x+36*i,28+36*y+36*j));
				}
			}
			break;
		}
		return true;
	}
	private void selMap0() {
		//绘制水
		for(int j=0; j<20; j++) {
			blocks.add(new Water(870,28+36*j));
		}
		for(int i=0; i<4; i++) {
			for(int j=0; j<2; j++) {
				blocks.add(new Water(6+36*i,100+36*j));
			}
		}
		for(int i=0; i<2; i++) {
			for(int j=0; j<4; j++) {
				blocks.add(new Water(222+36*i,28+36*j));
			}
		}
		for(int i=0; i<6; i++) {
			for(int j=0; j<2; j++) {
				blocks.add(new Water(366+36*i,100+36*j));
			}
		}
		for(int i=0; i<2; i++) {
			for(int j=0; j<6; j++) {
				blocks.add(new Water(582+36*i,28+36*j));
			}
		}
		for(int i=0; i<4; i++) {
			for(int j=0; j<2; j++) {
				blocks.add(new Water(726+36*i,100+36*j));
			}
		}
		for(int i=0; i<2; i++) {
			for(int j=0; j<2; j++) {
				blocks.add(new Water(726+36*i,172+36*j));
			}
		}
		for(int i=0; i<8; i++) {
			for(int j=0; j<2; j++) {
				blocks.add(new Water(78+36*i,244+36*j));
			}
		}
		for(int i=0; i<8; i++) {
			for(int j=0; j<2; j++) {
				blocks.add(new Water(582+36*i,316+36*j));
			}
		}
		for(int i=0; i<2; i++) {
			for(int j=0; j<4; j++) {
				blocks.add(new Water(222+36*i,316+36*j));
			}
		}
		for(int i=0; i<2; i++) {
			for(int j=0; j<4; j++) {
				blocks.add(new Water(438+36*i,316+36*j));
			}
		}
		for(int i=0; i<2; i++) {
			for(int j=0; j<4; j++) {
				blocks.add(new Water(654+36*i,388+36*j));
			}
		}
		for(int i=0; i<4; i++) {
			for(int j=0; j<2; j++) {
				blocks.add(new Water(6+36*i,388+36*j));
			}
		}
		for(int i=0; i<2; i++) {
			for(int j=0; j<2; j++) {
				blocks.add(new Water(366+36*i,388+36*j));
			}
		}
		for(int i=0; i<4; i++) {
			for(int j=0; j<2; j++) {
				blocks.add(new Water(78+36*i,532+36*j));
			}
		}
		for(int i=0; i<8; i++) {
			for(int j=0; j<2; j++) {
				blocks.add(new Water(294+36*i,532+36*j));
			}
		}
		for(int i=0; i<2; i++) {
			for(int j=0; j<2; j++) {
				blocks.add(new Water(438+36*i,604+36*j));
			}
		}
		for(int i=0; i<2; i++) {
			for(int j=0; j<2; j++) {
				blocks.add(new Water(726+36*i,604+36*j));
			}
		}
		for(int i=0; i<4; i++) {
			for(int j=0; j<2; j++) {
				blocks.add(new Water(6+36*i,676+36*j));
			}
		}
		for(int i=0; i<4; i++) {
			for(int j=0; j<2; j++) {
				blocks.add(new Water(222+36*i,676+36*j));
			}
		}
		for(int i=0; i<6; i++) {
			for(int j=0; j<2; j++) {
				blocks.add(new Water(582+36*i,676+36*j));
			}
		}
		//绘制树
		for(int i=0; i<4; i++) {
			for(int j=0; j<2; j++) {
				blocks.add(new Tree(6+36*i,172+36*j));
			}
		}
		for(int i=0; i<2; i++) {
			for(int j=0; j<2; j++) {
				blocks.add(new Tree(6+36*i,244+36*j));
			}
		}
		for(int i=0; i<4; i++) {
			for(int j=0; j<2; j++) {
				blocks.add(new Tree(6+36*i,316+36*j));
			}
		}
		for(int i=0; i<4; i++) {
			for(int j=0; j<2; j++) {
				blocks.add(new Tree(726+36*i,244+36*j));
			}
		}
		for(int i=0; i<2; i++) {
			for(int j=0; j<2; j++) {
				blocks.add(new Tree(798+36*i,172+36*j));
			}
		}
		for(int i=0; i<2; i++) {
			for(int j=0; j<4; j++) {
				blocks.add(new Tree(222+36*i,460+36*j));
			}
		}
		for(int i=0; i<2; i++) {
			for(int j=0; j<2; j++) {
				blocks.add(new Tree(510+36*i,460+36*j));
			}
		}
		for(int i=0; i<2; i++) {
			for(int j=0; j<4; j++) {
				blocks.add(new Tree(654+36*i,532+36*j));
			}
		}
		for(int i=0; i<2; i++) {
			for(int j=0; j<2; j++) {
				blocks.add(new Tree(582+36*i,604+36*j));
			}
		}
		//绘制墙体
		for(int i=0; i<2; i++) {
			for(int j=0; j<2; j++) {
				blocks.add(new Wall(438+36*i,460+36*j));
			}
		}
		for(int i=0; i<2; i++) {
			for(int j=0; j<2; j++) {
				blocks.add(new Wall(222+36*i,604+36*j));
			}
		}
		for(int i=0; i<2; i++) {
			for(int j=0; j<2; j++) {
				blocks.add(new Wall(510+36*i,676+36*j));
			}
		}
		for(int i=0; i<2; i++) {
			for(int j=0; j<2; j++) {
				blocks.add(new Wall(726+36*i,532+36*j));
			}
		}
		//绘制石头
		for(int i=0; i<2; i++) {
			for(int j=0; j<2; j++) {
				blocks.add(new Stone(438+36*i,244+36*j));
			}
		}
	}
}
