import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Organizer extends JFrame implements ActionListener {
    private FileOperations fileOp;
    private CardLayout cdl;
    private JPanel sideBar, forms;
    private JButton[] btns = new JButton[4];
    private Color sideBarColor, formsColor;

    public Organizer() {
        initComponents();
        Constants.setCustomFont();
        setDashboard();
        setTitle("Organizer Dashboard");
        setSize(Constants.WIDTH + 200, Constants.HEIGHT + 100);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == btns[btns.length - 1]) {
            new MessageBox("Thanks for using the system!", 1);
            dispose();
            new Login();
        }
    }

    private void initComponents() {
        String[] btnLabel = {"HOME", "ADD TEAMS", "ADD PLAYERS", "LOGOUT"};
        cdl = new CardLayout();
        sideBar = new JPanel();
        forms = new JPanel();   
        for(int i = 0; i < btns.length; i++) {
            btns[i] = new JButton(btnLabel[i]);

        }
    }

    private void setDashboard() {
        sideBarColor = Color.decode(Constants.CUSTOM_COLORS[1]);
        formsColor = Color.decode(Constants.CUSTOM_COLORS[0]);
        for(int i = 0; i < btns.length; i++) {
            if(i == btns.length - 1) {
                btns[i].setBounds(35, 480, 200, 50);
                btns[i].setBackground(Color.decode(Constants.CUSTOM_COLORS[2]));
                btns[i].setForeground(Color.decode(Constants.CUSTOM_COLORS[4]));
            } else {
                int yPos = 180 + (70 * i);
                btns[i].setBounds(10, yPos, 250, 50);
                btns[i].setBackground(sideBarColor);
                btns[i].setForeground(Color.decode(Constants.CUSTOM_COLORS[3]));
                
            }
            btns[i].setFont(Constants.customFonts[0].deriveFont(30f));
            btns[i].setBorderPainted(false);
            btns[i].setFocusPainted(false);
            btns[i].addActionListener(this);
            sideBar.add(btns[i]);
        }
        sideBar.setBounds(0, 0, 270, Constants.HEIGHT + 100);
        sideBar.setBackground(sideBarColor);
        sideBar.setLayout(null);
        forms.setBounds(270, 0, Constants.WIDTH, Constants.HEIGHT + 100);
        forms.setBackground(formsColor);
        this.add(sideBar);
        this.add(forms);
    }
}
