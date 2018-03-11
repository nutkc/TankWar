import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class RecThread implements Runnable {
	private Player another;
	private DatagramSocket ds;
	private ArrayList<Bullet> bullets;
	private GameFrame gf;
	private static long lastTime = 0l;
	public RecThread(GameFrame gf) {
		// TODO 自动生成的构造函数存根
		this.gf = gf;
		this.bullets = gf.foeBullets;
		this.another = gf.netPlayer;
		try {
			ds = new DatagramSocket(gf.localPort,InetAddress.getByName(gf.localIp));
		} catch (SocketException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	
	//建立一个方法用来识别接受的字符数组的指定信息位的值 
	//如第一个数字信息值index=1
	private static long trans(char[] msg,int index) {
		int num = 0;
		long buf = 0;
		for(int i=0;;i++) {
			if(msg[i] == ',') {
				num++;
			} else if(msg[i]>='0' && msg[i]<='9') {
				if(msg[i-1] == ',') {
					buf = 0;
				}
				buf = (buf * 10) + msg[i] -48;
			}
			if(num == index+1) {
				return buf;
			}
		}
	}
	
	
	
	
	@Override
	public void run() {
		// TODO 自动生成的方法存根
		while(true) {
			byte[] buf = new byte[1024];
			DatagramPacket dp = new DatagramPacket(buf,buf.length);
			try {
				ds.receive(dp);
			} catch (IOException e) {
				
			}
			String msg = new String(dp.getData(),0,dp.getLength());
			System.out.println(msg);
			char[] charArr = msg.toCharArray();
			switch(charArr[0]) {
			case 'p':
				if(lastTime == 0) {
					lastTime = trans(charArr,5);
				}
				if(lastTime <= trans(charArr,5)) {
					another.x = (int)trans(charArr,1);
					another.y = (int)trans(charArr,2);
					another.dir = (int)trans(charArr,3);
					another.life = (int)trans(charArr,4);
					lastTime = trans(charArr,5);
				}
				break;
			case 'b':
				try {
					bullets.add(new Bullet((int)trans(charArr,1),(int)trans(charArr,2),(int)trans(charArr,3)));
				} catch(Exception e) {}
				break;
			case 'R':
				gf.mapIndex = (int)trans(charArr,1);
				gf.reset();
				break;
			}
		}
	}
}
