import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Organizer extends JFrame implements ActionListener {
    private FileOperations fileOp;
    private CardLayout cdl;
    private JPanel sideBar;
    private JPanel[] forms = new JPanel[4];
    private JButton[] btns = new JButton[4];
    private JLabel[] adminLabels = new JLabel[2];
    private Image adminProfile;

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
        for(int i = 0; i < btns.length - 1; i++) {
            if(ae.getSource() == btns[i]) {
                cdl.show(forms[0], Integer.toString(i + 1));
            }
        }
    }

    private void initComponents() {
        String[] btnLabel = {"HOME", "ADD TEAMS", "ADD PLAYERS", "LOGOUT"};
        adminProfile = new ImageIcon(Constants.ASSET_DIR + "caloocan.jpg").getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        cdl = new CardLayout();
        sideBar = new JPanel(); 
        for (int i = 0; i < btns.length; i++) {
            btns[i] = new JButton(btnLabel[i]);
        }
        for (int i = 0; i < adminLabels.length; i++) {
            adminLabels[i] = (i == 1) ? new JLabel("ADMIN") : new JLabel();
        }
    }

    private void setDashboard() {
        Color sideBarColor = Color.decode(Constants.CUSTOM_COLORS[1]),
        formsColor = Color.decode(Constants.CUSTOM_COLORS[0]);

        sideBar.setBounds(0, 0, 270, Constants.HEIGHT + 100);
        sideBar.setBackground(sideBarColor);
        sideBar.setLayout(null);

        forms[0] = new JPanel(cdl);
        forms[1] = new JPanel();
        forms[2] = setTeamForm(formsColor);


        for(int i = 0; i < forms.length; i++) {
            // Color tmpColor = (i % 2 == 0) ? Color.RED : Color.GREEN;
            // forms[i].setBounds(270, 0, Constants.WIDTH, Constants.HEIGHT + 100);
            // forms[i].setBackground(tmpColor);
            if(i == 0) {
                continue;
            } else {
                forms[0].add(forms[i], Integer.toString(i));
            }
        }

        for(int i = 0; i < btns.length; i++) {
            if(i == btns.length - 1) {
                btns[i].setBounds(35, 480, 200, 50);
                btns[i].setBackground(Color.decode(Constants.CUSTOM_COLORS[2]));
                btns[i].setForeground(Color.decode(Constants.CUSTOM_COLORS[4]));
            } else {
                int yPos = 200 + (70 * i);
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

        adminLabels[0].setBounds(85, 30, 100, 100);
        adminLabels[0].setIcon(new ImageIcon(adminProfile));
        sideBar.add(adminLabels[0]);
        adminLabels[1].setBounds(85, 130, 250, 50);
        adminLabels[1].setFont(Constants.customFonts[0].deriveFont(30f));
        adminLabels[1].setForeground(Color.decode(Constants.CUSTOM_COLORS[3]));
        sideBar.add(adminLabels[1]);

        cdl.show(forms[0], "1");
        this.add(sideBar);
        this.add(forms[0]);
    }

    private JPanel setTeamForm(Color color) {
        JPanel tForm = new JPanel();
        String[] displayLabels = {"Team Details", "Please enter team information below. No duplicate teams allowed.", "Team Name", "Player Counts"};
        JLabel[] labels = new JLabel[4];
        JTextField[] inputFields = new JTextField[2];

        tForm.setLayout(null);
        tForm.setBounds(270, 0, Constants.WIDTH, Constants.HEIGHT + 100);
        tForm.setBackground(color);
        for(int i = 0; i < inputFields.length; i++) {
            inputFields[i].setBounds(0, 0, 300, 40);
            tForm.add(inputFields[i]);
        }
        return tForm;
    }

}
