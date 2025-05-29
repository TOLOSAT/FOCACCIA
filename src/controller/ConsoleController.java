package controller;

import java.awt.Color;

import model.SerialConnector;

public class ConsoleController {
    public SerialConnector console;

    public ConsoleController() {
        console = new SerialConnector("console", Color.DARK_GRAY);
        console.connect();
        console.readLines();
    }
}
