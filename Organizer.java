import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Organizer extends JFrame implements ActionListener {
    private FileOperations fileOp = new FileOperations();
    private CardLayout cdl;
    private JPanel sideBar;
    private JPanel[] forms = new JPanel[4];
    private JButton[] sideBarBtns = new JButton[4], dashBoardButtons = new JButton[2];
    private JLabel[] adminLabels = new JLabel[2];
    private Image adminProfile;
    private ImageIcon img;
    private JButton playerFormSubmit, teamFormSubmit;
    private JTextField[] playerInputFields = new JTextField[3], teamInputFields = new JTextField[2];
    private int teamCounts = 0, playerCounts = 0;
    private boolean connectedToCSV;
    private Team[] teams = new Team[0];
    private DefaultTableModel tableModel;
    private JTable dashboardDataTable;

    public Organizer() {
        setFrame();
        initComponents();
        Constants.setCustomFont();
        setDashboard();
        connectedToCSV = connectToCSVDB();
        if(!connectedToCSV) {
            new MessageBox("Please manually check if the CSV File exists or is not corrupted.", JOptionPane.ERROR_MESSAGE);
        } else {
            teams = fileOp.extractTeamData(Constants.DATA_DIR + Constants.TEAM_FILE);
            if(teams.length == 0) {
                new MessageBox("Please manually check your CSV File if corrupted or empty.", JOptionPane.ERROR_MESSAGE);
            }
            for (Team t : teams) {
                System.out.printf("%s, %d, %d, %d, %d\n", t.getTeamName(), t.getTeamID(), t.getPlayerCount(), t.getWins(), t.getLosses());
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
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
            System.out.println("Test Click!");
        }
        if(ae.getSource() == teamFormSubmit) {
            System.out.println("Test Click!");
        }
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

    private boolean connectToCSVDB() {
        String fPath = Constants.DATA_DIR + Constants.TEAM_FILE;
        File file = new File(fPath);
        if(!file.exists()) {
            try {
                boolean created = file.createNewFile();
                if(!created) {
                    new MessageBox("Failed to create file " + file.getAbsolutePath(), JOptionPane.ERROR_MESSAGE);
                }
            } catch(IOException e) {
                return false;
            }
        }
        return true;
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
        JList<String> listBox;
        DefaultListModel<String> listModel = new DefaultListModel<>();  
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

        for(int i = 0; i < teams.length; i++) {
            listModel.addElement(teams[i].getTeamName());
        }
        listBox = new JList<>(listModel);
        listBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listBox.setBackground(Color.decode(Constants.CUSTOM_COLORS[3]));

        JScrollPane scrollPane = new JScrollPane(listBox);
        scrollPane.setBounds(70, 180, 300, 80);
        tForm.add(scrollPane);
        
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
        JPanel dashBoard = new JPanel();
        JPanel[] counterPanels = new JPanel[2];
        JLabel[] counterLabels = new JLabel[4];
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
            counterLabels[i + 2].setBounds(90, 70, 180, 50); // Positioning inside the panel
            counterLabels[i + 2].setFont(new Font("Arial", Font.BOLD, 50));
            counterLabels[i + 2].setForeground(Color.decode(Constants.CUSTOM_COLORS[1]));
            counterPanels[i].add(counterLabels[i + 2]);
    
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

        tableModel = new DefaultTableModel(new String[]{"Team Name", "Team ID", "Player Counts", "Wins", "Losses"}, 0);
        dashboardDataTable = new JTable(tableModel);
        dashboardDataTable.setAutoCreateRowSorter(false);
        dashboardDataTable.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(dashboardDataTable);
        scrollPane.setBounds(0, 250, Constants.WIDTH - 85, (Constants.HEIGHT + 200) / 2);
        dashBoard.add(scrollPane, BorderLayout.CENTER);
        return dashBoard;
    }
}
