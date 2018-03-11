import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameKeyAdapter extends KeyAdapter{

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO 自动生成的方法存根
		GameFrame gf = (GameFrame)e.getSource();
		switch(e.getKeyCode()) {
		case KeyEvent.VK_F11:
			if(Foe.speed > 2) {
				Foe.speed--;
			}
			break;
		case KeyEvent.VK_F12:
			if(Foe.speed < 10) {
				Foe.speed++;
			}
			break;
		case KeyEvent.VK_F8:
			gf.player.life = 999999;
			break;
		case KeyEvent.VK_UP:
			gf.player.up = true;
//			gf.player.down = false;
//			gf.player.left = false;
//			gf.player.right = false;
			break;
		case KeyEvent.VK_DOWN:
//			gf.player.up = false;
			gf.player.down = true;
//			gf.player.left = false;
//			gf.player.right = false;
			break;
		case KeyEvent.VK_LEFT:
//			gf.player.up = false;
//			gf.player.down = false;
			gf.player.left = true;
//			gf.player.right = false;
			break;
		case KeyEvent.VK_RIGHT:
//			gf.player.up = false;
//			gf.player.down = false;
//			gf.player.left = false;
			gf.player.right = true;
			break;
		case KeyEvent.VK_SPACE:
			if(gf.player.shootCD == 0) {
				gf.playerBullets.add(gf.player.shoot());
				gf.player.shootCD = 15;
			}
			break;
		case KeyEvent.VK_PAGE_UP:
			gf.foes.add(new Foe(gf.foes,gf.blocks,gf.player));
			break;
		case KeyEvent.VK_PAGE_DOWN:
			if(gf.foes.size() != 0) {
				gf.foes.remove(0);
			}
			break;
		case KeyEvent.VK_R:
			GameFrame.sendMsg("R,"+gf.mapIndex+",");
			gf.reset();
			gf.atcTh.reStart = true;
			gf.atcTh = new AttackThread(gf);
			gf.atcTh.start();
			break;
		case KeyEvent.VK_O:
			gf.unless = !gf.unless;
			break;
		case KeyEvent.VK_NUMPAD1:
			gf.mapIndex = 0;
			GameFrame.sendMsg("R,0,");
			gf.reset();
			break;
		case KeyEvent.VK_NUMPAD2:
			gf.mapIndex = 1;
			GameFrame.sendMsg("R,1,");
			gf.reset();
			break;
		case KeyEvent.VK_ESCAPE:
			if(gf.repaint.runningFlag) {
				gf.repaint.runningFlag = false;
			} else {
				gf.repaint = new RepaintThread(gf);
				gf.repaint.start();
			}
			break;
		case KeyEvent.VK_K:
			if(!gf.isWaiting) {
				gf.isWaiting = true;
				gf.player.reset();
				gf.foes.clear();
				gf.blocks.clear();
				gf.NetMode = true;
				UDPAir ua = new UDPAir(gf);
				new Thread(ua).start();
				gf.anotherPort = 1611;
				gf.netPlayer.nReset();
				gf.player.reset();
			}
			break;
		case KeyEvent.VK_L:
			if(!gf.isWaiting) {
				gf.isWaiting = true;
				gf.player.reset();
				gf.foes.clear();
				gf.blocks.clear();
				gf.NetMode = true;
				new Thread(new UDPConnect(gf)).start();
				gf.anotherPort = 1612;
				gf.netPlayer.reset();
				gf.player.nReset();
			}
			break;
		}
	}

	
	
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO 自动生成的方法存根
		GameFrame gf = (GameFrame)e.getSource();
		switch(e.getKeyCode()) {
		case KeyEvent.VK_UP:
			gf.player.up = false;
			break;
		case KeyEvent.VK_DOWN:
			gf.player.down = false;
			break;
		case KeyEvent.VK_LEFT:
			gf.player.left = false;
			break;
		case KeyEvent.VK_RIGHT:
			gf.player.right = false;
			break;
		}
	}
	
}
