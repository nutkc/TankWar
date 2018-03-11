import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;

public class UDPAir implements Runnable {
	private GameFrame gf;
	private String ip;
	public boolean send = true;
	private DatagramPacket dp;
	private static DatagramSocket ds;
	private ArrayList<String> allIp;
	private int n = 0;
	public ArrayList<Thread> airCons = new ArrayList<Thread>();
	public UDPAir(GameFrame gf) {
		// TODO 自动生成的构造函数存根
		this.gf = gf;
		try {
			ds = new DatagramSocket();
		} catch (SocketException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		//多网卡获取IP
		allIp = getAllIp();
		for(String aIp:allIp) {
			airCons.add(new Thread(new UDPAirCon(gf,this,aIp)));
		}
		for(Thread thisThread:airCons) {
			thisThread.start();
		}
	}



	@Override
	public void run() {
		// TODO 自动生成的方法存根
		ArrayList<DatagramPacket> allDp = new ArrayList<DatagramPacket>();
		for(String ip:allIp) {
			byte[] ipToSend = ip.getBytes();
			try {
				allDp.add(new DatagramPacket(ipToSend,ipToSend.length,InetAddress.getByName(toAir(ip)),1611));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		while(send) {
			try {
				for(DatagramPacket dp:allDp) {
					ds.send(dp);
//					System.out.println("第"+n+++"次发送广播");
				}
			} catch (IOException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		//结束所有UDPAirCon线程
		for(Thread thisThread:airCons) {
			thisThread.stop();
//			System.out.println("结束了一条UDPAirCon线程");
		}
		ds.close();
		gf.isAir = true;
		gf.gameMap.setMap(gf.mapIndex);
		gf.localPort = 1612;
		new Thread(new RecThread(gf)).start();
	}


	//返回一个ArrayList<String> 存放本机所有IP
	static ArrayList<String> getAllIp() {
		ArrayList<String> allIp = new ArrayList<String>();
		try {
			Enumeration<NetworkInterface> interfaces=null;
			interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {  
				NetworkInterface ni = interfaces.nextElement(); 
				Enumeration<InetAddress> addresss = ni.getInetAddresses();
				while(addresss.hasMoreElements())
				{
					InetAddress nextElement = addresss.nextElement();
					String hostAddress = nextElement.getHostAddress();
					if(isIp(hostAddress)) {
						allIp.add(hostAddress);
					}
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allIp;
	}
	//判断一个字符串是否符合Ip格式 不判断每个字节组是否小于255
	static boolean isIp(String Address) {
		char[] buf = Address.toCharArray();
		int num = 0;
		for(int i=0;i<buf.length;i++) {
			if(buf[i] == '.') {
				num++;
			}
		}
		if(num == 3) {
			return true;
		}
		return false;
	}


	//借由本地IP获得255广播IP
	private static String toAir(String ip) {
		int count = 0,index = 0;
		char[] buf = ip.toCharArray();
		char[] publicIp = new char[15];
		while(count<3) {
			publicIp[index] = buf[index];
			if(buf[index] =='.') {
				count++;
			}
			index++;
		}
		return new String(publicIp,0,index)+"255";
	}
}
