import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.swing.JOptionPane;

/**
 * The MsgClient class initializes a UDP client that connects to a server using
 * a specified IP and port. It launches a graphical user interface
 * (MsgClientGui) for sending and receiving messages over the network.
 */
public class MsgClient {
	public static void main(String[] args) throws Exception {
		// Prompt user to enter the server port
		int serverPort = MsgConfig.askPort(null, "Enter server port:");

		// Prompt user to enter the server IP (defaults to localhost if empty or
		// canceled)
		String serverIP = JOptionPane.showInputDialog(null, "Enter server IP:", "127.0.0.1");
		if (serverIP == null || serverIP.isEmpty()) {
			serverIP = "127.0.0.1";
		}

		// Resolve IP address and create a UDP socket
		InetAddress serverAddr = InetAddress.getByName(serverIP);
		DatagramSocket socket = new DatagramSocket();

		// Launch the GUI for client interaction
		new MsgClientGui(socket, serverAddr, serverPort);
	}
}
