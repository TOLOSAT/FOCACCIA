package ui.pages;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import debug.uart.UartConnector;
import ui.components.LabeledPanel;
import ui.components.PortListDisplay;
import ui.components.TCSender;
import ui.components.UartConsole;

public class ConsolePage extends JPanel {
    public ConsolePage() {
        super();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Add port list
        add(new LabeledPanel("UART console :", true));
        // add(new PortListDisplay(UartConnector.listPorts()));
        add(new UartConsole());
        add(new JSeparator());

        // Add the TCSender component
        add(new LabeledPanel("TC Sender :", true));
        add(new TCSender());
    }
}
