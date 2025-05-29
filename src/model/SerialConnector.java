package model;

import java.awt.Color;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortInvalidPortException;

import model.config.Config;
import model.config.PortConfig;
import view.components.UartConsole;

public class SerialConnector {
    private String name;
    private PortConfig portConfig;
    private SerialPort port;
    private byte[] buffer;
    private Color color;

    private static final int SKIP_LINES = 5;

    public SerialConnector(String name) {
        this(name, Color.BLACK);
    }

    public SerialConnector(String name, Color color) {
        this.name = name;

        // Parse the port name and baud rate from json configuration
        try {
            // Read the JSON file as a string
            String json = Files.readString(Path.of("config/port.config.json"));

            // Parse the JSON string into a Config object without ObjectMapper
            ObjectMapper mapper = new ObjectMapper();
            Config config = mapper.readValue(json, Config.class);

            // Get the port configuration for the specified name
            this.portConfig = config.getPort(name);
        } catch (Exception e) {
            System.err.println("Error reading port configuration: " + e.getMessage());
        }

        // Initialize the buffer to read data
        this.buffer = new byte[1024];
        this.color = color;
    }

    public void connect() {
        if (portConfig != null) {
            // Connect to the serial port using the port configuration
            System.out.println("Connecting to port: " + portConfig.port() + " with baud rate: " + portConfig.baud());

            try {
                this.port = SerialPort.getCommPort(portConfig.port());
                port.setBaudRate(portConfig.baud());
                port.setNumDataBits(portConfig.dataBits());
                port.setNumStopBits(portConfig.stopBits());
                port.setParity(portConfig.parity());
                port.addDataListener(
                    new SerialPortDataListener() {
                        @Override public int getListeningEvents() {
                            return SerialPort.LISTENING_EVENT_PORT_DISCONNECTED;
                        }

                        @Override
                        public void serialEvent(SerialPortEvent serialPortEvent) {
                            if (serialPortEvent.getEventType() == SerialPort.LISTENING_EVENT_PORT_DISCONNECTED)
                                port.closePort();
                        }
                    }
                );

                if (port.openPort()) {
                    System.out.println("Port " + portConfig.port() + " opened successfully.");
                } else {
                    System.err.println("Failed to open port " + portConfig.port() + ".");
                }
            } catch (SerialPortInvalidPortException e) {
                // Wait for the port to be available
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e1) {
                    Thread.currentThread().interrupt();
                }
            }
        } else {
            System.err.println("No port configuration found for the specified name.");
        }
    }

    public void readLines() {
        StringBuilder lineBuffer = new StringBuilder();
        int i = 0; // Counter to skip the first few lines

        while (true) {
            if(port != null && port.isOpen()) {
                while (port.bytesAvailable() > 0) {
                    byte[] singleByte = new byte[1];
                    int numRead = port.readBytes(singleByte, 1);
                    if (numRead > 0) {
                        char c = (char) (singleByte[0] & 0xFF); // ISO-8859-1 safe cast
                        if (c == '\n' || c == '\r') {
                            if (lineBuffer.length() > 0) {
                                // Ignore the first few lines
                                if (i < SKIP_LINES) {
                                    i++;
                                } else {
                                    // Afficher la ligne dans la console
                                    System.out.println(String.format("[%s] - %s", name, lineBuffer.toString()));
                                    UartConsole.appendLine(String.format("[%s] - %s", name, lineBuffer.toString()), color);
                                }
                                lineBuffer.setLength(0);
                            }
                        } else {
                            lineBuffer.append(c);
                        }
                    }
                }
            } else {
                System.err.println("Port is not open or not initialized. Trying to connect...");
                connect();
            }

            // Petite pause pour éviter de monopoliser le CPU
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void read() {
        StringBuilder lineBuffer = new StringBuilder();
        int i = 0; // Counter to skip the first few lines

        while (true) {
            if(port != null && port.isOpen()) {
                while (port.bytesAvailable() > 0) {
                    int numRead = port.readBytes(buffer, buffer.length);
                    if (numRead > 0) {
                        // Convert the read bytes to a string of hex values
                        String hexString = HexFormat.ofDelimiter(":").formatHex(buffer, 0, numRead);

                        System.out.println(String.format("[%s] - %s", name, hexString));
                        UartConsole.appendLine(String.format("[%s] - %s", name, hexString), color);
                    }
                }
            } else {
                connect();
            }

            // Petite pause pour éviter de monopoliser le CPU
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void write(byte[] data) {
        if (port != null && port.isOpen()) {
            int bytesWritten = port.writeBytes(data, data.length);
            if (bytesWritten > 0) {
                System.out.println("Data written successfully to port " + portConfig.port() + ".");
            } else {
                System.err.println("Failed to write data to port " + portConfig.port() + ".");
            }
        } else {
            System.err.println("Port is not open or not initialized. Trying to connect...");
            connect();
        }
    }

    public void close() {
        if (port != null && port.isOpen()) {
            port.closePort();
            System.out.println("Port " + portConfig.port() + " closed successfully.");
        } else {
            System.err.println("Port is not open or already closed.");
        }
    }
}
