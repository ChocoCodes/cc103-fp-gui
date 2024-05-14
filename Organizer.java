import javax.swing.*;
import java.awt.event.*;

public class Organizer extends JFrame implements ActionListener {
    private FileOperations fileOp;
    private JLabel label;

    public Organizer() {
        setTitle("Organizer Dashboard");
        setSize(Constants.WIDTH, Constants.HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == label) {

        }
    }
}
