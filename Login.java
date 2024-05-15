import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Login extends JFrame implements ActionListener {
    // Components for the Login
    private JPanel titlePanel, loginForm;
    private JLabel username, password, welcomeTxt;
    private JTextField user;
    private JPasswordField pass;
    private JButton authBtn;

    public Login() {
        setTitle("Tournament Management System");
        setSize(Constants.WIDTH, Constants.HEIGHT);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false); 
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        initComponents();
        setLoginForm();
    }

    private void initComponents() {      
        username = new JLabel("Username");
        password = new JLabel("Password");
        welcomeTxt = new JLabel("WELCOME!");
        user = new JTextField();
        pass = new JPasswordField();
        loginForm = new JPanel();
        titlePanel = new JPanel();
        authBtn = new JButton("Login");
    }

    private void setLoginForm() {

        Color formColor = Color.decode(Constants.CUSTOM_COLORS[0]), 
        bgColor = Color.decode(Constants.CUSTOM_COLORS[1]); 
        ImageIcon img = new ImageIcon(Constants.ASSET_DIR + "kai-sotto.png");

        titlePanel.setBounds(0,0, Constants.WIDTH / 2, Constants.HEIGHT);
        titlePanel.setBackground(bgColor);
        // Add Title and Icon for dark mode on the panel

        loginForm.setBounds(Constants.WIDTH / 2, 0, Constants.WIDTH, Constants.HEIGHT);
        loginForm.setBackground(formColor);
        loginForm.setLayout(null);
        username.setFont(new Font("Arial",Font.PLAIN,25));
        username.setForeground(Color.decode(Constants.CUSTOM_COLORS[3]));
        password.setFont(new Font("Arial",Font.PLAIN,25));
        password.setForeground(Color.decode(Constants.CUSTOM_COLORS[3]));
        welcomeTxt.setFont(new Font("Arial", Font.BOLD, 60));
        welcomeTxt.setBounds(125, 100, 200, 40);
        user.setFont(new Font("Arial",Font.PLAIN,25));
        username.setBounds(125, 135, 200, 35);
        user.setBounds(125, 175, 200, 35);
        user.setBackground(Color.decode(Constants.CUSTOM_COLORS[3]));
        password.setBounds(125, 235, 200, 35);
        pass.setBounds(125, 275, 200, 35);
        pass.setFont(new Font("Arial",Font.PLAIN,25));
        pass.setBackground(Color.decode(Constants.CUSTOM_COLORS[3]));
        pass.setEchoChar('*');
        authBtn.addActionListener(this);
        authBtn.setBounds(150, 350, 150, 50);
        authBtn.setBackground(Color.decode(Constants.CUSTOM_COLORS[2]));
        authBtn.setForeground(Color.decode(Constants.CUSTOM_COLORS[0]));
        authBtn.setFont(new Font("Arial",Font.BOLD,25));
        loginForm.add(username);
        loginForm.add(welcomeTxt);
        loginForm.add(password);
        loginForm.add(user);
        loginForm.add(pass);
        loginForm.add(authBtn);

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
            new ErrorBox("Invalid Username/Password. Please enter valid/non-empty inputs.");
            user.setText("");
            pass.setText("");
        }

        if(validAdminLogin) {
            new Organizer();
            dispose();
        } else if (validOrgLogin) {
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

// Add all constants for the whole program HERE:
class Constants {
    public static final String ASSET_DIR = "Resources/";
    public static final int WIDTH = 900, HEIGHT = 500;
    public static String[] ADMIN_CREDENTIALS = {"admin", "123"};
    public static String[] OFFICIAL_CREDENTIALS = {"official", "123"};
    public static String[] CUSTOM_COLORS = {"#3E3E42", "#252526", "#00ECA4", "#e4e4e4"};
}

// Display Error Messages HERE:
class ErrorBox {
    private Image icon;
    private ImageIcon errorIcon;
    public ErrorBox(String message) {
        icon = new ImageIcon(Constants.ASSET_DIR + "error.png").getImage().getScaledInstance(32, 32, 4);
        errorIcon = new ImageIcon(icon);
        JOptionPane.showMessageDialog(null, 
        message, 
        "Error!", 
        JOptionPane.ERROR_MESSAGE, 
        errorIcon);
    }
}