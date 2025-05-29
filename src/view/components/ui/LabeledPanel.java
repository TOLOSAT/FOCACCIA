package view.components.ui;

import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class LabeledPanel extends JPanel {
    public LabeledPanel(String text, boolean isTitle) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        JLabel label = new JLabel(text);
        label.setFont(new Font("Monospaced", Font.PLAIN, 12));
        if (isTitle) {
            label.setFont(label.getFont().deriveFont(Font.BOLD, 16f));
        }
        add(label);
        setBorder(new EmptyBorder(5, 0, 5, 0));
    }

    public LabeledPanel(String text, boolean isTitle, boolean centered) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        JLabel label = new JLabel(text, centered ? SwingConstants.CENTER : SwingConstants.LEFT);
        label.setFont(new Font("Monospaced", Font.PLAIN, 12));
        if (isTitle) {
            label.setFont(label.getFont().deriveFont(Font.BOLD, 16f));
        }
        add(label);
        setBorder(new EmptyBorder(5, 0, 5, 0));
    }
}
