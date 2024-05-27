import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class TableOfficial extends JFrame implements ActionListener {
    private JPanel sideBar, forms;
    private JButton[] btns = new JButton[3];
    private JTable table;
    private Image tableOfficialProfile;
    private JLabel[] tableOfficialLabels = new JLabel[2];
    private FileOperations fileOp = new FileOperations();

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

        //FILEPATH METHODS
        String csvFilePath = Constants.DATA_DIR + Constants.SCHEDULES_DIR + Constants.RR_FILE; //
        String[][] data = fileOp.readCSVDataSchedules(csvFilePath);
        String[] columnNames = {"TEAM 1", "TEAM 2", "MATCH NO."}; 

        table = createTable(data, columnNames);
        JScrollPane sp = createScrollPane(table);
        forms.removeAll();
        JLabel tableTitle = createTableTitle("SINGLE ROUND ROBIN - FORMAT");

        addButtons(); 
        
        forms.add(tableTitle);
        forms.add(sp);
        forms.revalidate();
        forms.repaint();
    }

    private void setupSingleEliminationTable() {
        System.out.println("Single Elimination Button Pressed");

        //FILEPATH METHODS
        String csvFilePath = Constants.DATA_DIR + Constants.SCHEDULES_DIR + Constants.SE_FILE;
        String[][] data = fileOp.readCSVDataSchedules(csvFilePath);
        String[] columnNames = {"TEAM 1", "TEAM 2", "MATCH NO."}; 
    
        table = createTable(data, columnNames);
        JScrollPane sp = createScrollPane(table);
    
        forms.removeAll();
    
        JLabel tableTitle = createTableTitle("SINGLE ELIMINATION - FORMAT");
        forms.add(tableTitle);
    
        addButtons();
    
        forms.add(sp);
        forms.revalidate();
        forms.repaint();
    }

    private void handlePlayerStats() {
        if (table != null) {
            int selectedRow = table.getSelectedRow();
            int selectedColumn = table.getSelectedColumn();

            if (isSelectionValid(selectedRow, selectedColumn)) {
                String cellValueString = String.valueOf(table.getValueAt(selectedRow, selectedColumn));
                forms.removeAll();

                String csvFilePath = Constants.DATA_DIR + Constants.PLAYERS_DIR + cellValueString + ".csv";
                String[] columnNames = {"PLAYER", "JERSEY NO.", "POINT/S", "REBOUND/S", "ASSIST/S", "BLOCK/S", "STEAL/S"};
                
                String[][] tempData = fileOp.readCSVPlayerData(csvFilePath); 
                String[][] data = new String [tempData.length][7];

                for (int i = 0; i < tempData.length; i++) {
                    data[i][0] = tempData[i][0] + ", " + tempData[i][1]; 
                    data[i][1] = tempData[i][2]; 
    
                    for (int j = 2, k = 3; j < 7 && k < tempData[i].length; j++, k++) { 
                        data[i][j] = tempData[i][k]; 
                    }
                }

                table = createEditableTable(data, columnNames);
                JScrollPane sp = createScrollPane(table);

                JButton applyButton = addButton("Apply", PLAYER_STATS_BTN_X, PLAYER_STATS_BTN_Y, BUTTON_WIDTH_1, BUTTON_HEIGHT_1);

                applyButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        handleApplyActionPlayerStats(csvFilePath, columnNames);
                    }
                });

                JLabel tableTitle = createTableTitle("ENCODE PLAYER STATS FOR " + cellValueString);
                forms.add(tableTitle);
                forms.add(sp);
                forms.revalidate();
                forms.repaint();
            } else {
                new MessageBox("No / Invalid Column Selected for Player Stats. Usage: Select A Team", 0);
            }
        }
    }

    private boolean isSelectionValid(int selectedRow, int selectedColumn) {
        return selectedRow >= 0 && selectedColumn >= 0 && selectedColumn <= 1;
    }

    private JTable createTable(String[][] data, String[] columnNames) {
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        styleTable(table, false);
        return table;
    }

    private JTable createEditableTable(String[][] data, String[] columnNames) {
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column > 1;
            }
        };

        JTable table = new JTable(model);
        styleTable(table, true);
        return table;
    }

    private void handleApplyActionPlayerStats(String filePath, String[] columnHeaders) {
        int rowCount = table.getRowCount();
        int columnCount = table.getColumnCount();

        String[][] tempTeamStats = extractTableData(rowCount, columnCount); //Row 0 Team 1, Row 1 Team 2
        boolean allFieldsFilled = checkAllFieldsFilled(tempTeamStats, rowCount, columnCount);
        System.out.println("Before validation: " + Arrays.deepToString(tempTeamStats));
        boolean isValidNumber = allFieldsFilled && validateNumbers(tempTeamStats, rowCount, columnCount);

        if (!allFieldsFilled) {
            new MessageBox("Please fill in all fields.", 0);
        } else if (!isValidNumber) {
            new MessageBox("Please enter valid integer numbers.", 0);
        } else {
            String[][] teamStats = new String[tempTeamStats.length][8];
            for (int i = 0; i < tempTeamStats.length; i++) {
                String nameParts = tempTeamStats[i][0];
                String[] storeSplit = nameParts.split(", ");
                // Check if name was split correctly
                if (storeSplit.length == 2) { 
                    teamStats[i][0] = storeSplit[0]; // Last name
                    teamStats[i][1] = storeSplit[1]; // First name
                } else {
                    teamStats[i][0] = storeSplit[0];                      
                }
                for (int j = 1; j < 7; j++) {
                    teamStats[i][j + 1] = tempTeamStats[i][j];
                }
            }
            // DB: Print the contents of teamStats
            System.out.println("teamStats contents:");
            for (String[] row : teamStats) {
                System.out.println(Arrays.toString(row)); 
            }
            fileOp.writePlayerStatsToCSV(teamStats, columnHeaders, filePath);
        }
    }

    private void styleTable(JTable table, boolean isPlayerStats) {
        table.setBackground(new Color(55, 58, 99));
        table.setForeground(new Color(220, 220, 220));
        table.setFont(new Font("Arial", Font.BOLD, isPlayerStats ? 12 : 16));
        table.setRowHeight(40);
        table.setShowGrid(isPlayerStats ? true : false);
        table.setBorder(BorderFactory.createEmptyBorder());

        styleTableHeader(table.getTableHeader());

        table.setSelectionBackground(new Color(77, 81, 125));
        table.setSelectionForeground(new Color(220, 220, 220));
    }

    private void styleTableHeader(JTableHeader header) {
        header.setBackground(new Color(38, 40, 83));
        header.setForeground(new Color(180, 205, 230));
        header.setFont(new Font("Arial", Font.BOLD, 15));
        header.setPreferredSize(new Dimension(0, 65));
        header.setBorder(BorderFactory.createEmptyBorder());
    }

    private JScrollPane createScrollPane(JTable table) {
        JScrollPane sp = new JScrollPane(table);
        sp.getViewport().setBackground(new Color(21, 17, 57));
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.setBounds(SP_TABLE_STARTING_X_POS, SP_TABLE_STARTING_Y_POS, SP_TABLE_WIDTH, SP_TABLE_HEIGHT);
        return sp;
    }

    private JLabel createTableTitle(String title) {
        JLabel tableTitle = new JLabel(title);
        tableTitle.setBounds(SP_TABLE_STARTING_X_POS, TABLE_TITLE_Y, SP_TABLE_WIDTH, TABLE_TITLE_HEIGHT);
        tableTitle.setFont(Constants.customFonts[0].deriveFont(Font.BOLD, TABLE_TITLE_SIZE));
        tableTitle.setForeground(Color.decode(Constants.CUSTOM_COLORS[3]));
        return tableTitle;
    }

    private void addButtons() {
        addButton("Game Report", GAME_REPORT_BTN_X, GAME_REPORT_BTN_Y, BUTTON_WIDTH_1, BUTTON_HEIGHT_1);
        addButton("Player Stats", PLAYER_STATS_BTN_X, PLAYER_STATS_BTN_Y, BUTTON_WIDTH_1, BUTTON_HEIGHT_1);
    }

    private String[][] extractTableData(int rowCount, int columnCount) {
        String[][] teamStats = new String[rowCount][columnCount];
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                Object value = table.getValueAt(row, col);
                if (value != null) {
                    String cellValue = value.toString().trim(); 
                    if (cellValue.contains("+")) { // Check for addition expression
                        cellValue = calculateExpression(cellValue);
                    }
                    teamStats[row][col] = cellValue;
                }
            }
        }
        return teamStats;
    }
    
    private String calculateExpression(String expression) {
        String[] parts = expression.split("\\+"); // Split by "+"
        if (parts.length == 2 && fileOp.checkIfNumber(parts[0].trim()) && fileOp.checkIfNumber(parts[1].trim())) {
            try {
                int result = Integer.parseInt(parts[0].trim()) + Integer.parseInt(parts[1].trim());
                return String.valueOf(result);
            } catch (NumberFormatException e) {
                return expression; // Return if the parts being performed for addition are not valid integers
            }
        } else {
            return expression; // Return the original if not a valid addition
        }
    }

    private boolean checkAllFieldsFilled(String[][] teamStats, int rowCount, int columnCount) {
        for (int row = 0; row < rowCount; row++) {
            for (int col = 3; col < columnCount; col++) {
                if (teamStats[row][col] == null || teamStats[row][col].trim().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean validateNumbers(String[][] teamStats, int rowCount, int columnCount) {
    for (int row = 0; row < rowCount; row++) {
        for (int col = 2; col < columnCount; col++) {
            System.out.println("Validating value at row " + row + ", col " + col + ": " + teamStats[row][col]);
            try {
                int tempNum = Integer.parseInt(teamStats[row][col]);
                if (tempNum < 0) {
                    return false;
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid number found at row " + row + ", col " + col + ": " + teamStats[row][col]);
                return false;
            }
        }
    }
    return true;
}

    private void handleGameReport() {
        if (table != null) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String[] rowData = extractRowData(selectedRow);

                forms.removeAll(); // Clear previous content
                setupGameReportForm(rowData);

                forms.revalidate();
                forms.repaint();
            } else {
                new MessageBox("No / Invalid Row Selected for Game Report. Usage: Select A Team", 0);
            }
        }
    }

    private String[] extractRowData(int selectedRow) {
        int columnCount = table.getColumnCount();
        String[] rowData = new String[columnCount];

        for (int i = 0; i < columnCount; i++) {
            rowData[i] = String.valueOf(table.getValueAt(selectedRow, i));
        }
        return rowData;
    }

    private void setupGameReportForm(String[] rowData) {
        addTableTitle(rowData);
        addStatInputFields();

        JButton applyButton = addButton("Apply", PLAYER_STATS_BTN_X, PLAYER_STATS_BTN_Y, BUTTON_WIDTH_1, BUTTON_HEIGHT_1);

        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleApplyActionGameReport(rowData);
            }
        });
    }

    private void addTableTitle(String[] rowData) {
        JLabel tableTitle = new JLabel(rowData[0] + " VS. " + rowData[1]);
        tableTitle.setBounds(calculateLeftMargin(), TABLE_TITLE_Y, SP_TABLE_WIDTH, TABLE_TITLE_HEIGHT);
        tableTitle.setFont(Constants.customFonts[0].deriveFont(Font.BOLD, TABLE_TITLE_SIZE));
        tableTitle.setForeground(Color.decode(Constants.CUSTOM_COLORS[3]));
        forms.add(tableTitle);
    }

    private void addStatInputFields() {
        String[] statLabels = {
            "Score for Q1", "Score for Q2", "Score for Q3", "Score for Q4",
            "Rebounds", "Blocks", "Steals", "Assists"
        };

        int topMargin = TABLE_TITLE_Y + TABLE_TITLE_HEIGHT + 30;
        int leftMargin = calculateLeftMargin();
        int labelWidth = 150;
        int textFieldWidth = 130;
        int labelHeight = 30;
        int verticalGap = 20;
        int labelTextFieldGap = 50;

        for (int i = 0; i < statLabels.length; i++) {
            int label1X = leftMargin;
            JLabel label1 = createLabel(statLabels[i], label1X, topMargin + i * (labelHeight + verticalGap), labelWidth, labelHeight);
            int textField1X = label1X + labelWidth + labelTextFieldGap - 30;
            JTextField textField1 = createTextField(textField1X, label1.getY(), textFieldWidth, labelHeight);

            int label2X = textField1X + textFieldWidth + calculateColumnGap();
            JLabel label2 = createLabel(statLabels[i], label2X + 30, label1.getY(), labelWidth, labelHeight);
            int textField2X = label2X + labelWidth + labelTextFieldGap;
            JTextField textField2 = createTextField(textField2X, label1.getY(), textFieldWidth, labelHeight);

            forms.add(label1);
            forms.add(textField1);
            forms.add(label2);
            forms.add(textField2);
        }
    }

    private JLabel createLabel(String text, int x, int y, int width, int height) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        label.setFont(Constants.customFonts[0].deriveFont(18f));
        label.setForeground(Color.decode(Constants.CUSTOM_COLORS[3]));
        return label;
    }

    private JTextField createTextField(int x, int y, int width, int height) {
        JTextField textField = new JTextField();
        textField.setBounds(x, y, width, height);
        return textField;
    }

    private int calculateLeftMargin() {
        final int FORM_WIDTH = Constants.WIDTH - 100;
        final int LABEL_WIDTH = 150;
        final int TEXT_FIELD_WIDTH = 130;
        final int LABEL_TEXT_FIELD_GAP = 50;
        final int TOTAL_USED_WIDTH = (2 * LABEL_WIDTH) + (2 * TEXT_FIELD_WIDTH) + LABEL_TEXT_FIELD_GAP;
        final int AVAILABLE_SPACE = FORM_WIDTH - TOTAL_USED_WIDTH;
        return (AVAILABLE_SPACE / 2) - 30;
    }

    private int calculateColumnGap() {
        return 50; // Adjust this value if needed
    }

    private void handleApplyActionGameReport(String[] rowData) {
        Component[] components = forms.getComponents();
        int statIndex = 0;
        String[] team1Stats = new String[8];
        String[] team2Stats = new String[8];
        String[] overallMatchStats = new String[11];
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
            overallMatchStats = calculateOverallMatchStats(team1Stats, team2Stats, rowData);
            fileOp.writeGameReportToCSV(team1Stats, team2Stats, overallMatchStats);
        }
    }
    
    private String[] calculateOverallMatchStats(String[] team1Stats, String[] team2Stats, String[] rowData) {
        String[] overallStats = new String[11];
    
        // Team names (now at the beginning)
        overallStats[0] = rowData[0]; // Team 1 name
        overallStats[1] = rowData[1]; // Team 2 name
        
    
        // Total points 
        int totalPoints = 0;
        for (int i = 0; i < 4; i++) {  
            totalPoints += Integer.parseInt(team1Stats[i]) + Integer.parseInt(team2Stats[i]);
        }
        overallStats[2] = String.valueOf(totalPoints); 
    
        // Longest lead 
        int maxLead = 0;
        int currentLead = 0;
        for (int i = 0; i < 4; i++) { 
            currentLead += Integer.parseInt(team1Stats[i]) - Integer.parseInt(team2Stats[i]);
            maxLead = Math.max(maxLead, Math.abs(currentLead));  
        }
        overallStats[3] = String.valueOf(maxLead);
    
        // Total rebounds, blocks, steals, assists 
        for (int i = 4; i < 8; i++) { 
            overallStats[i] = String.valueOf(Integer.parseInt(team1Stats[i]) + Integer.parseInt(team2Stats[i]));
        }
    
        // Total team scores 
        int totalTeam1Score = 0;
        int totalTeam2Score = 0;
        for (int i = 0; i < 4; i++) { 
            totalTeam1Score += Integer.parseInt(team1Stats[i]);
            totalTeam2Score += Integer.parseInt(team2Stats[i]);
        }
        overallStats[8] = String.valueOf(totalTeam1Score);
        overallStats[9] = String.valueOf(totalTeam2Score);
        
        //Winner
        overallStats[10] = (totalTeam1Score > totalTeam2Score) ? rowData[0] : rowData[1];
        return overallStats;
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
                tableOfficialLabels[i].setText("<html><body style='font-family: \"" + fontStyle + "\"; font-size: " + fontSize + 
                "pt; text-align: center;'>TABLE<br>OFFICIAL</body></html>"); 
                tableOfficialLabels[i].setForeground(Color.decode(Constants.CUSTOM_COLORS[3]));
                tableOfficialLabels[i].setBounds(60, 130, 250, 90); 
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
                "<html><body style='font-family: \"" + fontStyle + "\"; font-size: " + 
                fontSize + "pt; text-align: center;'>ROUND<br>ROBIN</body></html>",
                "<html><body style='font-family: \"" + fontStyle + "\"; font-size: " + 
                fontSize + "pt; text-align: center;'>SINGLE<br>ELIMINATION</body></html>",
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
                int yPos = 235 + (87 * i);
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