import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import ui.pages.HomePage;

public class TapasDebugger extends JFrame {
    final static String HOMEPAGE = "Home";
    final static String DEBUG = "Debug";

    public TapasDebugger() {
        // Set up the window
        super("Tapas Debugger");

        WindowListener l = new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        };

        addWindowListener(l);
        setSize(800,600);
        setVisible(true);

        // Set up the card layout
        HomePage home = new HomePage();

        // Create the tabbed pane
        JTabbedPane cards = new JTabbedPane();
        cards.addTab(HOMEPAGE, home);

        add(cards, BorderLayout.CENTER);
    }

    public static void main(String [] args){
        JFrame frame = new TapasDebugger();

        // frame.pack();
        frame.setVisible(true);
    }
}