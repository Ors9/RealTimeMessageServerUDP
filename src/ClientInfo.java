import java.net.InetAddress;

/**
 * Represents a client's network information, including IP address and port.
 * Used for identifying and managing clients in a network-based application.
 */
public class ClientInfo {
	private InetAddress address;
	private int port;

	/**
	 * Constructs a new ClientInfo object with the given address and port.
	 *
	 * @param address the IP address of the client
	 * @param port    the port number the client is using
	 */
	public ClientInfo(InetAddress address, int port) {
		this.address = address;
		this.port = port;
	}

	/**
	 * Returns the IP address of the client.
	 *
	 * @return the InetAddress of the client
	 */
	public InetAddress getAddress() {
		return address;
	}

	/**
	 * Returns the port number of the client.
	 *
	 * @return the port number
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Checks whether this ClientInfo is equal to another object. Equality is based
	 * on the same IP address and port number.
	 *
	 * @param obj the object to compare
	 * @return true if the object is a ClientInfo with the same address and port,
	 *         false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ClientInfo))
			return false;
		ClientInfo other = (ClientInfo) obj;
		return address.equals(other.address) && port == other.port;
	}

	/**
	 * Returns a string representation of the client in the format "IP:Port".
	 *
	 * @return a string with the client's address and port
	 */
	@Override
	public String toString() {
		return address.getHostAddress() + ":" + port;
	}
}