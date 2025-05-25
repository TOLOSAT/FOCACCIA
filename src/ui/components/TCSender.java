package ui.components;

import java.awt.Button;
import java.util.HexFormat;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;

import controller.PusController;
import debug.ErrorReport;
import debug.ErrorReportDecoder;
import debug.tc.TCConnector;

public class TCSender extends JPanel {
    public TCSender() {
        super();

        TCConnector.connect();

        JComboBox<String> commandList = new JComboBox<>(
            // Sorted list of commands from TCConnector
            TCConnector.commands.keySet()
                .stream()
                .sorted()
                .toArray(String[]::new)
            );
        add(commandList);

        Button sendButton = new Button("Send TC");
        sendButton.addActionListener(e -> {
            String selectedCommand = commandList.getSelectedItem() != null ? commandList.getSelectedItem().toString() : null;
            if (selectedCommand != null) {
                byte[] commandBytes = TCConnector.commands.get(selectedCommand);
                if (commandBytes != null) {
                    System.out.println("[LOG] - Sending command: " + selectedCommand + " : " + HexFormat.ofDelimiter(":").formatHex(commandBytes));
                    // TCConnector.sendCommand(commandBytes);
                    PusController.sendCommand(commandBytes);
                } else {
                    System.err.println("[LOG] - Command not found: " + selectedCommand);
                }
            } else {
                System.err.println("[LOG] - No command selected.");
            }
        });
        sendButton.setSize(10, 30);
        add(sendButton);

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    }
}