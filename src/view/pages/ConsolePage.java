package view.pages;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import view.components.CommandSender;
import view.components.UartConsole;
import view.components.ui.LabeledPanel;

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
        add(new CommandSender());
    }
}
