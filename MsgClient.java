import java.net.DatagramSocket;
import java.net.InetAddress;

public class MsgClient {
	public static void main(String[] args) throws Exception {
		int serverPort = MsgConfig.askPort(null, "Enter server port:");
		String serverIP = javax.swing.JOptionPane.showInputDialog(null, "Enter server IP:", "127.0.0.1");
		if (serverIP == null || serverIP.isEmpty()) {
			serverIP = "127.0.0.1";
		}
		InetAddress serverAddr = InetAddress.getByName(serverIP);
		DatagramSocket socket = new DatagramSocket();

		new MsgClientGui(socket, serverAddr, serverPort);
	}
}
