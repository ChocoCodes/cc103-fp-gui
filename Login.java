import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.List;
import java.io.*;

public class Login extends JFrame implements ActionListener {
    // Components for the Login
    private JPanel titlePanel, loginForm, descPanel;
    private JLabel[] loginFormFields = new JLabel[4];
    private JLabel[]  descriptionFields = new JLabel[3];
    private JTextField user;
    private JPasswordField pass;
    private JButton authBtn;

    public Login() {
        initComponents();
        Constants.setCustomFont();
        setLoginForm();
        setTitle("Tournament Management System");
        setSize(Constants.WIDTH, Constants.HEIGHT);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false); 
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initComponents() {      
        String[] fields = {"Username*", "Password*", "WELCOME!", "Please enter your details below."};
        String[] descFields = {"TOURNAMENT", "MANAGEMENT", "SYSTEM"};
        for(int i = 0; i < loginFormFields.length; i++) {
            loginFormFields[i] = new JLabel(fields[i]);
        }
        user = new JTextField();
        pass = new JPasswordField();
        loginForm = new JPanel();
        for(int i = 0; i < descriptionFields.length; i++) {
            descriptionFields[i] = new JLabel(descFields[i]);
        }
        titlePanel = new JPanel();
        descPanel = new JPanel();
        authBtn = new JButton("LOGIN");
    }

    private void setLoginForm() {
        Color formColor = Color.decode(Constants.CUSTOM_COLORS[0]), 
        bgColor = Color.decode(Constants.CUSTOM_COLORS[1]); 
        ImageIcon img = new ImageIcon(Constants.ASSET_DIR + "kai-sotto.png");
        descPanel.setLayout(null);
        descPanel.setBounds(45,70, Constants.WIDTH - 550, Constants.HEIGHT - 150);
        descPanel.setBackground(formColor);

        titlePanel.setBounds(0,0, Constants.WIDTH / 2, Constants.HEIGHT);
        titlePanel.setBackground(bgColor);

        for(int i = 0; i < descriptionFields.length; i++) {
            int defaultYpos = 90 + (60 * i);
            descriptionFields[i].setFont(Constants.customFonts[0].deriveFont(45f));
            descriptionFields[i].setForeground(Color.decode(Constants.CUSTOM_COLORS[3]));
            descriptionFields[i].setBounds(20, defaultYpos, 400, 50);
            descPanel.add(descriptionFields[i]);
        }
        
        loginForm.setBounds(Constants.WIDTH / 2, 0, Constants.WIDTH, Constants.HEIGHT);
        loginForm.setBackground(formColor);
        loginForm.setLayout(null);

        for(int i = 0; i < loginFormFields.length; i++) {
            float size = (i == 2) ? 30f : (i == 3) ? 14f : 16f;
            int yPos = (i == 0) ? 160 : (i == 1) ? 250 : (i == 2) ? 70 : 100;
            int w = (i == 0 || i == 1) ? 200 : 400;
            int h = (i == 0 || i == 1) ? 35 : 50;

            loginFormFields[i].setFont(Constants.customFonts[0].deriveFont(size));
            loginFormFields[i].setForeground(Color.decode(Constants.CUSTOM_COLORS[3]));
            loginFormFields[i].setBounds(70, yPos, w, h);
            loginForm.add(loginFormFields[i]);
        }

        user.setFont(Constants.customFonts[1].deriveFont(20f));
        user.setBounds(70, 190, 300, 40);
        user.setBackground(Color.decode(Constants.CUSTOM_COLORS[3]));

        pass.setFont(Constants.customFonts[1].deriveFont(20f));
        pass.setBounds(70, 280, 300, 40);
        pass.setBackground(Color.decode(Constants.CUSTOM_COLORS[3]));
        pass.setEchoChar('*');

        authBtn.addActionListener(this);
        authBtn.setBounds(70, 350, 300, 50);
        authBtn.setBackground(Color.decode(Constants.CUSTOM_COLORS[2]));
        authBtn.setForeground(Color.decode(Constants.CUSTOM_COLORS[4]));
        authBtn.setFont(Constants.customFonts[0].deriveFont(30f));

        loginForm.add(user);
        loginForm.add(pass);
        loginForm.add(authBtn);

        this.add(descPanel);
        this.add(titlePanel);
        this.add(loginForm);
        this.setIconImage(img.getImage());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       if(e.getSource() == authBtn) {
            validateLogin();
       }
    }

