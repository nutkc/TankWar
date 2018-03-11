import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPAirCon implements Runnable {
	public static int count = 0;
	public int index;
	private GameFrame gf;
	private UDPAir ua;
	private DatagramSocket dsR;
	private String ip;
	public UDPAirCon(GameFrame gf,UDPAir ua,String ip) {
		// TODO 自动生成的构造函数存根
		this.gf = gf;
		this.ua = ua;
		this.ip =ip;
		try {
			dsR = new DatagramSocket(1612,InetAddress.getByName(ip));
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
//		System.out.println("UDPAirCon创建"+ip);
		index = count++;

	}

	@Override
	public void run() {
		// TODO 自动生成的方法存根
		byte[] buf = new byte[1024];
		DatagramPacket dpR = new DatagramPacket(buf,buf.length);
		try {
//			System.out.println(ip+"等待接收");
			dsR.receive(dpR);
//			System.out.println("接收到一条信息");
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		String RMsg = new String(dpR.getData(),0,dpR.getLength());
		gf.anotherIp = RMsg;
		System.out.println("广播端的得到对方IP："+RMsg);
		gf.connected = true;
		ua.send = false;
		gf.localIp = ip;
	}


}
