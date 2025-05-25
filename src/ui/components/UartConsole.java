package ui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import debug.uart.UartConnector;

public class UartConsole extends JPanel {
    private static JTextPane consoleArea;

    public UartConsole() {
        super();

        consoleArea = new JTextPane();
        consoleArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        consoleArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(consoleArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        add(scrollPane);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    public static void appendLine(String line, Color c) {
        SwingUtilities.invokeLater(() -> {
            StyledDocument doc = consoleArea.getStyledDocument();
            StyleContext sc = StyleContext.getDefaultStyleContext();
            AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

            try {
                doc.insertString(doc.getLength(), line + "\n", aset);
                consoleArea.setCaretPosition(doc.getLength());
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        });
    }
}
