import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class TableOfficial extends JFrame implements ActionListener {
    private JPanel sideBar, forms;
    private JButton[] btns = new JButton[3];
    private JTable table;
    private Image tableOfficialProfile;
    private JLabel[] tableOfficialLabels = new JLabel[2];
    private static FileOperations fileOp = new FileOperations();

    // Table Constants
    private final int SP_TABLE_STARTING_X_POS = 12;
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
    private final int APPLY_BTN_X = 290;

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
            handleLogout();
        } else if (ae.getSource() == btns[0]) {
            setupSingleRoundRobinTable();
        } else if (ae.getSource() == btns[1]) {
            setupSingleEliminationTable();
        } else if (ae.getActionCommand().equals("Player Stats")) {
            handlePlayerStats();
        } else if (ae.getActionCommand().equals("Game Report")) {
            handleGameReport();
        }
    }

    private void handleLogout() {
        new MessageBox("Thanks for using the system!", 0);
        dispose();
        new Login();
    }

    private void setupSingleRoundRobinTable() {
        System.out.println("Single Round Robin Button Pressed");
        
        //TODO create read file from CSV
        // Sample Data for now
        String[] columnNames = {"Column 1", "Column 2", "Column 3"};
        String[][] data = {
            {"TEAM A", "TEAM B", "1"},
            {"TEAM C", "TEAM D", "2"}
        };
        
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };
    
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
    
        sp.setBounds(SP_TABLE_STARTING_X_POS, SP_TABLE_STARTING_Y_POS, SP_TABLE_WIDTH, SP_TABLE_HEIGHT);
    
        forms.removeAll(); 
    
        // Table Title
        JLabel tableTitle = new JLabel("SINGLE ROUND ROBIN - FORMAT");
        tableTitle.setBounds(SP_TABLE_STARTING_X_POS, TABLE_TITLE_Y, SP_TABLE_WIDTH, TABLE_TITLE_HEIGHT);
        tableTitle.setFont(Constants.customFonts[0].deriveFont(Font.BOLD, TABLE_TITLE_SIZE));
        tableTitle.setForeground(Color.decode(Constants.CUSTOM_COLORS[3]));
        forms.add(tableTitle);
    
        // Buttons
        addButton("Game Report", GAME_REPORT_BTN_X, GAME_REPORT_BTN_Y, BUTTON_WIDTH_1, BUTTON_HEIGHT_1);
        addButton("Player Stats", PLAYER_STATS_BTN_X, PLAYER_STATS_BTN_Y, BUTTON_WIDTH_1, BUTTON_HEIGHT_1);
    
        // Add table and refresh
        forms.add(sp);
        forms.revalidate();
        forms.repaint();
    }

    private void setupSingleEliminationTable() {
        System.out.println("Single Elimination Button Pressed");
        
        //TODO create read file from csv
        // Sample Data for now
        String[] columnNames = {"Column A", "Column B", "Column C"};
        String[][] data = {
            {"TEAM A", "TEAM B", "1"},
            {"TEAM C", "TEAM D", "2"}
        };
    
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };
    
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
    
        sp.setBounds(SP_TABLE_STARTING_X_POS, SP_TABLE_STARTING_Y_POS, SP_TABLE_WIDTH, SP_TABLE_HEIGHT);
    
        forms.removeAll(); 
    
        // Table Title
        JLabel tableTitle = new JLabel("SINGLE ELIMINATION - FORMAT");
        tableTitle.setBounds(SP_TABLE_STARTING_X_POS, TABLE_TITLE_Y, SP_TABLE_WIDTH, TABLE_TITLE_HEIGHT);
        tableTitle.setFont(Constants.customFonts[0].deriveFont(Font.BOLD, TABLE_TITLE_SIZE));
        tableTitle.setForeground(Color.decode(Constants.CUSTOM_COLORS[3]));
        forms.add(tableTitle);
    
        // Buttons
        addButton("Game Report", GAME_REPORT_BTN_X, GAME_REPORT_BTN_Y, BUTTON_WIDTH_1, BUTTON_HEIGHT_1);
        addButton("Player Stats", PLAYER_STATS_BTN_X, PLAYER_STATS_BTN_Y, BUTTON_WIDTH_1, BUTTON_HEIGHT_1);
    
        // Add table and refresh
        forms.add(sp);
        forms.revalidate();
        forms.repaint();
    }
    
    //DB Purposes for now to make sure the concept is working
    private void handlePlayerStats() {
        if (table != null) {
            int selectedRow = table.getSelectedRow();
            int selectedColumn = table.getSelectedColumn();
    
            if (selectedRow >= 0 && selectedColumn >= 0) {
                Object cellValue = table.getValueAt(selectedRow, selectedColumn);
                String cellValueString = String.valueOf(cellValue); // Convert to string
                //DB
                System.out.println("Selected Cell Value (Player Stats): " + cellValueString);
            } else {
                new MessageBox("No/Invalid Column Selected for Player Stats. Usage: Select A Team", 0);
            }
        }
    }

    private void handleGameReport() {
        if (table != null) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int columnCount = table.getColumnCount();
                String[] rowData = new String[columnCount];
    
                for (int i = 0; i < columnCount; i++) {
                    rowData[i] = String.valueOf(table.getValueAt(selectedRow, i));
                }
    
                forms.removeAll(); // Clear previous content
    
                // Constants for layout
                final int LABEL_WIDTH = 150;
                final int TEXT_FIELD_WIDTH = 130;
                final int LABEL_HEIGHT = 30;
                final int VERTICAL_GAP = 20;
                final int TOP_MARGIN = TABLE_TITLE_Y + TABLE_TITLE_HEIGHT + 30;
                final int FORM_WIDTH = Constants.WIDTH - 100;

                int LABEL_TEXT_FIELD_GAP = 50; 
                int TOTAL_USED_WIDTH = (2 * LABEL_WIDTH) + (2 * TEXT_FIELD_WIDTH) + LABEL_TEXT_FIELD_GAP; 
                int AVAILABLE_SPACE = FORM_WIDTH - TOTAL_USED_WIDTH;
                int LEFT_MARGIN = (AVAILABLE_SPACE / 2) - 30; 
                int COLUMN_GAP = LABEL_TEXT_FIELD_GAP; 

                int tableTitleX = LEFT_MARGIN; 
                JLabel tableTitle = new JLabel(rowData[0] + " VS. " + rowData[1]);
                tableTitle.setBounds(tableTitleX, TABLE_TITLE_Y, SP_TABLE_WIDTH, TABLE_TITLE_HEIGHT);
                tableTitle.setFont(Constants.customFonts[0].deriveFont(Font.BOLD, TABLE_TITLE_SIZE));
                tableTitle.setForeground(Color.decode(Constants.CUSTOM_COLORS[3]));
                forms.add(tableTitle);

                String[] statLabels = {
                    "Score for Q1", "Score for Q2", "Score for Q3", "Score for Q4",
                    "Rebounds", "Blocks", "Steals", "Assists"
                };

                String[] team1Stats = new String[statLabels.length];
                String[] team2Stats = new String[statLabels.length];

                for (int i = 0; i < statLabels.length; i++) {

                    int label1X = LEFT_MARGIN; 
                    JLabel label1 = new JLabel(statLabels[i]);
                    label1.setBounds(label1X, TOP_MARGIN + i * (LABEL_HEIGHT + VERTICAL_GAP), LABEL_WIDTH, LABEL_HEIGHT);
                    label1.setFont(new Font("Times New Roman", Font.BOLD, 18));
                    label1.setForeground(Color.decode(Constants.CUSTOM_COLORS[3]));

                    int textField1X = label1X + LABEL_WIDTH + LABEL_TEXT_FIELD_GAP - 30;
                    JTextField textField1 = new JTextField();
                    textField1.setBounds(textField1X, label1.getY(), TEXT_FIELD_WIDTH, LABEL_HEIGHT);
                    forms.add(label1);
                    forms.add(textField1);

                    int label2X = textField1X + TEXT_FIELD_WIDTH + COLUMN_GAP;
                    JLabel label2 = new JLabel(statLabels[i]);
                    label2.setBounds(label2X + 30, label1.getY(), LABEL_WIDTH, LABEL_HEIGHT);
                    label2.setFont(new Font("Times New Roman", Font.BOLD, 18));
                    label2.setForeground(Color.decode(Constants.CUSTOM_COLORS[3]));

                    int textField2X = label2X + LABEL_WIDTH + LABEL_TEXT_FIELD_GAP;
                    JTextField textField2 = new JTextField();
                    textField2.setBounds(textField2X, label1.getY(), TEXT_FIELD_WIDTH, LABEL_HEIGHT);
                    forms.add(label2);
                    forms.add(textField2);

                }
                            
    
                addButton("Game Report", GAME_REPORT_BTN_X, GAME_REPORT_BTN_Y, BUTTON_WIDTH_1, BUTTON_HEIGHT_1);
                addButton("Player Stats", PLAYER_STATS_BTN_X, PLAYER_STATS_BTN_Y, BUTTON_WIDTH_1, BUTTON_HEIGHT_1);
                JButton applyButton = addButton("Apply", APPLY_BTN_X, PLAYER_STATS_BTN_Y, BUTTON_WIDTH_1, BUTTON_HEIGHT_1);

                applyButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Component[] components = forms.getComponents();
                        int statIndex = 0;
                        boolean allFieldsFilled = true;
                        boolean isValidNumber = true; 
                
                        for (Component component : components) {
                            if (component instanceof JTextField) {
                                JTextField textField = (JTextField) component;
                                String inputValue = textField.getText();
                
                                if (inputValue.trim().isEmpty()) {
                                    allFieldsFilled = false;
                                    break; 
                                } else if (!fileOp.checkIfNumber(inputValue)) { 
                                    isValidNumber = false;
                                    break; 
                                }
                
                                try {
                                    int tempNum = Integer.parseInt(inputValue);
                                    if (tempNum < 0) {
                                        isValidNumber = false;
                                        break; 
                                    }
                                } catch (NumberFormatException nfex) {
                                    new MessageBox("ERROR: Input must be an integer.", 0);
                                    isValidNumber = false;
                                    break; 
                                }

                                if (statIndex % 2 == 0) {
                                    team1Stats[statIndex / 2] = inputValue;
                                } else {
                                    team2Stats[statIndex / 2] = inputValue;
                                }
                                statIndex++;
                            }
                        }
                
                        if (!allFieldsFilled) {
                            Arrays.fill(team1Stats, null);
                            Arrays.fill(team2Stats, null);
                            System.out.println("Missing input. Stats cleared.");
                            new MessageBox("Please fill in all fields.", 0);
                        } else if (!isValidNumber) {
                            Arrays.fill(team1Stats, null);
                            Arrays.fill(team2Stats, null);
                            System.out.println("Invalid input. Stats cleared.");
                            new MessageBox("Please enter valid integer numbers.", 0);
                        } else {
                            //DB
                            new MessageBox("Game Report saved to CSV successfully", 1);
                            saveGameReportToCSV(team1Stats, team2Stats);
                        }
                    }
                });
                
                forms.revalidate();
                forms.repaint();

            } else {
                new MessageBox("No/Invalid Row Selected for Game Report. Usage: Select A Team", 0);
            }
        }
    }
    
    public void saveGameReportToCSV (String[] team1, String[] team2) {
        //TODO: Once CSV is available create save method
        //Check if teams are filled
        System.out.println("Team 1 Stats: " + Arrays.toString(team1));
        System.out.println("Team 2 Stats: " + Arrays.toString(team2));

    }


    private void initComponents() {
        tableOfficialProfile = new ImageIcon(Constants.ASSET_DIR + "billboard.jpg").getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        // Initialize labels and buttons in separate loops
        for (int i = 0; i < tableOfficialLabels.length; i++) {
            tableOfficialLabels[i] = new JLabel(); // Create the JLabel
            if (i == 0) { 
                tableOfficialLabels[i].setBounds(85, 30, 100, 100);
                tableOfficialLabels[i].setIcon(new ImageIcon(tableOfficialProfile)); // Set the icon here
            } else {
                Font customFont = Constants.customFonts[0];
                String fontStyle = customFont.getName() + ", " + customFont.getStyle();
                String fontSize = "30";
                tableOfficialLabels[i].setText("<html><body style='font-family: \"" + fontStyle + "\"; font-size: " + fontSize + "pt; text-align: center;'>TABLE<br>OFFICIAL</body></html>"); 
                tableOfficialLabels[i].setForeground(Color.decode(Constants.CUSTOM_COLORS[3]));
                tableOfficialLabels[i].setBounds(70, 130, 250, 90); 
            }
        }
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

        for (JLabel label : tableOfficialLabels) { 
            sideBar.add(label);
        }

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
                int yPos = 230 + (90 * i);
                btns[i].setBounds(10, yPos, 250, 70); 
                btns[i].setBackground(Color.decode(Constants.CUSTOM_COLORS[1]));
                btns[i].setForeground(Color.decode(Constants.CUSTOM_COLORS[3]));
            }
            sideBar.add(btns[i]);
        }
    }

    private JButton addButton(String label, int xPos, int yPos, int btnWidth, int btnHeight) {
        JButton button = new JButton(label);
        button.setBounds(xPos, yPos, btnWidth, btnHeight);
        button.setBackground(Color.decode(Constants.CUSTOM_COLORS[2]));
        button.setForeground(Color.decode(Constants.CUSTOM_COLORS[4]));
        button.setFont(Constants.customFonts[0].deriveFont(16f));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(this);
        forms.add(button);
        return button; // Return the created button
    }

    public static void main(String[] args) {
        new TableOfficial();
    }
}