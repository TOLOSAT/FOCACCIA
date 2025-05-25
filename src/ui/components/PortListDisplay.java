package ui.components;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.fazecast.jSerialComm.SerialPort;

public class PortListDisplay extends JPanel {
    public PortListDisplay(List<SerialPort> ports) {
        super();

        ports.stream()
            .filter(SerialPort::isOpen)
            .map(SerialPort::getSystemPortName)
            .sorted()
            .forEach(port -> {
                add(new LabeledPanel(port, false, false));
            });

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }
}
