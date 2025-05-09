import java.net.InetAddress;

public class ClientInfo {
	private InetAddress address;
	private int port;

	public ClientInfo(InetAddress address, int port) {
		this.address = address;
		this.port = port;
	}

	public InetAddress getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ClientInfo))
			return false;
		ClientInfo other = (ClientInfo) obj;
		return address.equals(other.address) && port == other.port;
	}

	@Override
	public String toString() {
		return address.getHostAddress() + ":" + port;
	}
}