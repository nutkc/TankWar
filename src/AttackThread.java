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
	//构造方法 传入所有场上对象 对其进行增删操作
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
		// TODO 自动生成的方法存根
		while(!reStart) {
			//			发现副线程中若不添加sleep则会卡死
			try {
				Thread.sleep(8);
			} catch(Exception e) {
				e.printStackTrace();
			}

			ArrayList<Block> movedBlocks = new ArrayList<Block>();
			ArrayList<Bullet> movedBullets = new ArrayList<Bullet>();
			ArrayList<Foe> movedFoes = new ArrayList<Foe>();
			if(!gf.connected) {
				//遍历playerBullets和foes将击中的一组添加到moved集合中
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
							//判定playerBullet和Block的撞击事件
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
					//遍历foeBullets判定攻击
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
					//遍历foes判定碰撞
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
				//遍历playerBullets判定对 敌人，建筑的攻击并将击中或出界的Bullet添加到movedbullets中
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
				//遍历foeBullets判定对 玩家，建筑的攻击 并将击中或出界的Bullet加入movedBullets中
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
				
				//判定 玩家 与 敌人是否碰撞
				if(gf.player.x+50>gf.netPlayer.x &&
						gf.player.x<gf.netPlayer.x+50 &&
						gf.player.y+50>gf.netPlayer.y &&
						gf.player.y<gf.netPlayer.y+50) {
					player.life = 0;
					player.alive = false;
					gf.netPlayer.life = 0;
					gf.netPlayer.alive =false;
				}
				//移除所有发生碰撞的Bullet
				foeBullets.removeAll(movedBullets);
				playerBullets.removeAll(movedBullets);
				blocks.removeAll(movedBlocks);
			}
				
		}

	}
}
