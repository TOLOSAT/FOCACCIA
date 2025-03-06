package ui.pages;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import ui.components.FileSelector;
import ui.components.ReportDisplay;

public class HomePage extends JPanel {
    public HomePage() {
        super();

        // Add a welcome message
        add(createLabeledPanel("Welcome to the Tapas Debugger!", true));

        add(new JSeparator());

        // Add a file selector
        FileSelector fileSelector = new FileSelector();
        add(fileSelector);

        // Diplays the selected file
        ReportDisplay reportDisplay = new ReportDisplay(fileSelector.getFile());
        add(reportDisplay);

        // Set the layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    private JPanel createLabeledPanel(String text, boolean isTitle) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        JLabel label = new JLabel(text);
        if (isTitle) {
            label.setFont(label.getFont().deriveFont(Font.BOLD, 14f));
        }
        panel.add(label);
        panel.setBorder(new EmptyBorder(5, 0, 5, 0));
        return panel;
    }
}
