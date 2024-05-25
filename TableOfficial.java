import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TableOfficial extends JFrame implements ActionListener {
    private JPanel sideBar, forms;
    private JButton[] btns = new JButton[3];
    private JTable table;

    //May gn add ko d nga addButton function saylo na lng ni guro sa constants nga method para magamit kag uniform tanan Line 173

    // Table Constants
    private final int SP_TABLE_STARTING_X_POS = 10;
    private final int SP_TABLE_STARTING_Y_POS = 70;
    private final int SP_TABLE_WIDTH = 790;
    private final int SP_TABLE_HEIGHT = 405;

    // Button Constants
    private final int BUTTON_WIDTH_1 = 150;
    private final int BUTTON_HEIGHT_1 = 30;
    private final int GAME_REPORT_BTN_X = 450;
    private final int GAME_REPORT_BTN_Y = 500;
    private final int PLAYER_STATS_BTN_X = 610;
    private final int PLAYER_STATS_BTN_Y = 500;

    //Table Title
    private final int TABLE_TITLE_Y = 20;
    private final int TABLE_TITLE_HEIGHT = 40;
    private final int TABLE_TITLE_SIZE = 30;

    public TableOfficial() {
        Constants.setCustomFont();
        initComponents();
        setDashboard();
        setTitle("Table Official Dashboard");
        setSize(Constants.WIDTH + 200, Constants.HEIGHT + 100);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btns[btns.length - 1]) {
            new MessageBox("Thanks for using the system!", 1);
            dispose();
            new Login();
        }

        // Single Round Robin Button
        if (ae.getSource() == btns[0]) {
            System.out.println("Single Button Pressed");

            // Sample Data for now - Specified file path 
            String[] columnNames = {"Column 1", "Column 2", "Column 3"};
            String[][] data = {
                    {"Row 1, Col 1", "Row 1, Col 2", "Row 1, Col 3"},
                    {"Row 2, Col 1", "Row 2, Col 2", "Row 2, Col 3"}
            };

            table = new JTable(data, columnNames);
            JScrollPane sp = new JScrollPane(table);

            sp.setBounds(SP_TABLE_STARTING_X_POS, SP_TABLE_STARTING_Y_POS, SP_TABLE_WIDTH, SP_TABLE_HEIGHT);

            //Clear Previous Components
            forms.removeAll();

            JLabel tableTitle = new JLabel("SINGLE ROUND ROBIN - FORMAT");
            tableTitle.setBounds(SP_TABLE_STARTING_X_POS, TABLE_TITLE_Y, SP_TABLE_WIDTH, TABLE_TITLE_HEIGHT);
            tableTitle.setFont(Constants.customFonts[0].deriveFont(Font.BOLD, TABLE_TITLE_SIZE));
            tableTitle.setForeground(Color.decode(Constants.CUSTOM_COLORS[3]));
            forms.add(tableTitle); 

            // Buttons
            addButton("Game Report", GAME_REPORT_BTN_X, GAME_REPORT_BTN_Y, BUTTON_WIDTH_1, BUTTON_HEIGHT_1);
            addButton("Player Stats", PLAYER_STATS_BTN_X, PLAYER_STATS_BTN_Y, BUTTON_WIDTH_1, BUTTON_HEIGHT_1);

            forms.add(sp);
            forms.revalidate();
            forms.repaint();
        }

        // Single Elimination Button
        if (ae.getSource() == btns[1]) {
            System.out.println("Single Elimination Button Pressed");
        
            // Sample Data for Single Elimination
            String[] columnNames = {"Column A", "Column B", "Column C"};
            String[][] data = {
                    {"Check Single ELim", "Row 1, Col B", "Row 1, Col C"},
                    {"Row 2, Col A", "Row 2, Col B", "Check single Elim"}
            };
        
            table = new JTable(data, columnNames);
            JScrollPane sp = new JScrollPane(table);
        
            sp.setBounds(SP_TABLE_STARTING_X_POS, SP_TABLE_STARTING_Y_POS, SP_TABLE_WIDTH, SP_TABLE_HEIGHT);
        
            // Clear Previous Components
            forms.removeAll();

            JLabel tableTitle = new JLabel("SINGLE ELIMINATION - FORMAT");
            tableTitle.setBounds(SP_TABLE_STARTING_X_POS, TABLE_TITLE_Y, SP_TABLE_WIDTH, TABLE_TITLE_HEIGHT);
            tableTitle.setFont(Constants.customFonts[0].deriveFont(Font.BOLD, TABLE_TITLE_SIZE));
            tableTitle.setForeground(Color.decode(Constants.CUSTOM_COLORS[3]));
            forms.add(tableTitle); 

            // Buttons
            addButton("Game Report", GAME_REPORT_BTN_X, GAME_REPORT_BTN_Y, BUTTON_WIDTH_1, BUTTON_HEIGHT_1);
            addButton("Player Stats", PLAYER_STATS_BTN_X, PLAYER_STATS_BTN_Y, BUTTON_WIDTH_1, BUTTON_HEIGHT_1);

            forms.add(sp);
            forms.revalidate();
            forms.repaint();
        }
    }

    private void initComponents() {
        sideBar = new JPanel();
        forms = new JPanel();
        for (int i = 0; i < btns.length; i++) {
            btns[i] = new JButton(); 
        }
    }

    private void setDashboard() {
        sideBar.setLayout(null);
        sideBar.setBounds(0, 0, 270, Constants.HEIGHT + 100);
        sideBar.setBackground(Color.decode(Constants.CUSTOM_COLORS[1]));

        forms.setLayout(null);
        forms.setBounds(270, 0, Constants.WIDTH, Constants.HEIGHT + 100);
        forms.setBackground(Color.decode(Constants.CUSTOM_COLORS[0]));

        initButtons();

        this.add(sideBar);
        this.add(forms);
    }

    private void initButtons() {
        Font customFont = Constants.customFonts[0];
        String fontStyle = customFont.getName() + ", " + customFont.getStyle();
        String fontSize = "30";
        String[] btnLabel = {
                "<html><body style='font-family: \"" + fontStyle + "\"; font-size: " + fontSize + "pt; text-align: center;'>SINGLE<br>ROUND ROBIN</body></html>",
                "<html><body style='font-family: \"" + fontStyle + "\"; font-size: " + fontSize + "pt; text-align: center;'>SINGLE<br>ELIMINATION</body></html>",
                "LOGOUT"
        };

        for (int i = 0; i < btns.length; i++) {
            btns[i].setText(btnLabel[i]);
            btns[i].setFont(customFont.deriveFont(30f));
            btns[i].setBorderPainted(false);
            btns[i].setFocusPainted(false);
            btns[i].addActionListener(this);
            if (i == btns.length - 1) {
                btns[i].setBounds(35, 480, 200, 50);
                btns[i].setBackground(Color.decode(Constants.CUSTOM_COLORS[2]));
                btns[i].setForeground(Color.decode(Constants.CUSTOM_COLORS[4]));
            } else {
                int yPos = 180 + (120 * i);
                btns[i].setBounds(10, yPos, 250, 70); // Increased height to accommodate two lines of text
                btns[i].setBackground(Color.decode(Constants.CUSTOM_COLORS[1]));
                btns[i].setForeground(Color.decode(Constants.CUSTOM_COLORS[3]));
            }
            sideBar.add(btns[i]);
        }
    }

    private void addButton(String label, int xPos, int yPos, int btnWidth, int btnHeight) {
        JButton button = new JButton(label);
        button.setBounds(xPos, yPos, btnWidth, btnHeight);
        button.setBackground(Color.decode(Constants.CUSTOM_COLORS[2]));
        button.setForeground(Color.decode(Constants.CUSTOM_COLORS[4]));
        button.setFont(Constants.customFonts[0].deriveFont(16f));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(this);
        forms.add(button);
    }

    public static void main(String[] args) {
        new TableOfficial();
    }
}