import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;
public class GameFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Image iBuffer = null;
	Graphics graImg = null;
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	public ArrayList<Explode> foeExp = new ArrayList<Explode>();
	public ArrayList<Bullet> playerBullets = new ArrayList<Bullet>();
	public ArrayList<Bullet> foeBullets = new ArrayList<Bullet>();
	public ArrayList<Foe> foes = new ArrayList<Foe>();
	public ArrayList<Block> blocks = new ArrayList<Block>();
	public Player player = new Player(blocks);
	public Player netPlayer = new Player(blocks);
	public boolean isAir = false;
	public boolean unless = false;
	public boolean NetMode = false;
	public boolean connected = false;
	public boolean isWaiting = false;
	public ServerSocket ss = null;
	public static String anotherIp;
	public static String localIp;
	public static int localPort;
	public static int anotherPort;
	public int mapIndex = 0;
	public AttackThread atcTh;
	public RepaintThread repaint = new RepaintThread(this);
	public URL startSound = GameFrame.class.getClassLoader().getResource("sounds/start.wav");
	public URL expSound = GameFrame.class.getClassLoader().getResource("sounds/exp.wav");
	AudioClip startClip = Applet.newAudioClip(startSound);
	AudioClip expClip = Applet.newAudioClip(expSound);
	public GameMap gameMap = null; 
	private int unlessCount = 0;
	private Explode playerExp = null;
	private Explode netPlayerExp = null;
	private int expNum = 0;
	private int netExpNum = 0;
	private static Image backgroundImg = tk.getImage(GameFrame.class.getClassLoader().getResource("images/bgi.png"));
	private static Image agimg = tk.getImage(GameFrame.class.getClassLoader().getResource("images/gg.png"));
	private static Image[] playerImgs = {
			tk.getImage(GameFrame.class.getClassLoader().getResource("images/playerUp.png")),
			tk.getImage(GameFrame.class.getClassLoader().getResource("images/playerDown.png")),
			tk.getImage(GameFrame.class.getClassLoader().getResource("images/playerLeft.png")),
			tk.getImage(GameFrame.class.getClassLoader().getResource("images/playerRight.png")),
			tk.getImage(GameFrame.class.getClassLoader().getResource("images/playerBullet.png"))
	};
	private static Image[] netPlayerImgs = {
			tk.getImage(GameFrame.class.getClassLoader().getResource("images/NetPlayer/playerUp.png")),
			tk.getImage(GameFrame.class.getClassLoader().getResource("images/NetPlayer/playerDown.png")),
			tk.getImage(GameFrame.class.getClassLoader().getResource("images/NetPlayer/playerLeft.png")),
			tk.getImage(GameFrame.class.getClassLoader().getResource("images/NetPlayer/playerRight.png"))
	};
	private static Image[] foeImgs = {
			tk.getImage(GameFrame.class.getClassLoader().getResource("images/foeUp.png")),
			tk.getImage(GameFrame.class.getClassLoader().getResource("images/foeDown.png")),
			tk.getImage(GameFrame.class.getClassLoader().getResource("images/foeLeft.png")),
			tk.getImage(GameFrame.class.getClassLoader().getResource("images/foeRight.png")),
			tk.getImage(GameFrame.class.getClassLoader().getResource("images/foeBullet.png"))
	};
	private static Image[] blockImgs = {
			tk.getImage(GameFrame.class.getClassLoader().getResource("images/wall.png")),
			tk.getImage(GameFrame.class.getClassLoader().getResource("images/tree.png")),
			tk.getImage(GameFrame.class.getClassLoader().getResource("images/water.png")),
			tk.getImage(GameFrame.class.getClassLoader().getResource("images/stone.png")),
	};
	private static Image[] endImgs = {
			tk.getImage(GameFrame.class.getClassLoader().getResource("images/gameover.png")),
			tk.getImage(GameFrame.class.getClassLoader().getResource("images/win.png")),
			tk.getImage(GameFrame.class.getClassLoader().getResource("images/winner.png"))
	};
	private static Image[] expImgs = {
			tk.getImage(GameFrame.class.getClassLoader().getResource("images/exp/exp0.png")),
			tk.getImage(GameFrame.class.getClassLoader().getResource("images/exp/exp1.png")),
			tk.getImage(GameFrame.class.getClassLoader().getResource("images/exp/exp2.png")),
			tk.getImage(GameFrame.class.getClassLoader().getResource("images/exp/exp3.png")),
			tk.getImage(GameFrame.class.getClassLoader().getResource("images/exp/exp4.png")),
			tk.getImage(GameFrame.class.getClassLoader().getResource("images/exp/exp5.png"))
	};

	GameFrame() {

		//初始化游戏界面
		super("Tank War");
		player.reset();
		this.setLayout(null);
		this.setBounds(200, 50, 1092, 754);
		this.setResizable(false);

		//设置地图
		gameMap = new GameMap(blocks);
		gameMap.setMap(mapIndex);

		//向foes添加Foe对象
		for(int i=0; i<5 ;i++) {
			foes.add(new Foe(foes,blocks,player));
		}

		//添加键盘监听
		this.addKeyListener(new GameKeyAdapter());
		//添加窗口关闭监听
		this.addWindowListener (new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});


		//显示窗口用开启刷新线程
		startClip.play();
		this.setVisible(true);
		atcTh = new AttackThread(this);
		atcTh.start();
		repaint.start();

	}

	public void reset() {
		startClip.play();
		if(!connected) {
			player.reset();
			foes.clear();
			foeBullets.clear();
			playerBullets.clear();
			gameMap.setMap(mapIndex);
			for(int i=0; i<5; i++) {
				foes.add(new Foe(foes,blocks,player));
			}
		} else {
//			System.out.println("Frame已重置");
			if(isAir) {
				player.reset();
				netPlayer.nReset();
				foeBullets.clear();
				playerBullets.clear();
				gameMap.setMap(mapIndex);
			} else {
				netPlayer.reset();
				player.nReset();
				foeBullets.clear();
				playerBullets.clear();
				gameMap.setMap(mapIndex);
			}
		}

	}


	public static void sendMsg(String msg) {
		try {
			DatagramSocket ds = new DatagramSocket();
			DatagramPacket dp = new DatagramPacket(msg.getBytes(),msg.getBytes().length,InetAddress.getByName(anotherIp),anotherPort);
			ds.send(dp);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}

	public void paint(Graphics g) {


		//首尾两行为双缓冲操作 解决在JFrame中不调用repaint方法使重写repaint双缓冲无效的问题  同时没有使用自带双缓冲的Jpanel
		this.iBuffer = this.createVolatileImage(this.getWidth(), this.getHeight());
		this.graImg = this.iBuffer.getGraphics();	
		//画出背景图
		graImg.drawImage(backgroundImg, 0, 0, null);
		graImg.setFont(new Font("FontA",1,35));
		graImg.drawString("当前血量:", 920, 315);
		graImg.drawString("" + player.life, 920, 350);
		if(!NetMode) {
			//单人模式
			graImg.drawString("当前分数:", 920, 225);
			graImg.drawString("" + player.score, 920, 260);
			graImg.drawString("剩余敌人:", 920, 135);
			graImg.drawString("" + foes.size(), 920, 170);
			graImg.setFont(new Font("FontB",1,20));
			graImg.drawString("无尽模式:", 923, 77);
			if(unless) {
				graImg.drawString("开启", 1023, 77);
			} else {
				graImg.drawString("关闭", 1023, 77);
			}
			graImg.drawString("操作指南:", 915, 400);
			graImg.drawString("方向键：控制移动", 915, 435);
			graImg.drawString("Space：发射炮弹", 915, 460);
			graImg.drawString("R：重置战场", 915, 485);
			graImg.drawString("O：无尽模式", 915, 510);
			graImg.drawString("ESC：暂停游戏", 915, 535);
			graImg.drawString("PAGEUP/DOWN:", 915, 560);
			graImg.drawString("增/减敌人数", 915, 585);
			graImg.drawString("NUMPAD 1-9:", 915, 610);
			graImg.drawString("更换地图", 915, 635);
			graImg.drawString("L:开启联机模式", 915, 670);
			//判断玩家是否存活
			if(player.alive) {
				//画出玩家坦克 同时刷新位置
				graImg.drawImage(playerImgs[player.dir], player.x, player.y, null);
				player.move();
				//为防止连发子弹 加入shoot冷却机制
				if(player.shootCD > 0) {
					player.shootCD--;
				}

				//判断是否开启无尽模式
				if(unless && foes.size()<15) {
					unlessCount++;
					//每5秒刷新一个敌人
					if(unlessCount >= 500) {
						foes.add(new Foe(foes,blocks,player));
						unlessCount = 0;
					}
				}
				//画出敌人
				if(foes.size() != 0) {
					Iterator<Foe> i = foes.iterator();
					while(i.hasNext()) {
						Foe thisFoe = i.next();
						graImg.drawImage(foeImgs[thisFoe.dir], thisFoe.x, thisFoe.y, null);
						//绘出每个敌人同时刷新位置
						thisFoe.move();
						//敌人每移动2次有0.5的几率打出一发子弹
						if(thisFoe.shootCount == 2) {
							if(Math.random()>0.5) {
								foeBullets.add(thisFoe.shoot());
							}
							thisFoe.shootCount = 0;
						}
					}
				}
				//为实现子弹在水面飞过的效果需要在绘制Bullet前先绘制Water
				drawBlock(blocks,true,graImg);
				//画出玩家子弹
				drawBullet(playerBullets,false,graImg);
				//画出foe子弹
				drawBullet(foeBullets,true,graImg);
				//画出爆炸效果
				ArrayList<Explode> movedExp = new ArrayList<Explode>();
				if(foeExp.size() != 0) {
					Iterator<Explode> i = foeExp.iterator();
					while(i.hasNext()) {
						Explode thisExp = i.next();
						if(thisExp.exist) {
							if(thisExp.paintNum<10) {
								graImg.drawImage(expImgs[thisExp.num], thisExp.x, thisExp.y, null);
								thisExp.paintNum++;
							} else {
								thisExp.next();
								thisExp.paintNum = 0;
							}

						} else {
							movedExp.add(thisExp);
						}
					}
				}
				foeExp.removeAll(movedExp);
				//最后画Block
				drawBlock(blocks,false,graImg);
				if(!unless && foes.size() == 0){
					foeBullets.clear();
					playerBullets.clear();
					player.x = 431;
					player.y = 698;
					player.dir = 0;
					player.killAll = true;
					graImg.drawImage(endImgs[1], 6, 28, null);
				}
			} else {
				if(playerExp == null && !player.exploded) {
					playerExp = new Explode(player.x,player.y);
					expClip.play();
				}
				if(!player.exploded && playerExp.exist) {
					if(expNum<15) {
						graImg.drawImage(expImgs[playerExp.num], playerExp.x, playerExp.y, null);
						expNum++;
					} else {
						graImg.drawImage(expImgs[playerExp.num], playerExp.x, playerExp.y, null);
						expNum = 0;
						playerExp.next();
					}
				} else {
					player.exploded = true;
					playerExp = null;
					foes.clear();
					foeBullets.clear();
					playerBullets.clear();
					graImg.drawImage(endImgs[0], 6, 28, null);
				}
			}
		} else {
			//开启联机模式
			graImg.drawString("敌人血量:", 920, 225);
			graImg.drawString("" + netPlayer.life, 920, 260);
			if(connected) {
				graImg.drawString("连接成功!!", 920, 135);
				if(player.alive&&netPlayer.alive) {
					drawBlock(blocks,true,graImg);
					drawPlayer(player,false,graImg);
					drawPlayer(netPlayer,true,graImg);
					drawBullet(playerBullets,false,graImg);
					drawBullet(foeBullets,true,graImg);
					drawBlock(blocks,false,graImg);
				} else if(!player.alive) {
					if(playerExp == null && !player.exploded) {
						playerExp = new Explode(player.x,player.y);
						expClip.play();
					}
					if(!player.exploded && playerExp.exist) {
						if(expNum<15) {
							graImg.drawImage(expImgs[playerExp.num], playerExp.x, playerExp.y, null);
							expNum++;
						} else {
							graImg.drawImage(expImgs[playerExp.num], playerExp.x, playerExp.y, null);
							expNum = 0;
							playerExp.next();
						}
					} else {
						player.exploded = true;
						playerExp = null;
						foeBullets.clear();
						playerBullets.clear();
						graImg.drawImage(endImgs[0], 6, 28, null);
					}
				} else if(!netPlayer.alive) {
					if(netPlayerExp == null && !netPlayer.exploded) {
						netPlayerExp = new Explode(netPlayer.x,netPlayer.y);
						expClip.play();
					}
					if(!netPlayer.exploded && netPlayerExp.exist) {
						if(netExpNum<15) {
							graImg.drawImage(expImgs[netPlayerExp.num], netPlayerExp.x, netPlayerExp.y, null);
							netExpNum++;
						} else {
							graImg.drawImage(expImgs[netPlayerExp.num], netPlayerExp.x, netPlayerExp.y, null);
							netExpNum = 0;
							netPlayerExp.next();
						}
					} else {
						netPlayer.exploded = true;
						netPlayerExp = null;
						foeBullets.clear();
						playerBullets.clear();
						graImg.drawImage(endImgs[2], 6, 28, null);
					}
				}

			} else {
				graImg.drawString("等待连接...", 920, 135);
			}
		}

		this.graImg.dispose();
		g.drawImage(this.iBuffer, 0, 0, this);			
	}

	private static void drawPlayer(Player player,boolean isFoe,Graphics g) {
		if(isFoe) {
			g.drawImage(netPlayerImgs[player.dir], player.x, player.y, null);
		} else {
			g.drawImage(playerImgs[player.dir], player.x, player.y, null);
		}
		player.move();
		if(player.shootCD > 0) {
			player.shootCD--;
		}
	}
	private static void drawBlock(ArrayList<Block> blocks,boolean isWater,Graphics g) {
		try {
			for(Block thisBlock:blocks) {
				if(isWater) {
					if(thisBlock instanceof Water) {
						g.drawImage(blockImgs[2], thisBlock.x, thisBlock.y, null);
					}
				} else {
					if(thisBlock instanceof Wall) {
						g.drawImage(blockImgs[0], thisBlock.x, thisBlock.y, null);
					}
					if(thisBlock instanceof Tree) {
						g.drawImage(blockImgs[1], thisBlock.x, thisBlock.y, null);
					}
					if(thisBlock instanceof Stone) {
						g.drawImage(blockImgs[3], thisBlock.x, thisBlock.y, null);
					}
				}
			}
		} catch(Exception e) {
		}

	}
	private static void drawBullet(ArrayList<Bullet> bullets,boolean isFoe,Graphics g) {
		Image img;
		if(isFoe) {
			img = foeImgs[4];
		} else {
			img = playerImgs[4];
		}
		try {
			for(Bullet thisBullet:bullets) {
				g.drawImage(img, thisBullet.x, thisBullet.y, null);
				thisBullet.move();
			}
		} catch(Exception e) {
		}
	}
}
