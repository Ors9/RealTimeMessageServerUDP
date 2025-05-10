import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * MsgClientConnection is a thread that listens for incoming UDP messages from
 * the server. Upon receiving a message, it appends it to the GUI along with a
 * timestamp.
 */
public class MsgClientConnection extends Thread {
	private DatagramSocket socket; // UDP socket used to receive messages
	private MsgClientGui gui; // Reference to the GUI to display incoming messages

	/**
	 * Constructs a MsgClientConnection with the given socket and GUI.
	 *
	 * @param socket the UDP socket connected to the server
	 * @param gui    the GUI interface for displaying received messages
	 */
	public MsgClientConnection(DatagramSocket socket, MsgClientGui gui) {
		this.socket = socket;
		this.gui = gui;
	}

	/**
	 * The main loop of the thread. Continuously listens for UDP packets. On
	 * receiving a message, it adds a timestamp and displays it in the GUI. If an
	 * error occurs, it displays an error message and exits the loop.
	 */
	public void run() {
		byte[] buffer = new byte[MsgConfig.MAX_PACKET_SIZE];

		while (true) {
			try {
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet); // Wait for a packet from the server
				String msg = new String(packet.getData(), 0, packet.getLength());

				String timestamp = new SimpleDateFormat("[HH:mm:ss]").format(new Date());
				gui.appendMessage(timestamp + " " + msg);
			} catch (Exception e) {
				gui.appendMessage("❌ Error receiving message");
				break;
			}
		}
	}
}
