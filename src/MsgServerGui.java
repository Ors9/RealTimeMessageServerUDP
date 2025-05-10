import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

/**
 * MsgServerGui is a simple UDP-based server with a graphical interface
 * for handling client messages in a chat-like environment.
 * It allows multiple clients to join, send messages, and leave.
 */
public class MsgServerGui extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextArea logArea;   // Text area for displaying server logs
	private JTextField inputField;  // Field for typing messages to clients
	private JButton sendButton;  // Button to send a server message
	private DatagramSocket socket;    // UDP socket for communication
	private ArrayList<ClientInfo> clients = new ArrayList<>();  // List of currently connected clients

	

	/**
	 * Constructs the server GUI and starts listening for UDP messages.
	 *
	 * @param port The port on which the server will listen
	 * @throws Exception If socket creation fails
	 */
	public MsgServerGui(int port) throws Exception {
		setTitle("Message Server");
		setSize(500, 450);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		logArea = new JTextArea();
		logArea.setEditable(false);
		add(new JScrollPane(logArea), BorderLayout.CENTER);

		inputField = new JTextField();
		sendButton = new JButton("Send");

		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.add(inputField, BorderLayout.CENTER);
		bottomPanel.add(sendButton, BorderLayout.EAST);
		add(bottomPanel, BorderLayout.SOUTH);

		// Send button listener to broadcast messages from the server
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = inputField.getText().trim();
				if (!message.isEmpty()) {
					try {
						broadcast(message, null); // null = server is the sender
						log("📤 You (Server): " + message);
						inputField.setText("");
					} catch (Exception ex) {
						log("❌ Failed to send from server.");
					}
				}
			}
		});

		setVisible(true);

		socket = new DatagramSocket(port);
		log("✅ Server started on port " + port);

		// Start the server thread for handling incoming messages
		Thread serverThread = new Thread(new Runnable() {
			public void run() {
				runServer();
			}
		});
		serverThread.start();
	}

	
	/**
	 * Continuously listens for incoming UDP packets and processes them.
	 * Handles JOIN/LEAVE commands and message broadcasting.
	 */
	private void runServer() {
		byte[] buffer = new byte[MsgConfig.MAX_PACKET_SIZE];

		while (true) {
			try {
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);
				String message = new String(packet.getData(), 0, packet.getLength());
				ClientInfo sender = new ClientInfo(packet.getAddress(), packet.getPort());

				if (message.equals(MsgConfig.CMD_JOIN)) {
					if (!clients.contains(sender)) {
						clients.add(sender);
						log("🟢 Joined: " + sender);
					}
				} else if (message.equals(MsgConfig.CMD_LEAVE)) {
					clients.remove(sender);
					log("🔴 Left: " + sender);
				} else {
					log("📩 " + sender + ": " + message);
					broadcast(message, sender);
				}
			} catch (Exception e) {
				log("❌ Error: " + e.getMessage());
			}
		}
	}

	/**
	 * Sends a message to all connected clients except the sender.
	 *
	 * @param message The message to send
	 * @param sender  The client who sent the message (null if from server)
	 * @throws Exception If a packet fails to send
	 */
	private void broadcast(String message, ClientInfo sender) throws Exception {
		byte[] data = message.getBytes();
		for (ClientInfo client : clients) {
			if (sender == null || !client.equals(sender)) {
				DatagramPacket packet = new DatagramPacket(data, data.length, client.getAddress(), client.getPort());
				socket.send(packet);
			}
		}
	}

	/**
	 * Appends a message to the log area in the GUI.
	 *
	 * @param msg The message to display
	 */
	private void log(String msg) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				logArea.append(msg + "\\n");
			}
		});
	}

	/**
	 * Entry point to start the server. Asks user for the port number.
	 *
	 * @param args Not used
	 * @throws Exception If an error occurs during server setup
	 */
	public static void main(String[] args) throws Exception {
		int port = MsgConfig.askPort(null, "Enter server port:");
		new MsgServerGui(port);
	}
}
