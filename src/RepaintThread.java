import java.awt.Frame;

public class RepaintThread extends Thread {
	Frame f = null;
	public boolean runningFlag = true;
	RepaintThread(Frame f) {
		this.f = f;
	}
	@Override
	public void run() {
		// TODO �Զ����ɵķ������
		while (runningFlag) {
			f.repaint();
			try {
				Thread.sleep(10);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	

}
