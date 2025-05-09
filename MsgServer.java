import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class MsgServer {
	private DatagramSocket socket;
	private ArrayList<ClientInfo> clientList = new ArrayList<>();

	public MsgServer(int port) throws Exception {
		socket = new DatagramSocket(port);
		System.out.println("📡 Server running on port " + port);
		startListening();
	}

	private void startListening() throws Exception {
		byte[] buffer = new byte[MsgConfig.MAX_PACKET_SIZE];

		while (true) {
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			socket.receive(packet);
			String content = new String(packet.getData(), 0, packet.getLength());
			ClientInfo sender = new ClientInfo(packet.getAddress(), packet.getPort());

			if (content.equals(MsgConfig.CMD_JOIN)) {
				if (!clientList.contains(sender)) {
					clientList.add(sender);
					System.out.println("➕ New client: " + sender);
				}
			} else if (content.equals(MsgConfig.CMD_LEAVE)) {
				clientList.remove(sender);
				System.out.println("➖ Client left: " + sender);
			} else {
				broadcast(content, sender);
			}
		}
	}

	private void broadcast(String message, ClientInfo sender) throws Exception {
		byte[] data = message.getBytes();
		for (ClientInfo c : clientList) {
			if (!c.equals(sender)) {
				DatagramPacket packet = new DatagramPacket(data, data.length, c.getAddress(), c.getPort());
				socket.send(packet);
			}
		}
		System.out.println("📨 Message sent from " + sender + ": " + message);
	}

	public static void main(String[] args) {
		try {
			int port = MsgConfig.askPort(null, "Enter port for MsgServer:");
			new MsgServerGui(port); // מפעיל את ממשק הגרפי
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}