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
		// TODO �Զ����ɵĹ��캯�����
		this.gf = gf;
		this.ua = ua;
		this.ip =ip;
		try {
			dsR = new DatagramSocket(1612,InetAddress.getByName(ip));
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
//		System.out.println("UDPAirCon����"+ip);
		index = count++;

	}

	@Override
	public void run() {
		// TODO �Զ����ɵķ������
		byte[] buf = new byte[1024];
		DatagramPacket dpR = new DatagramPacket(buf,buf.length);
		try {
//			System.out.println(ip+"�ȴ�����");
			dsR.receive(dpR);
//			System.out.println("���յ�һ����Ϣ");
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		String RMsg = new String(dpR.getData(),0,dpR.getLength());
		gf.anotherIp = RMsg;
		System.out.println("�㲥�˵ĵõ��Է�IP��"+RMsg);
		gf.connected = true;
		ua.send = false;
		gf.localIp = ip;
	}


}
