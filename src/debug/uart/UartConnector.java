package debug.uart;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fazecast.jSerialComm.SerialPort;

import ui.components.UartConsole;

public class UartConnector {
    public static List<SerialPort> listPorts() {
        SerialPort[] ports = SerialPort.getCommPorts();
        return Arrays.stream(ports)
            .collect(Collectors.toList());
    }

    public static void connectAndRead() {
        List<SerialPort> ports = listPorts();
        for (SerialPort port : ports) {
            if(port.getSystemPortName().equals("tty.usbserial-B0001LWG")) {
                System.out.println("[LOG] - Connecting to port: " + port.getSystemPortName());
                port.setBaudRate(115200);
                port.setNumDataBits(8);
                port.setNumStopBits(SerialPort.ONE_STOP_BIT);
                port.setParity(SerialPort.NO_PARITY);
                System.out.println("[LOG] - Port " + port.getSystemPortName() + " configured successfully.");
                if (port.openPort()) {
                    System.out.println("[LOG] - Port " + port.getSystemPortName() + " opened successfully.");
                    byte[] buffer = new byte[1024];
                    int skipLines = 5;

                    StringBuilder lineBuffer = new StringBuilder();

                    while (true) {
                        while (port.bytesAvailable() > 0) {
                            byte[] singleByte = new byte[1];
                            int numRead = port.readBytes(singleByte, 1);
                            if (numRead > 0) {
                                char c = (char) (singleByte[0] & 0xFF); // ISO-8859-1 safe cast
                                if (c == '\n' || c == '\r') {
                                    if (lineBuffer.length() > 0) {
                                        // Ignore the first few lines
                                        if (skipLines > 0) {
                                            skipLines--;
                                        } else {
                                            // Afficher la ligne dans la console
                                            UartConsole.appendLine(lineBuffer.toString(), Color.MAGENTA);
                                        }
                                        lineBuffer.setLength(0);
                                    }
                                } else {
                                    lineBuffer.append(c);
                                }
                            }
                        }

                        // Petite pause pour éviter de monopoliser le CPU
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                } else {
                    System.err.println("[LOG] - Failed to open port " + port.getSystemPortName() + ".");
                }
            }
        }
    }
}
