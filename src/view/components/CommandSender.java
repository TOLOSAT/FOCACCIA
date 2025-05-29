package view.components;

import java.awt.Button;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import controller.PusController;
import model.pus.Command;
import model.pus.data.atomics.ComposedData;
import model.pus.data.atomics.EmptyData;
import model.pus.data.atomics.RawData;
import model.pus.data.atomics.integers.UInt32Data;
import model.pus.data.atomics.integers.UInt8Data;

public class CommandSender extends JPanel {

    // Create a map to hold the commands
    private static Map<String, Command> commands;
    static {
        commands = new HashMap<>();
        /**< PUS Service 3   : Housekeeping                                                                                                  */
        commands.put("enable_hk"        , new Command(3  , 5  , new RawData("00:00:00:01")                                                   ));
        commands.put("disable_hk"       , new Command(3  , 6  , new RawData("00:00:00:01")                                                   ));
        /**< PUS Service 6   : Memory management                                                                                             */
        commands.put("load_object_data" , new Command(6  , 1  , new RawData("00:00:00:00:00:00:00:00:00:05:68:65:6c:6c:6f")                  ));
        commands.put("dump_object_data" , new Command(6  , 1  , new RawData("00:00:00:00:00:00:00:00:00:05")                                 ));
        /**< PUS Service 9   : Time Management                                                                                               */
        commands.put("set_on_board_time", new Command(9  , 128, new RawData("1f:7b:91:6d:e5:00")                                             ));
        /**< PUS Service 11  : Time Based Scheduling                                                                                         */
        commands.put("enable_tbs"       , new Command(11 , 1  , new EmptyData()                                                              ));
        commands.put("disable_tbs"      , new Command(11 , 2  , new EmptyData()                                                              ));
        commands.put("add_tbs_activity" , new Command(11 , 4  , new RawData("1f:4f:3e:3a:68:00:00:00:18:55:c0:00:00:06:29:11:01:00:00:90:21")));
        /**< PUS Service 17  : Test                                                                                                          */
        commands.put("ping_test"        , new Command(17 , 1  , new EmptyData()                                                              ));
        /**< PUS Service 160 : System                                                                                                        */
        commands.put("reboot_software"  , new Command(160, 1  , new UInt32Data("mode", 0, 1)                                                 ));
        commands.put("select_software"  , new Command(160, 2  , new ComposedData(new UInt8Data("id", 0, 2), new UInt32Data("mode", 0, 1))    ));
        commands.put("request_context"  , new Command(160, 17 , new EmptyData()                                                              ));
        commands.put("reset_context"    , new Command(160, 23 , new EmptyData()                                                              ));
    }

    private Command command;
    private Component component;
    private final Component[] currentComponent = {null};

    public CommandSender() {
        super();

        JPanel commandPanel = new JPanel();
        commandPanel.setLayout(new BoxLayout(commandPanel, BoxLayout.Y_AXIS));
        add(commandPanel);

        // Create a combo box to select commands
        JComboBox<String> commandList = new JComboBox<>(
            this.commands.keySet()
                .stream()
                .sorted()
                .toArray(String[]::new)
        );

        commandList.addActionListener(e -> {
            // Check if a command is selected
            if (commandList.getSelectedItem() == null) {
                throw new IllegalArgumentException("No command selected");
            }

            // Remove previous component if any
            if (currentComponent[0] != null) {
                commandPanel.remove(currentComponent[0]);
                currentComponent[0] = null;
            }

            // Get the selected command
            command = commands.get(commandList.getSelectedItem().toString());

            component = command.getComponent();
            if (component != null) {
                commandPanel.add(component);
                currentComponent[0] = component;
                component.setVisible(true);
            }
            commandPanel.revalidate();
            commandPanel.repaint();
        });

        // Add the combo box to the panel
        commandPanel.add(commandList);

        // Set the initial selected item to the first command
        command = commands.get(commandList.getSelectedItem().toString());

        component = command.getComponent();
        if (component != null) {
            commandPanel.add(component);
            currentComponent[0] = component;
            component.setVisible(true);
        }

        // Create a button to send the selected command
        Button sendButton = new Button("Send TC");
        sendButton.setSize(10, 30);
        sendButton.addActionListener(e -> {
            // Check if a command is selected
            if (commandList.getSelectedItem() == null) {
                throw new IllegalArgumentException("No command selected");
            }

            // Get the selected command
            command = commands.get(commandList.getSelectedItem().toString());

            // If the command is null, throw an exception
            if (command == null) {
                throw new IllegalArgumentException("Command not found: " + commandList.getSelectedItem());
            }

            // Send the command using PusController
            PusController.sendCommand(command);
        });

        // Add the button to the panel
        add(sendButton);

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    }
}