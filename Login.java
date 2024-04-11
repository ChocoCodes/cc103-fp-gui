import javax.swing.*;
import java.lang.Exception;

public class Login {
    // private static final String[] ORGANIZER_LOGIN = {"organizer", "admin123"};
    // private static final String[] TABLE_OFFICIAL_LOGIN = {"tableofficial", "table123"};
    public static void main(String[] args) {
        try {
            // Run the program on the Event Dispatch Thread (EDT)
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    GUIBuilder loginWindow = new GUIBuilder();
                    loginWindow.displayWindow();
                }
            });
        } catch (Exception e) {
            System.err.println("An error occured while the program is running.");
        }
    }
}
