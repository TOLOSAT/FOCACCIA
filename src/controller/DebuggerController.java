package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

import com.fasterxml.jackson.databind.ObjectMapper;

import controller.DebuggerController.DebuggerMode;
import model.config.Config;
import model.config.TargetConfig;

public class DebuggerController {
    public enum DebuggerMode {
        BOOTLOADER_MODE,
        FLASH_MODE
    }

    private static final long BOOTLOADER_ADDR = 0x00000800L;
    private static final long FLASH_ADDR = 0x08000000L;

    private Process openOcdProcess;
    private TelnetClientThread telnetClient;

    private String bootloader_path = "";
    private String flash_path = "";

    private DebuggerMode currentMode = DebuggerMode.FLASH_MODE;

    public DebuggerController() {
        try {
            // Read the JSON file as a string
            String json = Files.readString(Path.of("config/target.config.json"));

            // Parse the JSON string into a Config object without ObjectMapper
            ObjectMapper mapper = new ObjectMapper();
            TargetConfig config = mapper.readValue(json, TargetConfig.class);

            // Get the port configuration for the specified name
            this.bootloader_path = config.getPath("bootloader");
            this.flash_path = config.getPath("path");
        } catch (Exception e) {
            System.err.println("Error reading port configuration: " + e.getMessage());
        }
    }

    public void init() {
        ProcessBuilder pb = new ProcessBuilder(
            "openocd",
            "-f", "interface/stlink.cfg",
            "-f", "target/stm32h7x.cfg",
            "-c", "init",
            "-c", "reset init"
        );

        pb.redirectErrorStream(true);

        Thread thread = new Thread(() -> {
            try {
                openOcdProcess = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(openOcdProcess.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[OpenOCD] " + line);
                }

                int exitCode = openOcdProcess.waitFor();
                System.out.println("OpenOCD exited with code " + exitCode);

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.setDaemon(true);
        thread.start();

        try {
            Thread.sleep(2000); // Laisser OpenOCD démarrer
            telnetClient = new TelnetClientThread("localhost", 4444);
            telnetClient.start();
            Thread.sleep(1000); // Laisser le client se connecter

            String response = telnetClient.sendCommandAndWaitForResponse("stm32h7x option_read 0 0x040", 1000);
            System.out.println("[Boot Addr Read] " + response);

            long bootAddr = parseBootAddress(response);
            System.out.println("Parsed boot address: 0x" + Long.toHexString(bootAddr));

            if (bootAddr == FLASH_ADDR) {
                currentMode = DebuggerMode.FLASH_MODE;
                System.out.println("Current mode set to FLASH_MODE.");
            } else if (bootAddr == BOOTLOADER_ADDR) {
                currentMode = DebuggerMode.BOOTLOADER_MODE;
                System.out.println("Current mode set to BOOTLOADER_MODE.");
            } else {
                System.err.println("Unknown boot address, defaulting to FLASH_MODE.");
                currentMode = DebuggerMode.FLASH_MODE;
            }

            telnetClient.sendCommand("reset");

        } catch (IOException | InterruptedException e) {
            System.err.println("Failed to connect to OpenOCD Telnet server: " + e.getMessage());
            return;
        }

        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    public void switchMode(DebuggerMode mode) {
        String command;
        switch (mode) {
            case BOOTLOADER_MODE:
                command = String.format("stm32h7x option_write 0 0x044 0x%08X", BOOTLOADER_ADDR);
                break;
            case FLASH_MODE:
                command = String.format("stm32h7x option_write 0 0x044 0x%08X", FLASH_ADDR);
                break;
            default:
                throw new IllegalArgumentException("Unknown Debugger Mode: " + mode);
        }

        telnetClient.sendCommand("reset halt");
        telnetClient.sendCommand(command);
    }

    public void uploadBootLoader() {
        telnetClient.sendCommand("reset halt");
        telnetClient.sendCommand("reset init");
        telnetClient.sendCommand(String.format("program %s", bootloader_path));
        telnetClient.sendCommand("reset");
    }

    public void uploadFlash() {
        telnetClient.sendCommand("reset halt");
        telnetClient.sendCommand("reset init");
        telnetClient.sendCommand(String.format("load_image %s", flash_path));
        telnetClient.sendCommand("reset");
    }

    public DebuggerMode getCurrentMode() {
        return currentMode;
    }

    private long parseBootAddress(String response) {
        for (String line : response.split("\\R")) { // \R = any linebreak
            if (line.contains("Option Register")) {
                int eqIndex = line.indexOf('=');
                if (eqIndex != -1) {
                    try {
                        String addrStr = line.substring(eqIndex + 1).trim().replace("0x", "");
                        return Long.parseLong(addrStr, 16);
                    } catch (NumberFormatException e) {
                        System.err.println("Failed to parse boot address: " + e.getMessage());
                    }
                }
            }
        }
        return -1; // Unknown address
    }

    public void stop() {
        System.out.println("Shutting down DebuggerController...");

        if (telnetClient != null) {
            telnetClient.stopClient();
        }

        if (openOcdProcess != null && openOcdProcess.isAlive()) {
            openOcdProcess.destroy();
            try {
                if (!openOcdProcess.waitFor(2, java.util.concurrent.TimeUnit.SECONDS)) {
                    openOcdProcess.destroyForcibly();
                }
            } catch (InterruptedException ignored) {}
        }

        System.out.println("DebuggerController shut down.");
    }
}
