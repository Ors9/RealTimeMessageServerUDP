import javax.swing.*;

public class MsgConfig {
    public static final int DEFAULT_PORT = 7000; // ברירת מחדל אם המשתמש לא מזין פורט
    public static final int MAX_PACKET_SIZE = 4096; // גודל מקסימלי של חבילה
    public static final String CMD_JOIN = "JOIN";   // מחרוזת הצטרפות
    public static final String CMD_LEAVE = "LEAVE"; // מחרוזת עזיבה


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
