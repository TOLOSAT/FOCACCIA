package controller;

import debug.SerialConnector;

public class ConsoleController {
    public SerialConnector console;

    public ConsoleController() {
        console = new SerialConnector("console");
        console.connect();
        new Thread(() -> console.readLines()).start();
    }
}
