import javax.swing.*;
import java.awt.*;

public class GUIBuilder {
    private static final String ASSET_DIR = "Resources/";
    private static final int WIDTH = 1200, HEIGHT = 700;
    
    private JFrame frame;
    private PanelBuilder imgPanel, formPanel;

    public GUIBuilder() {
        setLoginFrame();
    }

    public void setLoginFrame() {
        frame = new JFrame("NBA Tournament Management System");
        ImageIcon img = new ImageIcon(ASSET_DIR + "kai-sotto.png");
        this.frame.setIconImage(img.getImage());
        this.frame.setSize(WIDTH, HEIGHT);
        this.frame.setLayout(new GridLayout(1, 2, 0, 0));
        imgPanel = new PanelBuilder();
        formPanel = new PanelBuilder();
        this.frame.add(imgPanel);
        this.frame.add(formPanel);
        this.frame.pack();
    }

    public void displayWindow() {
        this.frame.setResizable(false); 
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
    }
}

class PanelBuilder extends JPanel {
    private static final String ASSET_DIR = "Resources/";
    private static final int WIDTH = 1200, HEIGHT = 700;

    private JPanel pane;

    public PanelBuilder() {
        buildLoginPanel();
    }
    
    public void buildLoginPanel() {
        pane = new JPanel();
        Image scaledImg = new ImageIcon(ASSET_DIR + "display-img.jpg").getImage().getScaledInstance((WIDTH / 2), HEIGHT, Image.SCALE_DEFAULT);
        ImageIcon displayImg = new ImageIcon(scaledImg);
        JLabel label = new JLabel();
        label.setIcon(displayImg);
        this.pane.add(label);
        this.pane.setPreferredSize(new Dimension((WIDTH / 2), HEIGHT));
        this.add(pane);
    }
}
