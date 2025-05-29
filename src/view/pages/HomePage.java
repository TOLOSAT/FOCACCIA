package view.pages;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import view.components.CommandSender;
import view.components.UartConsole;
import view.components.ui.LabeledPanel;

public class HomePage extends JPanel {
    public HomePage() {
        super();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Add a welcome message
        add(new LabeledPanel("Welcome to Focaccia, the Tapas Debugger!", true));
        add(new LabeledPanel("Flatsat Operations Center And Command Control Interface Application", false));
        add(new JSeparator());

        try {
            BufferedImage myPicture = ImageIO.read(new File("assets/tolosat_logo.png"));
            JLabel logoLabel = new JLabel(new ImageIcon(myPicture));
            JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            logoPanel.add(logoLabel);
            add(logoPanel);
        } catch (Exception e) {
            e.printStackTrace();
            add(new JLabel("Failed to load logo image."));
        }

        add(new JSeparator());

        // Add credits
        add(new LabeledPanel("Credits:", true));
        add(new LabeledPanel("Focaccia is a tool developed by Theo Bessel, part of the Tolosat Flight Software team.", false));
        add(new LabeledPanel("For more information, visit: https://theobessel.fr/tapas-debugger", false));
        add(new JSeparator());
    }
}
