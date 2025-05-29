package controller;

import java.awt.Color;

import model.SerialConnector;
import model.pus.Command;

public class PusController {
    public static SerialConnector pus;

    public PusController() {
        pus = new SerialConnector("pus", Color.BLUE);
        pus.connect();
        pus.read();
    }

    public static void sendCommand(Command command) {
        // Toujours regénérer les bytes à l'envoi
        byte[] commandBytes = command.getBytes();
        pus.write(commandBytes);
    }
}
