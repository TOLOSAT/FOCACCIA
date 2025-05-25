import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.FileReader;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import com.fasterxml.jackson.core.JsonParser;
import com.fazecast.jSerialComm.SerialPort;

import controller.ConsoleController;
import controller.PusController;
import debug.SerialConnector;
import debug.uart.UartConnector;
import ui.pages.ConsolePage;
import ui.pages.HomePage;
import ui.pages.ReportPage;

public class TapasDebugger extends JFrame {
    final static String HOMEPAGE = "Home";
    final static String CONSOLE = "Console";
    final static String REPORT = "Report";

    public TapasDebugger() {
        // Set up the window
        super("Focaccia - Tapas Debugger");

        new ConsoleController();
        new PusController();

        WindowListener l = new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        };

        addWindowListener(l);
        setSize(1000,800);
        setVisible(true);

        // Create the tabbed pane
        JTabbedPane cards = new JTabbedPane();
        cards.addTab(HOMEPAGE, new HomePage());
        cards.addTab(CONSOLE, new ConsolePage());
        cards.addTab(REPORT, new ReportPage());

        add(cards, BorderLayout.CENTER);
    }

    public static void main(String [] args){
        new TapasDebugger().setVisible(true);
    }
}