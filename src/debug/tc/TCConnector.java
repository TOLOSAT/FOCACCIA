package debug.tc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fazecast.jSerialComm.SerialPort;

import utils.crc.CRC16;

public class TCConnector {
    public static List<SerialPort> listPorts() {
        SerialPort[] ports = SerialPort.getCommPorts();
        return Arrays.stream(ports)
            .collect(Collectors.toList());
    }

    private static SerialPort tcPort;

    public static Map<String, byte[]> commands = new HashMap<>();

    public static void connect() {
        commands.put("reboot_nominal", buildCommand(160, 1, HexFormat.ofDelimiter(":").parseHex("00:00:00:00")));
        commands.put("reboot_safe", buildCommand(160, 1, HexFormat.ofDelimiter(":").parseHex("01:00:00:00")));
        commands.put("select_safe_0", buildCommand(160, 2, HexFormat.ofDelimiter(":").parseHex("00:01:00:00:00")));
        commands.put("select_safe_1", buildCommand(160, 2, HexFormat.ofDelimiter(":").parseHex("01:01:00:00:00")));
        commands.put("select_nominal_0", buildCommand(160, 2, HexFormat.ofDelimiter(":").parseHex("00:00:00:00:00")));
        commands.put("select_nominal_1", buildCommand(160, 2, HexFormat.ofDelimiter(":").parseHex("01:00:00:00:00")));
        commands.put("select_nominal_2", buildCommand(160, 2, HexFormat.ofDelimiter(":").parseHex("02:00:00:00:00")));
        commands.put("request_context", buildCommand(160, 17, new byte[0]));
        commands.put("reset_context", buildCommand(160, 23, new byte[0]));

        // List<SerialPort> ports = listPorts();
        // for (SerialPort port : ports) {
        //     if(port.getSystemPortName().equals("tty.usbmodem11203")) {
        //         System.out.println("[LOG] - Connecting to port: " + port.getSystemPortName());
        //         port.setBaudRate(115200);
        //         port.setNumDataBits(8);
        //         port.setNumStopBits(SerialPort.ONE_STOP_BIT);
        //         port.setParity(SerialPort.NO_PARITY);
        //         System.out.println("[LOG] - Port " + port.getSystemPortName() + " configured successfully.");
        //         if (port.openPort()) {
        //             System.out.println("[LOG] - Port " + port.getSystemPortName() + " opened successfully.");
        //             tcPort = port;
        //         } else {
        //             System.err.println("[LOG] - Failed to open port " + port.getSystemPortName() + ".");
        //         }
        //     }
        // }
    }

    public static void sendCommand(byte[] command) {
        // TapasDebugger.pus.write(command);
        // int bytesWritten = tcPort.writeBytes(command, command.length);
        // if (bytesWritten > 0) {
        //     System.out.println("[LOG] - Command sent successfully.");
        // } else {
        //     System.err.println("[LOG] - Failed to send command.");
        // }
    }

    public static byte[] buildCommand(int service, int subservice, byte[] data) {
        String strHeader = String.format("18:55:c0:00:00:%02x:29:%02x:%02x:00:00", data.length + 6, service, subservice);
        byte[] header = HexFormat.ofDelimiter(":").parseHex(strHeader);
        byte[] command = new byte[header.length + data.length];
        System.arraycopy(header, 0, command, 0, header.length);
        System.arraycopy(data, 0, command, header.length, data.length);
        byte[] checksum = CRC16.CRC16_CCITT_FALSE(command);
        byte[] fullCommand = new byte[command.length + checksum.length];
        System.arraycopy(command, 0, fullCommand, 0, command.length);
        System.arraycopy(checksum, 0, fullCommand, command.length, checksum.length);
        return fullCommand;
    }
}
