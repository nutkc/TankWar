import java.util.ArrayList;
import java.util.Iterator;

public class AttackThread extends Thread {
	public ArrayList<Explode> foeExp = null;
	public ArrayList<Bullet> playerBullets = null;
	public ArrayList<Bullet> foeBullets = null;
	public ArrayList<Foe> foes = null;
	public ArrayList<Block> blocks = null;
	public Player player = null;
	public GameFrame gf = null;
	public boolean reStart = false;
	//���췽�� �������г��϶��� ���������ɾ����
	public AttackThread(GameFrame gf) {
		super();
		this.gf = gf;
		this.foeExp = gf.foeExp;
		this.playerBullets = gf.playerBullets;
		this.foeBullets = gf.foeBullets;
		this.foes = gf.foes;
		this.blocks = gf.blocks;
		this.player = gf.player;
	}

	@Override
	public void run() {
		// TODO �Զ����ɵķ������
		while(!reStart) {
			//			���ָ��߳����������sleep��Ῠ��
			try {
				Thread.sleep(8);
			} catch(Exception e) {
				e.printStackTrace();
			}

			ArrayList<Block> movedBlocks = new ArrayList<Block>();
			ArrayList<Bullet> movedBullets = new ArrayList<Bullet>();
			ArrayList<Foe> movedFoes = new ArrayList<Foe>();
			if(!gf.connected) {
				//����playerBullets��foes�����е�һ����ӵ�moved������
				try {
					if(playerBullets.size() != 0) {
						Iterator<Bullet> pbi = playerBullets.iterator();
						while(pbi.hasNext()) {
							Bullet thisBullet = pbi.next();
							if(thisBullet.x<6 ||
									thisBullet.x>896 ||
									thisBullet.y<28 ||
									thisBullet.y>738) {
								movedBullets.add(thisBullet);
								continue;
							}
							if(foes.size() != 0) {
	
								Iterator<Foe> foei = foes.iterator();
								while(foei.hasNext()) {
									Foe thisFoe = foei.next();
									if(thisBullet.x+10>thisFoe.x &&
											thisBullet.x<thisFoe.x+50 &&
											thisBullet.y+10>thisFoe.y &&
											thisBullet.y<thisFoe.y+50) {
										thisFoe.attacked();
										if(!thisFoe.alive) {
											movedFoes.add(thisFoe);
											foeExp.add(new Explode(thisFoe.x,thisFoe.y));
										}
										movedBullets.add(thisBullet);
										break;
									}
								}
	
							}
							//�ж�playerBullet��Block��ײ���¼�
							if(blocks.size() != 0) {
								Iterator<Block> i = blocks.iterator();
								while(i.hasNext()) {
									Block thisBlock = i.next();
									if(!thisBlock.shootable) {
										if(thisBullet.x<thisBlock.x+36 &&
												thisBullet.x+10>thisBlock.x &&
												thisBullet.y<thisBlock.y+36 &&
												thisBullet.y+10>thisBlock.y) {
											if(thisBlock.breakable) {
												thisBlock.attacked();
											}
											if(!thisBlock.alive) {
												movedBlocks.add(thisBlock);
											}
											movedBullets.add(thisBullet);
										}
									}
								}								
							}
						}
	
					}
					if (movedBlocks.size() != 0) {
						blocks.removeAll(movedBlocks);
					}
					if (movedBullets.size() != 0) {
						playerBullets.removeAll(movedBullets);
					}
					if (movedFoes.size() != 0) {
						player.score += movedFoes.size() * 100;
						foes.removeAll(movedFoes);
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
				try {
					//����foeBullets�ж�����
					if(foeBullets.size() != 0) {
						Iterator<Bullet> i = foeBullets.iterator();
						while(i.hasNext()) {
							Bullet thisBullet = i.next();
							if(thisBullet.x<6 ||
									thisBullet.x>896 ||
									thisBullet.y<28 ||
									thisBullet.y>738) {
								movedBullets.add(thisBullet);
								continue;
							}
							if(thisBullet.x+10>player.x &&
									thisBullet.x<player.x+50 &&
									thisBullet.y+10>player.y &&
									thisBullet.y<player.y+50) {
								movedBullets.add(thisBullet);
								player.attacked();
								continue;
							}
							if(blocks.size() != 0) {
								Iterator<Block> blockIte = blocks.iterator();
								while(blockIte.hasNext()) {
									Block thisBlock = blockIte.next();
									if(!thisBlock.shootable) {
										if(thisBullet.x<thisBlock.x+36 &&
												thisBullet.x+10>thisBlock.x &&
												thisBullet.y<thisBlock.y+36 &&
												thisBullet.y+10>thisBlock.y) {
											if(thisBlock.breakable) {
												thisBlock.attacked();
											}
											if(!thisBlock.alive) {
												movedBlocks.add(thisBlock);
											}
											movedBullets.add(thisBullet);
										}
									}
								}								
							}
						}
						if (movedBlocks.size() != 0) {
							blocks.removeAll(movedBlocks);
						}
						if (movedBullets.size() != 0) {
							foeBullets.removeAll(movedBullets);
						}
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
	
				try {
					//����foes�ж���ײ
					if(foes.size() != 0) {
						Iterator<Foe> i = foes.iterator();
						while(i.hasNext()) {
							Foe thisFoe = i.next();
							if(! thisFoe.setSuccess) {
								movedFoes.add(thisFoe);
								continue;
							}
							if(thisFoe.x+50>player.x &&
									thisFoe.x<player.x+50 &&
									thisFoe.y+50>player.y &&
									thisFoe.y<player.y+50) {
								movedFoes.add(thisFoe);
								player.life = 0;
								player.alive = false;
							}
						}
						if (movedFoes.size() != 0) {
							foes.removeAll(movedFoes);
						}
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			} else {
				//����playerBullets�ж��� ���ˣ������Ĺ����������л�����Bullet��ӵ�movedbullets��
				try {
					for(Bullet thisBullet:playerBullets) {
						if(thisBullet.x < 6 ||
								thisBullet.x > 896 ||
								thisBullet.y < 28 ||
								thisBullet.y >738) {
							movedBullets.add(thisBullet);
							continue;
						}
						for(Block thisBlock:blocks) {
							if(!thisBlock.shootable) {
								if(thisBullet.x<thisBlock.x+36 &&
										thisBullet.x+10>thisBlock.x &&
										thisBullet.y<thisBlock.y+36 &&
										thisBullet.y+10>thisBlock.y) {
									if(thisBlock.breakable) {
										thisBlock.attacked();
									}
									if(!thisBlock.alive) {
										movedBlocks.add(thisBlock);
									}
									movedBullets.add(thisBullet);
								}
							}
						}
						if(thisBullet.x+10>gf.netPlayer.x &&
								thisBullet.x<gf.netPlayer.x+50 &&
								thisBullet.y+10>gf.netPlayer.y &&
								thisBullet.y<gf.netPlayer.y+50) {
							gf.netPlayer.attacked();
							movedBullets.add(thisBullet);
						}
					}
				} catch(Exception e) {
				}
				//����foeBullets�ж��� ��ң������Ĺ��� �������л�����Bullet����movedBullets��
				try {
					for(Bullet thisBullet:foeBullets) {
						if(thisBullet.x < 6 ||
								thisBullet.x > 896 ||
								thisBullet.y < 28 ||
								thisBullet.y >738) {
							movedBullets.add(thisBullet);
							continue;
						}
						for(Block thisBlock:blocks) {
							if(!thisBlock.shootable) {
								if(thisBullet.x<thisBlock.x+36 &&
										thisBullet.x+10>thisBlock.x &&
										thisBullet.y<thisBlock.y+36 &&
										thisBullet.y+10>thisBlock.y) {
									if(thisBlock.breakable) {
										thisBlock.attacked();
									}
									if(!thisBlock.alive) {
										movedBlocks.add(thisBlock);
									}
									movedBullets.add(thisBullet);
								}
							}
						}
						if(thisBullet.x+10>gf.player.x &&
								thisBullet.x<gf.player.x+50 &&
								thisBullet.y+10>gf.player.y &&
								thisBullet.y<gf.player.y+50) {
							gf.player.attacked();
							movedBullets.add(thisBullet);
						}
					}
				} catch(Exception e) {					
				}
				
				//�ж� ��� �� �����Ƿ���ײ
				if(gf.player.x+50>gf.netPlayer.x &&
						gf.player.x<gf.netPlayer.x+50 &&
						gf.player.y+50>gf.netPlayer.y &&
						gf.player.y<gf.netPlayer.y+50) {
					player.life = 0;
					player.alive = false;
					gf.netPlayer.life = 0;
					gf.netPlayer.alive =false;
				}
				//�Ƴ����з�����ײ��Bullet
				foeBullets.removeAll(movedBullets);
				playerBullets.removeAll(movedBullets);
				blocks.removeAll(movedBlocks);
			}
				
		}

	}
}
