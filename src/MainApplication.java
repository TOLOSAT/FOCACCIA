import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import controller.ConsoleController;
import controller.DebuggerController;
import controller.PusController;
import view.pages.ConsolePage;
import view.pages.DebuggerPage;
import view.pages.HomePage;
import view.pages.ReportPage;

public class MainApplication extends JFrame {
    final static String HOMEPAGE = "Home";
    final static String CONSOLE = "Console";
    final static String REPORT = "Report";
    final static String DEBUGGER = "Debugger";

    private DebuggerController debuggerController;

    public MainApplication() {
        // Set up the window
        super("Focaccia - Tapas Debugger");

        new Thread(() -> {
            new ConsoleController();
        }).start();

        new Thread(() -> {
            new PusController();
        }).start();

        debuggerController = new DebuggerController();
        debuggerController.init();

        WindowListener l = new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                if (debuggerController != null) {
                    debuggerController.stop();
                }
                System.exit(0);
            }
        };

        addWindowListener(l);
        setSize(1000,800);
        setVisible(true);

        // Create the tabbed pane
        JTabbedPane cards = new JTabbedPane();
        cards.addTab(HOMEPAGE, new HomePage());
        cards.addTab(CONSOLE, new ConsolePage(debuggerController));
        // cards.addTab(REPORT, new ReportPage());
        // cards.addTab(DEBUGGER, new DebuggerPage(debuggerController));

        add(cards, BorderLayout.CENTER);
    }

    public static void main(String [] args){
        new MainApplication().setVisible(true);
    }
}