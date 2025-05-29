import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.FileReader;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import com.fasterxml.jackson.core.JsonParser;
import com.fazecast.jSerialComm.SerialPort;

import controller.ConsoleController;
import controller.PusController;
import model.SerialConnector;
import view.pages.ConsolePage;
import view.pages.HomePage;
import view.pages.ReportPage;

public class MainApplication extends JFrame {
    final static String HOMEPAGE = "Home";
    final static String CONSOLE = "Console";
    final static String REPORT = "Report";

    public MainApplication() {
        // Set up the window
        super("Focaccia - Tapas Debugger");

        new Thread(() -> {
            new ConsoleController();
        }).start();

        new Thread(() -> {
            new PusController();
        }).start();

        // FLASH_KEYR1 (0x004)
        // FLASH_CR1 (0x00C)

        // FLASH_OPTKEYR (0x008)
        // FLASH_OPTCR (0x018)

        // BOOT_ADDR (0x040)

        /**
         * reset halt
         * halt
         * reset init
         *
         * stm32h7x option_write 0 0x004 0x45670123
         * stm32h7x option_write 0 0x004 0xCDEF89AB
         * stm32h7x option_read 0 0x00C
         *
         * stm32h7x option_write 0 0x008 0x08192A3B
         * stm32h7x option_write 0 0x008 0x4C5D6E7F
         * stm32h7x option_read 0 0x018
         *
         * stm32h7x option_write 0 0x040 0x09000000
         * stm32h7x option_read 0 0x040
         */

        /**
         * More specifically, write operations to embedded flash memory control registers
         * (FLASH_CR1/2 and FLASH_OPTCR) are not allowed after reset.
         *
         *  The following sequence must be used to unlock FLASH_CR1/2 register:
         *   1. Program KEY1 to 0x45670123 in FLASH_KEYR1/2 key register.
         *   2. Program KEY2 to 0xCDEF89AB in FLASH_KEYR1/2 key register.
         *   3. LOCK1/2 bit is now cleared and FLASH_CR1/2 is unlocked.
         *
         *  The following sequence must be used to unlock FLASH_OPTCR register:
         *   1. Program OPTKEY1 to 0x08192A3B in FLASH_OPTKEYR option key register.
         *   2. Program OPTKEY2 to 0x4C5D6E7F in FLASH_OPTKEYR option key register.
         *   3. OPTLOCK bit is now cleared and FLASH_OPTCR register is unlocked.
         *
         *  Any wrong sequence locks up the corresponding register/bit until the next system reset, and
         * generates a bus error.
         *
         * The FLASH_CR1/2 (respectively FLASH_OPTCR) register can be locked again by software
         * by setting the LOCK1/2 bit in FLASH_CR1/2 register (respectively OPTLOCK bit in
         * FLASH_OPTCR register).
         */

        WindowListener l = new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        };

        addWindowListener(l);
        setSize(1000,800);
        setVisible(true);

        // Create the tabbed pane
        JTabbedPane cards = new JTabbedPane();
        cards.addTab(HOMEPAGE, new HomePage());
        cards.addTab(CONSOLE, new ConsolePage());
        cards.addTab(REPORT, new ReportPage());

        add(cards, BorderLayout.CENTER);
    }

    public static void main(String [] args){
        new MainApplication().setVisible(true);
    }
}