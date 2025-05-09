import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MsgClientConnection extends Thread {
    private DatagramSocket socket;
    private MsgClientGui gui;

    public MsgClientConnection(DatagramSocket socket, MsgClientGui gui) {
        this.socket = socket;
        this.gui = gui;
    }

    public void run() {
        byte[] buffer = new byte[MsgConfig.MAX_PACKET_SIZE];

        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
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