    private void validateLogin() {
        String userInput = user.getText(), passInput = new String(pass.getPassword());
        boolean validAdminLogin = userInput.equals(Constants.ADMIN_CREDENTIALS[0]) && passInput.equals(Constants.ADMIN_CREDENTIALS[1]),
        validOrgLogin = userInput.equals(Constants.OFFICIAL_CREDENTIALS[0]) && passInput.equals(Constants.OFFICIAL_CREDENTIALS[1]);
        if (userInput.length() == 0 || passInput.length() == 0 || (!validAdminLogin && !validOrgLogin)){
            new MessageBox("Invalid Username/Password. Please enter a valid/non-empty input/s.", 0);
            user.setText("");
            pass.setText("");
        }
        if (validAdminLogin) {
            new MessageBox("Organizer Login Success.", 1);
            new Organizer();
            dispose();
        } 
        if (validOrgLogin) {
            new MessageBox("Official Login Success.", 1);
            new TableOfficial();
            dispose();
        } 
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Login();
            }
        });
    }
}

// Utility Class Constants for the whole program
class Constants {
    public static final String ASSET_DIR = "Resources/";
    public static final int WIDTH = 900, HEIGHT = 500;
    public static String[] ADMIN_CREDENTIALS = {"admin", "123"};
    public static String[] OFFICIAL_CREDENTIALS = {"official", "123"};
    public static String[] CUSTOM_COLORS = {"#262853", "#151139", "#00ECA4", "#e4e4e4", "#3E3E42"};
    private static String[] FONT_STYLE_PATHS = {ASSET_DIR + "Fonts/HandoSoftTrial-Bold.ttf",
    ASSET_DIR + "Fonts/HandoSoftTrial-Regular.ttf"};
    public static Font[] customFonts = new Font[FONT_STYLE_PATHS.length];

    public static void setCustomFont() {
        GraphicsEnvironment GE = GraphicsEnvironment.getLocalGraphicsEnvironment();
        List<String> fontsAvailable = Arrays.asList(GE.getAvailableFontFamilyNames());
        for (int i = 0; i < FONT_STYLE_PATHS.length; i++) {
            String fPath = FONT_STYLE_PATHS[i];
            try {
                File fontFile = new File(fPath);
                if (fontFile.exists()) {
                    Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
                    if (!fontsAvailable.contains(font.getFontName())) {
                        GE.registerFont(font);
                    }
                    customFonts[i] = font;
                }
            } catch (FontFormatException | IOException e) {
                new MessageBox(e.getMessage(), 0);
            }
        }
    }
}

// Display Message Boxes for different purposes:
class MessageBox {
    private Image icon;
    private ImageIcon errorIcon;
    private String boxTitle;
    // 0 - ERROR MESSAGE, 1 - INFO MESSAGE
    public MessageBox(String message, int flag) {
        switch(flag) {
            case 0:
                boxTitle = "Error!";
                icon = new ImageIcon(Constants.ASSET_DIR + "error.png").getImage().getScaledInstance(32, 32, 4);
                break;
            case 1:
                boxTitle = "Info Message!";
                icon = new ImageIcon(Constants.ASSET_DIR + "success.png").getImage().getScaledInstance(32, 32, 4);
                break;
        }
        errorIcon = new ImageIcon(icon);
        JOptionPane.showMessageDialog(null, 
        message, 
        boxTitle, 
        flag, 
        errorIcon);
    }
}