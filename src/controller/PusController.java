package controller;

import debug.SerialConnector;

public class PusController {
    public static SerialConnector pus;

    public PusController() {
        pus = new SerialConnector("pus");
        pus.connect();
        new Thread(() -> pus.read()).start();
    }

    public static void sendCommand(byte[] command) {
        pus.write(command);
    }
}
