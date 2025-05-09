import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MsgClientGui extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField inputField;
	private JTextArea chatArea;
	private JButton sendButton, joinButton, leaveButton, clearButton;
	private DatagramSocket socket;
	private InetAddress serverAddress;
	private int serverPort;

	public MsgClientGui(DatagramSocket socket, InetAddress serverAddress, int serverPort) {
		this.socket = socket;
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;

		setTitle("Msg Client");
		setSize(500, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		chatArea = new JTextArea();
		chatArea.setEditable(false);
		add(new JScrollPane(chatArea), BorderLayout.CENTER);

		inputField = new JTextField();
		sendButton = new JButton("Send");
		joinButton = new JButton("Join");
		leaveButton = new JButton("Leave");
		clearButton = new JButton("Clear");

		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.add(inputField, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(sendButton);
		buttonPanel.add(joinButton);
		buttonPanel.add(leaveButton);
		buttonPanel.add(clearButton);
		bottomPanel.add(buttonPanel, BorderLayout.EAST);

		add(bottomPanel, BorderLayout.SOUTH);

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

		new MsgClientConnection(socket, this).start();
		setVisible(true);
	}

	public void send(String message) {
		try {
			byte[] buffer = message.getBytes();
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, serverPort);
			socket.send(packet);
		} catch (Exception e) {
			appendMessage("❌ Failed to send message");
		}
	}

	public void appendMessage(String msg) {
		String timestamp = new SimpleDateFormat("[HH:mm:ss]").format(new Date());
		chatArea.append(timestamp + " " + msg + "\n");
	}
}
