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

		//��ʼ����Ϸ����
		super("Tank War");
		player.reset();
		this.setLayout(null);
		this.setBounds(200, 50, 1092, 754);
		this.setResizable(false);

		//���õ�ͼ
		gameMap = new GameMap(blocks);
		gameMap.setMap(mapIndex);

		//��foes���Foe����
		for(int i=0; i<5 ;i++) {
			foes.add(new Foe(foes,blocks,player));
		}

		//��Ӽ��̼���
		this.addKeyListener(new GameKeyAdapter());
		//��Ӵ��ڹرռ���
		this.addWindowListener (new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});


		//��ʾ�����ÿ���ˢ���߳�
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
//			System.out.println("Frame������");
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
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}

	}

	public void paint(Graphics g) {


		//��β����Ϊ˫������� �����JFrame�в�����repaint����ʹ��дrepaint˫������Ч������  ͬʱû��ʹ���Դ�˫�����Jpanel
		this.iBuffer = this.createVolatileImage(this.getWidth(), this.getHeight());
		this.graImg = this.iBuffer.getGraphics();	
		//��������ͼ
		graImg.drawImage(backgroundImg, 0, 0, null);
		graImg.setFont(new Font("FontA",1,35));
		graImg.drawString("��ǰѪ��:", 920, 315);
		graImg.drawString("" + player.life, 920, 350);
		if(!NetMode) {
			//����ģʽ
			graImg.drawString("��ǰ����:", 920, 225);
			graImg.drawString("" + player.score, 920, 260);
			graImg.drawString("ʣ�����:", 920, 135);
			graImg.drawString("" + foes.size(), 920, 170);
			graImg.setFont(new Font("FontB",1,20));
			graImg.drawString("�޾�ģʽ:", 923, 77);
			if(unless) {
				graImg.drawString("����", 1023, 77);
			} else {
				graImg.drawString("�ر�", 1023, 77);
			}
			graImg.drawString("����ָ��:", 915, 400);
			graImg.drawString("������������ƶ�", 915, 435);
			graImg.drawString("Space�������ڵ�", 915, 460);
			graImg.drawString("R������ս��", 915, 485);
			graImg.drawString("O���޾�ģʽ", 915, 510);
			graImg.drawString("ESC����ͣ��Ϸ", 915, 535);
			graImg.drawString("PAGEUP/DOWN:", 915, 560);
			graImg.drawString("��/��������", 915, 585);
			graImg.drawString("NUMPAD 1-9:", 915, 610);
			graImg.drawString("������ͼ", 915, 635);
			graImg.drawString("L:��������ģʽ", 915, 670);
			//�ж�����Ƿ���
			if(player.alive) {
				//�������̹�� ͬʱˢ��λ��
				graImg.drawImage(playerImgs[player.dir], player.x, player.y, null);
				player.move();
				//Ϊ��ֹ�����ӵ� ����shoot��ȴ����
				if(player.shootCD > 0) {
					player.shootCD--;
				}

				//�ж��Ƿ����޾�ģʽ
				if(unless && foes.size()<15) {
					unlessCount++;
					//ÿ5��ˢ��һ������
					if(unlessCount >= 500) {
						foes.add(new Foe(foes,blocks,player));
						unlessCount = 0;
					}
				}
				//��������
				if(foes.size() != 0) {
					Iterator<Foe> i = foes.iterator();
					while(i.hasNext()) {
						Foe thisFoe = i.next();
						graImg.drawImage(foeImgs[thisFoe.dir], thisFoe.x, thisFoe.y, null);
						//���ÿ������ͬʱˢ��λ��
						thisFoe.move();
						//����ÿ�ƶ�2����0.5�ļ��ʴ��һ���ӵ�
						if(thisFoe.shootCount == 2) {
							if(Math.random()>0.5) {
								foeBullets.add(thisFoe.shoot());
							}
							thisFoe.shootCount = 0;
						}
					}
				}
				//Ϊʵ���ӵ���ˮ��ɹ���Ч����Ҫ�ڻ���Bulletǰ�Ȼ���Water
				drawBlock(blocks,true,graImg);
				//��������ӵ�
				drawBullet(playerBullets,false,graImg);
				//����foe�ӵ�
				drawBullet(foeBullets,true,graImg);
				//������ըЧ��
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
				//���Block
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
			//��������ģʽ
			graImg.drawString("����Ѫ��:", 920, 225);
			graImg.drawString("" + netPlayer.life, 920, 260);
			if(connected) {
				graImg.drawString("���ӳɹ�!!", 920, 135);
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
				graImg.drawString("�ȴ�����...", 920, 135);
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
