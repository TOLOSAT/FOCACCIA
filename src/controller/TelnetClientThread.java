package controller;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TelnetClientThread extends Thread {
    private final BlockingQueue<String> commandQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<String> responseQueue = new LinkedBlockingQueue<>();
    private final Socket socket;
    private final BufferedReader reader;
    private final BufferedWriter writer;
    private volatile boolean running = true;

    public TelnetClientThread(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void run() {
        new Thread(() -> {
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[Telnet <-] " + line);
                    responseQueue.offer(line); // stocker la réponse pour la lecture synchrone
                }
            } catch (IOException e) {
                System.err.println("Erreur de lecture Telnet : " + e.getMessage());
            }
        }).start();

        try {
            while (running) {
                String command = commandQueue.take(); // bloquant
                writer.write(command + "\n");
                writer.flush();
                System.out.println("[Telnet ->] " + command);
            }
        } catch (Exception e) {
            System.err.println("Erreur dans le thread Telnet : " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }

    public void sendCommand(String cmd) {
        commandQueue.offer(cmd);
    }

    public String sendCommandAndWaitForResponse(String cmd, long timeoutMillis) throws InterruptedException, IOException {
        commandQueue.offer(cmd);
        writer.write(cmd + "\n");
        writer.flush();
        System.out.println("[Telnet ->] " + cmd);

        StringBuilder response = new StringBuilder();
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < timeoutMillis) {
            String line = responseQueue.poll(1000, TimeUnit.MILLISECONDS);
            if (line != null) {
                response.append(line).append("\n");
            }
        }
        return response.toString();
    }

    public String sendCommandAndReadUntilIdle(String cmd, int timeoutMillis) throws IOException, InterruptedException {
        StringBuilder response = new StringBuilder();
        sendCommand(cmd);

        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < timeoutMillis) {
            if (this.reader.ready()) {
                String line = this.reader.readLine();
                if (line != null) {
                    response.append(line).append("\n");
                }
            } else {
                Thread.sleep(50); // éviter busy-wait
            }
        }
        return response.toString();
    }


    public void stopClient() {
        running = false;
        this.interrupt(); // Pour débloquer .take()
        try {
            socket.close(); // Fermer proprement
        } catch (IOException ignored) {}
    }
}
