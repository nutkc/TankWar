import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;

public class UDPConnect implements Runnable {
	private GameFrame gf;
	private DatagramSocket ds;
	private DatagramSocket dsR;
	private DatagramPacket dp;
	private DatagramPacket dpS;
	private String ip;
	private ArrayList<String> allIp;
	public UDPConnect(GameFrame gf) {
		// TODO 自动生成的构造函数存根
		this.gf = gf;
		try {
			dsR = new DatagramSocket(1611);
		} catch (SocketException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		try {
			ds = new DatagramSocket();
		} catch (SocketException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		allIp = UDPAir.getAllIp();
	}
	
	
	
	
	
	@Override
	public void run() {
		// TODO 自动生成的方法存根
		byte[] buf = new byte[1024];
		dp = new DatagramPacket(buf,buf.length);
		try {
			dsR.receive(dp);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		String RMsg = new String(dp.getData(),0,dp.getLength());
		for(String aIp:allIp) {
			if(isSame(aIp,RMsg)) {
				ip = aIp;
			}
		}
		gf.localIp = ip;
		gf.anotherIp = RMsg;
		System.out.println("个人端得到对方IP："+RMsg);
		try {
			dpS = new DatagramPacket(ip.getBytes(),ip.length(),InetAddress.getByName(RMsg),1612);
			ds.send(dpS);
//			System.out.println("成功发送了本机IP："+ip);
		} catch (UnknownHostException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		gf.isAir = false;
		gf.gameMap.setMap(gf.mapIndex);
		gf.connected = true;
		dsR.close();
		gf.localPort = 1611;
		new Thread(new RecThread(gf)).start();
	}
	
	
	//判断本线程获取的IP是否与得到的广播端IP在同一局域网
	private static boolean isSame(String first,String second) {
		int num = 0;
		char[] fir = first.toCharArray();
		char[] sec = second.toCharArray();
		for(int i=0;num<3;i++) {
			if(fir[i] == sec[i]) {
				if(fir[i] == '.') {
					num++;
				}
			} else return false;
		}
		return true;
	}
}
