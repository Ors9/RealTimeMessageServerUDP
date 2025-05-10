import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * MsgClientGui is the graphical interface for the UDP-based messaging client.
 * It provides text input and control buttons for joining, leaving, sending
 * messages, and clearing chat. It interacts with the server using
 * DatagramSocket and displays incoming messages via MsgClientConnection.
 */
public class MsgClientGui extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField inputField; // Input field for typing messages
	private JTextArea chatArea; // Area to display chat messages
	private JButton sendButton, joinButton, leaveButton, clearButton; // Action buttons
	private DatagramSocket socket; // UDP socket for sending messages
	private InetAddress serverAddress; // Server IP address
	private int serverPort; // Server port

	/**
	 * Constructs the GUI and initializes all components and event handlers.
	 *
	 * @param socket        the UDP socket to use for communication
	 * @param serverAddress the server's IP address
	 * @param serverPort    the server's port number
	 */
	public MsgClientGui(DatagramSocket socket, InetAddress serverAddress, int serverPort) {
		this.socket = socket;
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;

		setTitle("Msg Client");
		setSize(500, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		// Setup chat area
		chatArea = new JTextArea();
		chatArea.setEditable(false);
		add(new JScrollPane(chatArea), BorderLayout.CENTER);

		// Setup input field and buttons
		inputField = new JTextField();
		sendButton = new JButton("Send");
		joinButton = new JButton("Join");
		leaveButton = new JButton("Leave");
		clearButton = new JButton("Clear");

		// Layout for bottom panel with input and buttons
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.add(inputField, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(sendButton);
		buttonPanel.add(joinButton);
		buttonPanel.add(leaveButton);
		buttonPanel.add(clearButton);
		bottomPanel.add(buttonPanel, BorderLayout.EAST);

		add(bottomPanel, BorderLayout.SOUTH);

		// Button actions
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = inputField.getText().trim();
				if (!text.isEmpty()) {
					send(text);
					inputField.setText("");
				}
			}
		});

		joinButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				send(MsgConfig.CMD_JOIN);
			}
		});

		leaveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				send(MsgConfig.CMD_LEAVE);
			}
		});

		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chatArea.setText("");
			}
		});

		// Start listening for messages from server
		new MsgClientConnection(socket, this).start();
		setVisible(true);
	}

	/**
	 * Sends a message to the server.
	 *
	 * @param message the text message to send
	 */
	public void send(String message) {
		try {
			byte[] buffer = message.getBytes();
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, serverPort);
			socket.send(packet);
		} catch (Exception e) {
			appendMessage("❌ Failed to send message");
		}
	}

	/**
	 * Appends a message to the chat area with a timestamp.
	 *
	 * @param msg the message to display
	 */
	public void appendMessage(String msg) {
		String timestamp = new SimpleDateFormat("[HH:mm:ss]").format(new Date());
		chatArea.append(timestamp + " " + msg + "\n");
	}
}
