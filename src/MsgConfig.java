import javax.swing.*;

/**
 * MsgConfig is a utility class that holds constants and helper methods for
 * configuring the UDP messaging client and server.
 */
public class MsgConfig {
	/** Default port used if no input is provided by the user. */
	public static final int DEFAULT_PORT = 7777;

	/** Maximum allowed size of a UDP packet in bytes. */
	public static final int MAX_PACKET_SIZE = 4096;

	/** Command string sent when a client joins the chat. */
	public static final String CMD_JOIN = "JOIN";

	/** Command string sent when a client leaves the chat. */
	public static final String CMD_LEAVE = "LEAVE";

	/**
	 * Prompts the user to enter a port number using a dialog box. If the input is
	 * invalid or canceled, the default port is returned.
	 *
	 * @param parent the parent JFrame (can be null)
	 * @param prompt the message to display in the input dialog
	 * @return the parsed port number, or DEFAULT_PORT if input is invalid
	 */
	public static int askPort(JFrame parent, String prompt) {
		try {
			String input = JOptionPane.showInputDialog(parent, prompt);
			if (input == null || input.trim().isEmpty()) {
				return DEFAULT_PORT;
			}
			return Integer.parseInt(input.trim());
		} catch (Exception e) {
			return DEFAULT_PORT;
		}
	}
}
