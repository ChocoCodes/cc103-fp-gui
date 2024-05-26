import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Random;

public class Organizer extends JFrame implements ActionListener {
    private FileOperations fileOp = new FileOperations(); // File Handler
    private CardLayout cdl;
    private JPanel sideBar;
    private JPanel[] forms = new JPanel[4];
    private JButton[] sideBarBtns = new JButton[4], dashBoardButtons = new JButton[2];
    private JLabel[] adminLabels = new JLabel[2], counterLabels = new JLabel[4];
    private Image adminProfile;
    private ImageIcon img;
    private JButton playerFormSubmit, teamFormSubmit, popupBtn;
    private JTextField[] playerInputFields = new JTextField[3], teamInputFields = new JTextField[2];
    private int teamCounts = 0, playerCounts = 0;
    private boolean connectedToCSV;
    private Team[] teams = new Team[0]; // Data Handler
    private Player[] players = new Player[0];
    private DefaultTableModel tableModel;
    private JTable dashboardDataTable;
    private JComboBox<String> selectionPopup, popupOptions;
    private JScrollPane scrollPane;
    private char currentOperation = ' ';
    private String selectedTeam = "";

    public Organizer() {
        setFrame();
        initComponents();
        Constants.setCustomFont();
        connectedToCSV = connectToCSVDB();     
        if(!connectedToCSV) {
            new MessageBox("Please manually check if the CSV File exists or is not corrupted.", JOptionPane.ERROR_MESSAGE);
            dispose();
            new Login();
        } else {
            teams = fileOp.extractTeamData(Constants.DATA_DIR + Constants.TEAM_FILE);
            if(teams.length == 0) {
                new MessageBox("Please manually check your CSV File if corrupted or empty.", JOptionPane.ERROR_MESSAGE);
            }
        }
        setDashboard();
    }

    private void setFrame() {
        setTitle("Organizer Dashboard");
        setSize(Constants.WIDTH + 200, Constants.HEIGHT + 100);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initComponents() {
        img = new ImageIcon(Constants.ASSET_DIR + "kai-sotto.png");
        String[] btnLabel = {
            "HOME", 
            "ADD TEAMS", 
            "ADD PLAYERS", 
            "LOGOUT"
        };
        adminProfile = new ImageIcon(Constants.ASSET_DIR + "caloocan.jpg").getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        cdl = new CardLayout();
        sideBar = new JPanel(); 
        for (int i = 0; i < sideBarBtns.length; i++) {
            sideBarBtns[i] = new JButton(btnLabel[i]);
        }
        for (int i = 0; i < adminLabels.length; i++) {
            adminLabels[i] = (i == 1) ? new JLabel("ADMIN") : new JLabel();
        }
        selectionPopup = new JComboBox<>();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        int idx = 0;

        if(ae.getSource() == sideBarBtns[sideBarBtns.length - 1]) {
            new MessageBox("Thanks for using the system!", 1);
            dispose();
            new Login();
        }

        for(int i = 0; i < sideBarBtns.length - 1; i++) {
            if(ae.getSource() == sideBarBtns[i]) {
                cdl.show(forms[0], Integer.toString(i + 1));
            }
        }

        if(ae.getSource() == playerFormSubmit) {
            idx = 2;
            handlePlayerFormSubmission(idx);
        }

        if(ae.getSource() == teamFormSubmit) {
            idx = 1;
            handleTeamFormSubmission(idx);
        }

        char flag = ' ';
        if(ae.getSource() == dashBoardButtons[0]) {
            flag = 'D';
            JFrame popupFrame = createPopupFrame(flag);
            popupFrame.setVisible(true);
        }
        if(ae.getSource() == dashBoardButtons[1]) {
            flag = 'G';
            JFrame popupFrame = createPopupFrame(flag);
            popupFrame.setVisible(true);
        }
        
        if(ae.getSource() == popupBtn) {
            handlePopupOperations();
        }
    }


    private void handlePlayerFormSubmission(int idx) {
        boolean areValidInputs, fileSaved = false;
        int defaultValue = 0;

        selectedTeam = selectionPopup.getSelectedItem().toString();
        String filePath = Constants.DATA_DIR + Constants.PLAYERS_DIR + selectedTeam + ".csv";
        boolean fileExists = fileOp.checkIfFileExists(filePath);
        players = new Player[0];
        Team team = new Team();
        for(int i = 0; i < teams.length; i++) {
            if(selectedTeam.equals(teams[i].getTeamName())) {
                team = teams[i];
                break;
            }
        }
        if (fileExists) {
            players = fileOp.extractPlayerData(filePath);
        }
        int pCountListed = team.getPlayerCount();
        areValidInputs = validateFormInputs(playerInputFields, idx, 'P');
        if (areValidInputs && players.length <= pCountListed) {
            String fName = playerInputFields[0].getText().toString();
            String lName = playerInputFields[1].getText().toString();
            String jersey = playerInputFields[2].getText().toString();
            Player player = new Player(fName, lName, jersey, defaultValue, defaultValue, defaultValue, defaultValue, defaultValue);
            boolean append = players.length > 0;
            fileSaved = fileOp.writeToCSVFile(player, filePath, append);
            if(fileSaved) {
                new MessageBox("Player added to team. CSV file updated successfully.", JOptionPane.INFORMATION_MESSAGE);
            } else {
                new MessageBox("An error occured while saving data to the CSV File.", JOptionPane.ERROR_MESSAGE);                 
            }  
        }
    }

    private void handleTeamFormSubmission(int idx) {
        int defaultValue = 0;
        boolean areValidInputs = validateFormInputs(teamInputFields, idx, 'T');
        if(areValidInputs) {
            int newID = generateTeamID();
            String teamName = teamInputFields[0].getText();
            int playerCount = Integer.parseInt(teamInputFields[1].getText());
            Team newTeam = new Team(teamName, newID, playerCount, defaultValue, defaultValue);
            boolean fileSaved = fileOp.saveToCSV(Constants.DATA_DIR + Constants.TEAM_FILE, newTeam);
            if(!fileSaved) {
                new MessageBox("An error occured when saving the file.", JOptionPane.ERROR_MESSAGE);
            } else {
                new MessageBox("New Team Created. File saved successfully.", JOptionPane.INFORMATION_MESSAGE);
            }
            teamCounts++;
            playerCounts += playerCount;
            updateComponents(teamName);
        }
    }

    private void handlePopupOperations() {
        switch (currentOperation) {
            case 'D':   
                String teamToDelete = popupOptions.getSelectedItem().toString();
                boolean teamDeleted = deleteTeamRecord(teamToDelete);
                if(!teamDeleted) {
                    new MessageBox("Failed to delete team " + teamToDelete + ". Please check the CSV File.", JOptionPane.ERROR_MESSAGE);
                } else {
                    new MessageBox("Team deleted successfully. Updated CSV File.", JOptionPane.INFORMATION_MESSAGE);
                }
                break;
            case 'G':
                boolean isValidTeamCount = checkMinimumTeamCount(teams),
                roundRobinGenerated = fileOp.checkIfFileExists(Constants.DATA_DIR + Constants.SCHEDULES_DIR + Constants.RR_FILE), 
                eliminationGenerated = fileOp.checkIfFileExists(Constants.DATA_DIR + Constants.SCHEDULES_DIR + Constants.SE_FILE);
                if(!isValidTeamCount) {
                    new MessageBox("Minimum Team Counts should be " + Constants.MIN_TEAMS + " before generating.", JOptionPane.ERROR_MESSAGE);
                }
                if(roundRobinGenerated && eliminationGenerated) {
                    new MessageBox("Tournament format/s already generated.", JOptionPane.ERROR_MESSAGE);
                } else {
                    String tournamentFormat = popupOptions.getSelectedItem().toString();
                    if(tournamentFormat.equals(Constants.TOURNAMENT_FORMATS[0])) {
                        generateRoundRobin(teams);
                    } else {
                        generateSingleElimination(teams);
                    }
                }
                break;
        }
    }

    private void generateSingleElimination(Team[] teams) {
        if (!checkforEvenTeamCount(teams)) {
            new MessageBox("Please ensure that you have an even count of teams.", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String filename = Constants.DATA_DIR + Constants.SCHEDULES_DIR + Constants.SE_FILE;
        // Create Copy of Team List
        String[] newTeamList = new String[teams.length];
        for(int i = 0; i < newTeamList.length; i++) {
            newTeamList[i] = teams[i].getTeamName();
        }
        newTeamList = shuffleArray(newTeamList);
        int remainingTeams = newTeamList.length;

        int ctr = 0;
        String[] matchUps = new String[remainingTeams / 2];
        // Dynamically create matchups and append to the matchUps array
        for (int i = 0; i < remainingTeams - 1; i += 2) {
            String teamA = newTeamList[i];
            String teamB = newTeamList[i + 1];
            String matchUp = teamA + " vs " + teamB;
            matchUps[ctr++] = matchUp;
        }

        boolean isSaved = fileOp.writeToCSVFile(filename, matchUps);
        if (!isSaved) {
            new MessageBox("An error occured when saving match schedule", JOptionPane.ERROR_MESSAGE);
        } else {
            new MessageBox("Match schedules for Single Elimination saved successfully.", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private String[] shuffleArray(String[] array) {
        Random random = new Random();
        for (int i = array.length - 1; i >= 0; i--) {
            int index = random.nextInt(i + 1);
            //Swap elements Randomly
            String temp = array[i];
            array[i] = array[index];
            array[index] = temp;
        }
        return array;
    }

    private boolean checkforEvenTeamCount(Team[] teams) {
        return teams.length % 2 == 0;
    }

    private boolean checkMinimumTeamCount(Team[] teams) {
        return teams.length >= Constants.MIN_TEAMS;
    }

    // Generate Round Robin Format
    private void generateRoundRobin(Team[] teams) {
        String filePath = Constants.DATA_DIR + Constants.SCHEDULES_DIR + Constants.RR_FILE;
        String[] matchTeam = new String[(teams.length * (teams.length - 1) / 2)];
        int matchTeamIndex = 0;
        // Generate Team Match-Ups
        for (int i = 0; i < teams.length; i++) {
            for (int j = i + 1; j < teams.length; j++) {
                if (teams[i].getTeamName() != teams[j].getTeamName()) {
                    matchTeam[matchTeamIndex] = (teams[i].getTeamName() + " vs " + teams[j].getTeamName());
                    matchTeamIndex++;
                }
            }
        }
        boolean matchSaved = fileOp.writeToCSVFile(filePath, matchTeam);
        if (!matchSaved)  {
            new MessageBox("An error occured when saving the schedule.", JOptionPane.ERROR_MESSAGE);
        } else {
            new MessageBox("Round Robin schedule successfully generated.", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private boolean deleteTeamRecord(String team) {
        int idxToDelete = 0;
        for(int i = 0; i < teams.length; i++) {
            if(teams[i].getTeamName().equals(team)) {
                idxToDelete = i;
                playerCounts -= teams[i].getPlayerCount();
                teamCounts--;
                selectionPopup.removeItemAt(i);
                popupOptions.removeItemAt(i);
                break;
            }
        }
        Team[] newTeam = new Team[teams.length - 1];
        int newIndex = 0;
        for (int i = 0; i < teams.length; i++) {
            if (i != idxToDelete) {
                newTeam[newIndex] = teams[i];
                newIndex++;
            }
        }
        // Save CSV File
        boolean isSaved = fileOp.saveToCSV(newTeam, Constants.DATA_DIR + Constants.TEAM_FILE);
        updateComponents(idxToDelete);
        return isSaved;
    }

    private void updateComponents(int idx) {
        teams = fileOp.extractTeamData(Constants.DATA_DIR + Constants.TEAM_FILE);
        loadDataToTable();
        counterLabels[2].setText(Integer.toString(teamCounts));
        counterLabels[3].setText(Integer.toString(playerCounts));
    }

    private void updateComponents(String teamName) {
        resetInputFields(teamInputFields);
        teams = fileOp.extractTeamData(Constants.DATA_DIR + Constants.TEAM_FILE);
        selectionPopup.addItem(teamName);
        counterLabels[2].setText(Integer.toString(teamCounts));
        counterLabels[3].setText(Integer.toString(playerCounts));
        loadDataToTable();
    }   

    private boolean connectToCSVDB() {
        String fPath = Constants.DATA_DIR + Constants.TEAM_FILE;
        File file = new File(fPath);
        boolean validFileStructure = false;
        if(file.exists()) {
            if(file.length() == 0) {
                return true;
            } else {
                validFileStructure = fileOp.checkFileStructure(fPath, Constants.TEAM_FIELD_COUNTS, 1);
                if(!validFileStructure) {
                    return false;
                }
            }
        } else {
            try {
                boolean created = file.createNewFile();
                if (!created) {
                    new MessageBox("Failed to create file " + file.getAbsolutePath(), JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } catch (IOException e) {
                return false;
            }
        }
        return true;
    }

    private void resetInputFields(JTextField[] inputs) {
        for(int i = 0; i < inputs.length; i++) {
            inputs[i].setText("");
        }
    }

    private int generateTeamID() {
        int maxID = 0;
        for(int i = 0; i < teams.length; i++) {
            if(teams[i].getTeamID() > maxID) {
                maxID = teams[i].getTeamID();
            }
        }
        return maxID + 1;
    }

    private boolean validateFormInputs(JTextField[] inputs, int numIndex, char flag) {
        for (int i = 0; i < inputs.length; i++) {
            String tmp = inputs[i].getText();
            if (tmp.isEmpty()) {
                new MessageBox("Input is empty.", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            boolean isValid = (i == numIndex) ? fileOp.checkIfNumber(tmp) : fileOp.checkIfAlphabet(tmp);
            if (!isValid) {
                new MessageBox("Invalid input at field " + (i + 1) + ".", JOptionPane.ERROR_MESSAGE);
                inputs[i].setText("");
                return false;
            }
        }
        switch (flag) {
            case 'T':
                String teamName = inputs[0].getText(); 
                boolean isDuplicate = fileOp.checkDuplicates(teamName, teams);
                int playerCount = Integer.parseInt(inputs[numIndex].getText());
                boolean validPlayerCount = (playerCount >= Constants.MIN_PLAYERS && playerCount <= Constants.MAX_PLAYERS);
                String errorMessage = "";
                if (!isDuplicate) {
                    inputs[0].setText("");
                    errorMessage += "Team name is a duplicate.\n";
                }
                if (!validPlayerCount) {
                    inputs[numIndex].setText("");
                    errorMessage += "Player count is less than " + Constants.MIN_PLAYERS + " or greater than " + Constants.MAX_PLAYERS + ".\n";
                }
                if(!isDuplicate || !validPlayerCount) {
                    new MessageBox(errorMessage, JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                break;
            case 'P':
                String filePath = Constants.DATA_DIR + Constants.PLAYERS_DIR + selectedTeam + ".csv";
                String jNum = inputs[2].getText().toString();
                players = fileOp.extractPlayerData(filePath);
                if(players.length == 0) {
                    return true;
                }
                boolean duplicateJerseyNumber = fileOp.checkDuplicates(jNum, players);
                if(duplicateJerseyNumber) {
                    inputs[2].setText("");
                    new MessageBox("No duplicate jersey numbers allowed.", JOptionPane.ERROR_MESSAGE);
                }
                break;
        }
        return true;
    }

    private int getTotalPlayerCounts() {
        int total = 0;
        for(int i = 0; i < teams.length; i++) {
            total += teams[i].getPlayerCount();
        }
        return total;
    }


    private void setSelections() {
        for(int i = 0; i < teams.length; i++) {
            selectionPopup.addItem(teams[i].getTeamName());
        }
    }

    private void setDashboard() {
        Color sideBarColor = Color.decode(Constants.CUSTOM_COLORS[1]),
        formsColor = Color.decode(Constants.CUSTOM_COLORS[0]);
        sideBar.setBounds(0, 0, 270, Constants.HEIGHT + 100);
        sideBar.setBackground(sideBarColor);
        sideBar.setLayout(null);

        for(int i = 0; i < forms.length; i++) {
            forms[i] = (i == 0) ? new JPanel(cdl) : (i == 1) ? setDashboardInfo() : (i == 2) ? setTeamForm() : setPlayerForm();
            forms[i].setBounds(270, 0, Constants.WIDTH - 70, Constants.HEIGHT + 100);
            forms[i].setBackground(formsColor);
            if(i == 0) {
                continue;
            } else {
                forms[0].add(forms[i], Integer.toString(i));
            }
        }

        for (int i = 0; i < sideBarBtns.length; i++) {
            int buttonWidth = (i == sideBarBtns.length - 1) ? 200 : 300; // Different width for "LOGOUT" button
            int xPos = (270 - buttonWidth) / 2; // Centering the button
    
            if (i == sideBarBtns.length - 1) {
                sideBarBtns[i].setBounds(xPos, 480, 200, 50);
                sideBarBtns[i].setBackground(Color.decode(Constants.CUSTOM_COLORS[2]));
                sideBarBtns[i].setForeground(Color.decode(Constants.CUSTOM_COLORS[4]));
            } else {
                int yPos = 200 + (70 * i);
                sideBarBtns[i].setBounds(xPos, yPos, 300, 50);
                sideBarBtns[i].setBackground(sideBarColor);
                sideBarBtns[i].setForeground(Color.decode(Constants.CUSTOM_COLORS[3]));
            }
            sideBarBtns[i].setFont(Constants.customFonts[0].deriveFont(30f));
            sideBarBtns[i].setBorderPainted(false);
            sideBarBtns[i].setFocusPainted(false);
            sideBarBtns[i].addActionListener(this);
            sideBar.add(sideBarBtns[i]);
        }

        adminLabels[0].setBounds(85, 30, 100, 100);
        adminLabels[0].setIcon(new ImageIcon(adminProfile));
        sideBar.add(adminLabels[0]);
        adminLabels[1].setBounds(75, 130, 250, 50);
        adminLabels[1].setFont(Constants.customFonts[0].deriveFont(30f));
        adminLabels[1].setForeground(Color.decode(Constants.CUSTOM_COLORS[3]));
        sideBar.add(adminLabels[1]);

        cdl.show(forms[0], "1");
        this.add(sideBar);
        this.add(forms[0]);
        this.setIconImage(img.getImage());
    }

    private JPanel setPlayerForm() {
        JPanel tForm = new JPanel();
        JLabel[] labels = new JLabel[6];
        playerFormSubmit = new JButton("Submit");
        String[] displayLabels = {
            "PLAYER DETAILS", 
            "Please enter player information below. No duplicate players allowed.",
            "Player First Name", 
            "Player Last Name", 
            "Player Team", 
            "Player Jersey Number"
        };
        int xPos = 0, yPos = 0;
        float size;

        tForm.setLayout(null);
        for(int i = 0; i < playerInputFields.length; i++) {
            xPos = (i % 2 == 0) ? 450 : 70;
            yPos = (i == playerInputFields.length - 1) ? 220 + 120 : 180;
            playerInputFields[i] = new JTextField();
            playerInputFields[i].setFont(new Font("Arial", Font.PLAIN, 20));
            playerInputFields[i].setBounds(xPos, yPos, 300, 40);
            playerInputFields[i].setBackground(Color.decode(Constants.CUSTOM_COLORS[3]));
            tForm.add(playerInputFields[i]);
        }
        for(int i = 0; i < labels.length; i++) {
            size = (i == 0) ? 45f : (i == 1) ? 14f : 16f;
            labels[i] = new JLabel(displayLabels[i]);
            if(i < 2) {
                xPos = 70;
                yPos = (i == 0) ? 30 : 70;         
            } else {
                xPos = (i % 2 == 0) ? 70 : 450;
                yPos = 140 + ((i - 2) / 2) * 160;
            }
            labels[i].setBounds(xPos, yPos, 550, 50);
            labels[i].setFont(Constants.customFonts[0].deriveFont(size));
            labels[i].setForeground(Color.decode(Constants.CUSTOM_COLORS[3]));
            tForm.add(labels[i]);
        }


        selectionPopup.setBounds(70, 340, 300, 40);
        selectionPopup.setFont(Constants.customFonts[1].deriveFont(20f));
        selectionPopup.setForeground(Color.decode(Constants.CUSTOM_COLORS[4]));
        selectionPopup.setBackground(Color.decode(Constants.CUSTOM_COLORS[3]));
        if(teams.length != 0) {
            setSelections();
        }
        selectionPopup.addActionListener(this);
        tForm.add(selectionPopup);

        playerFormSubmit.setBounds(610,430, 140, 50);
        playerFormSubmit.setFont(Constants.customFonts[0].deriveFont(25f));
        playerFormSubmit.setBackground(Color.decode(Constants.CUSTOM_COLORS[2]));
        playerFormSubmit.setForeground(Color.decode(Constants.CUSTOM_COLORS[4]));
        playerFormSubmit.setBorderPainted(false);
        playerFormSubmit.setFocusPainted(false);
        playerFormSubmit.addActionListener(this);
        tForm.add(playerFormSubmit);
        return tForm;
    }

    private JPanel setTeamForm() {
        JPanel tForm = new JPanel();
        String[] displayLabels = {
            "TEAM DETAILS", 
            "Please enter team information below. No duplicate teams allowed.", 
            "Team Name", 
            "Player Counts"
        };
        JLabel[] labels = new JLabel[4];
        teamFormSubmit = new JButton("Submit");

        tForm.setLayout(null);
        for(int i = 0; i < teamInputFields.length; i++) {
            int yPos = 220 + (100 * i);
            teamInputFields[i] = new JTextField();
            teamInputFields[i].setFont(new Font("Arial", Font.PLAIN, 20));
            teamInputFields[i].setBounds(250, yPos, 300, 40);
            teamInputFields[i].setBackground(Color.decode(Constants.CUSTOM_COLORS[3]));
            tForm.add(teamInputFields[i]);
        }

        for(int i = 0; i < labels.length; i++) {
            float size = (i == 0) ? 45f : (i == 1) ? 14f : 16f;
            int yPos = (i == 0) ? 50 : (i == 1) ? 90 : (i == 2) ? 180 : 280,
            xPos = (i == 0) ? 230 : (i == 1) ? 200 : 250;
            labels[i] = new JLabel(displayLabels[i]);
            labels[i].setFont(Constants.customFonts[0].deriveFont(size));
            labels[i].setForeground(Color.decode(Constants.CUSTOM_COLORS[3]));
            labels[i].setBounds(xPos, yPos, 450, 40);
            tForm.add(labels[i]);
        }

        teamFormSubmit.setFont(Constants.customFonts[0].deriveFont(25f));
        teamFormSubmit.setBounds(330,430, 140, 50);
        teamFormSubmit.setBackground(Color.decode(Constants.CUSTOM_COLORS[2]));
        teamFormSubmit.setForeground(Color.decode(Constants.CUSTOM_COLORS[4]));
        teamFormSubmit.setBorderPainted(false);
        teamFormSubmit.setFocusPainted(false);
        teamFormSubmit.addActionListener(this);
        tForm.add(teamFormSubmit);
        return tForm;
    }
    
    private JPanel setDashboardInfo() {
        teamCounts = teams.length;
        playerCounts = getTotalPlayerCounts();
        JPanel dashBoard = new JPanel();
        JPanel[] counterPanels = new JPanel[2];
        String[] counterNames = {"TEAMS", "PLAYERS", Integer.toString(teamCounts), Integer.toString(playerCounts)};
        String[] btnNames = {"Delete", "Generate"};

        dashBoard.setLayout(null);
        for (int i = 0; i < counterPanels.length; i++) {
            int xPos = 90 + (230 * i);
            counterPanels[i] = new JPanel(null); // Set layout to null for absolute positioning within the panel
            counterPanels[i].setBounds(xPos, 50, 200, 150);
            counterPanels[i].setBackground(Color.decode(Constants.CUSTOM_COLORS[2]));
            
            // Add the counter name label to the panel
            xPos = (i == 0) ? 60 : 50;
            counterLabels[i] = new JLabel(counterNames[i]);
            counterLabels[i].setBounds(xPos, 10, 180, 30); // Positioning inside the panel
            counterLabels[i].setFont(Constants.customFonts[0].deriveFont(20f));
            counterLabels[i].setForeground(Color.decode(Constants.CUSTOM_COLORS[1]));
            counterPanels[i].add(counterLabels[i]);
            
            // Add the count label to the panel
            counterLabels[i + 2] = new JLabel(counterNames[i + 2]);
            counterLabels[i + 2].setFont(new Font("Arial", Font.BOLD, 50));
            counterLabels[i + 2].setForeground(Color.decode(Constants.CUSTOM_COLORS[1]));
            counterPanels[i].add(counterLabels[i + 2]);

            // Calculate the X position to center the label
            int labelWidth = counterLabels[i + 2].getPreferredSize().width;
            int xLabelPos = (200 - labelWidth) / 2; // Centered position
            counterLabels[i + 2].setBounds(xLabelPos, 70, labelWidth, 50); // Positioning inside the panel
    
            int buttonYPos = 50 + 70 * i;
            dashBoardButtons[i] = new JButton(btnNames[i]);
            dashBoardButtons[i].setBounds(550, buttonYPos, 180, 50);
            dashBoardButtons[i].setFont(Constants.customFonts[0].deriveFont(25f));
            dashBoardButtons[i].setForeground(Color.decode(Constants.CUSTOM_COLORS[1]));
            dashBoardButtons[i].setBackground(Color.decode(Constants.CUSTOM_COLORS[2]));
            dashBoardButtons[i].setBorderPainted(false);
            dashBoardButtons[i].setFocusPainted(false);
            dashBoardButtons[i].addActionListener(this);

            dashBoard.add(counterPanels[i]);
            dashBoard.add(dashBoardButtons[i]);
        }

        tableModel = new DefaultTableModel(Constants.DASHBOARD_COLUMNS, 0) {
            // Override the DefaultTableModel to make all the cells non-editable
            // Via StackOverflow at: https://tinyurl.com/nnsscb5d
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dashboardDataTable = new JTable(tableModel);
        scrollPane = new JScrollPane(dashboardDataTable);
        styleTable();
        loadDataToTable();
        dashBoard.add(scrollPane, BorderLayout.CENTER);
        return dashBoard;
    }

    private void styleTable() {
        // Header Styles
        JTableHeader header = dashboardDataTable.getTableHeader();
        header.setBackground(new Color(38,40,83));
        header.setForeground(Color.decode(Constants.CUSTOM_COLORS[3]));
        header.setFont(Constants.customFonts[1].deriveFont(20f));
        header.setBorder(BorderFactory.createEmptyBorder());
        header.setPreferredSize(new Dimension(0, 40));
        // Table Styles
        dashboardDataTable.setBackground(new Color(55,58,99));
        dashboardDataTable.setForeground(Color.decode(Constants.CUSTOM_COLORS[3]));
        dashboardDataTable.setShowGrid(false);
        dashboardDataTable.setRowHeight(40);
        dashboardDataTable.setFont(Constants.customFonts[1].deriveFont(16f));
        dashboardDataTable.setBorder(BorderFactory.createEmptyBorder());
        dashboardDataTable.setSelectionBackground(new Color(77, 81, 125));
        dashboardDataTable.setSelectionForeground(new Color(220,220,220));
        // Center table data
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < dashboardDataTable.getColumnCount(); i++) {
            dashboardDataTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        // Scroll Pane Styles
        scrollPane.setBounds(0, 250, Constants.WIDTH - 85, (Constants.HEIGHT + 200) / 2);
        scrollPane.getViewport().setBackground(Color.decode(Constants.CUSTOM_COLORS[0]));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
    }

    private void loadDataToTable() {
        tableModel.setRowCount(0);
        for(int i = 0; i < teams.length; i++) {
            Object[] rowData = {
                teams[i].getTeamName(),
                teams[i].getTeamID(),
                teams[i].getPlayerCount(),
                teams[i].getWins(),
                teams[i].getLosses()
            };
            tableModel.addRow(rowData);
        }
        Dimension tableSize = dashboardDataTable.getPreferredSize();
        scrollPane.setPreferredSize(new Dimension(tableSize.width, tableSize.height));
        scrollPane.revalidate();
    }

    private JFrame createPopupFrame(char flag) {
        popupOptions = new JComboBox<>();
        popupBtn = new JButton((flag == 'D') ? "Delete" : "Generate");
        String title = (flag == 'D') ? "Delete Team" : "Tournament Format Generator";
        currentOperation = flag;
        JFrame displayFrame = new JFrame(title);

        displayFrame.setSize(400, 200);
        displayFrame.setLayout(null);
        displayFrame.getContentPane().setBackground(Color.decode(Constants.CUSTOM_COLORS[0]));
        displayFrame.setLocationRelativeTo(null);
        displayFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        popupOptions.setBounds((displayFrame.getWidth() - 300) / 2 - 10, (displayFrame.getHeight() - 40) / 2 - 40, 300, 40);
        popupOptions.setFont(Constants.customFonts[1].deriveFont(20f));
        popupOptions.setForeground(Color.decode(Constants.CUSTOM_COLORS[4]));
        popupOptions.setBackground(Color.decode(Constants.CUSTOM_COLORS[3]));

        if(flag == 'D') {
            for(int i = 0; i < teams.length; i++) {
                popupOptions.addItem(teams[i].getTeamName());
            }
        } else {
            for(int i = 0; i < Constants.TOURNAMENT_FORMATS.length; i++) {
                popupOptions.addItem(Constants.TOURNAMENT_FORMATS[i]);
            }
        }
        
        popupBtn.setBounds((displayFrame.getWidth() - 150) / 2 - 10, (displayFrame.getHeight() - 40) / 2 + 20, 150, 40);
        popupBtn.setFont(Constants.customFonts[1].deriveFont(20f));
        popupBtn.setForeground(Color.decode(Constants.CUSTOM_COLORS[1]));
        popupBtn.setBackground(Color.decode(Constants.CUSTOM_COLORS[2]));
        popupBtn.setBorderPainted(false);
        popupBtn.setFocusPainted(false);
        popupBtn.addActionListener(this);

        displayFrame.add(popupBtn);
        displayFrame.add(popupOptions);
        displayFrame.setVisible(true);
        return displayFrame;
    }

}
