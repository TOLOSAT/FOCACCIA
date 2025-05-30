package view.pages;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.Timer;

import controller.DebuggerController;

public class DebuggerPage extends JPanel {

    private final DebuggerController controller;

    public DebuggerPage(DebuggerController controller) {
        super();
        this.controller = controller;

        JPanel bootloaderSwitch = new JPanel();

        JLabel switchLabel = new JLabel("Bootloader Mode:");

        JToggleButton switchButton = new JToggleButton() {
            private float animPosition = 0f;
            private Timer animationTimer;
            private final int animDuration = 150;
            private long animStartTime;
            private boolean animatingToOn;

            {
                setPreferredSize(new Dimension(40, 20)); // Taille ajustable
                setFocusPainted(false);
                setBorderPainted(false);
                setContentAreaFilled(false);

                addActionListener(e -> startAnimation(isSelected()));

                if (controller.getCurrentMode() == DebuggerController.DebuggerMode.BOOTLOADER_MODE) {
                    this.setSelected(true);
                    this.startAnimation(true);
                } else {
                    this.setSelected(false);
                    this.startAnimation(false);
                }
            }

            private void startAnimation(boolean toOn) {
                animatingToOn = toOn;
                animStartTime = System.currentTimeMillis();

                if (animationTimer != null && animationTimer.isRunning()) {
                    animationTimer.stop();
                }

                animationTimer = new Timer(10, new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        long elapsed = System.currentTimeMillis() - animStartTime;
                        float progress = Math.min(1f, elapsed / (float) animDuration);
                        animPosition = animatingToOn ? progress : 1f - progress;

                        repaint();
                        if (progress >= 1f) {
                            animationTimer.stop();
                        }
                    }
                });
                animationTimer.start();
            }

            private Color blend(Color c1, Color c2, float ratio) {
                int red = (int) (c1.getRed() * (1 - ratio) + c2.getRed() * ratio);
                int green = (int) (c1.getGreen() * (1 - ratio) + c2.getGreen() * ratio);
                int blue = (int) (c1.getBlue() * (1 - ratio) + c2.getBlue() * ratio);
                return new Color(red, green, blue);
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g); // appel au cas où

                Graphics2D g2 = (Graphics2D) g.create();
                int width = getWidth();
                int height = getHeight();
                int margin = 4;

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color backgroundOn = Color.DARK_GRAY;
                Color backgroundOff = Color.LIGHT_GRAY;
                Color knobColor = Color.WHITE;

                Color background = blend(backgroundOff, backgroundOn, animPosition);
                g2.setColor(background);
                g2.fillRoundRect(0, 0, width, height, height, height);

                int knobDiameter = height - 2 * margin;
                int knobX = (int) (animPosition * (width - knobDiameter - 2 * margin)) + margin;
                int knobY = margin;

                g2.setColor(knobColor);
                g2.fillOval(knobX, knobY, knobDiameter, knobDiameter);

                g2.dispose();
            }
        };

        switchButton.addActionListener(e -> {
            if (switchButton.isSelected()) {
                this.controller.switchMode(DebuggerController.DebuggerMode.BOOTLOADER_MODE);
            } else {
                this.controller.switchMode(DebuggerController.DebuggerMode.FLASH_MODE);
            }
        });

        Button uploadButton = new Button("Upload");
        uploadButton.addActionListener(e -> {
            if (controller.getCurrentMode() == DebuggerController.DebuggerMode.BOOTLOADER_MODE) {
                System.out.println("==============================================");
                System.out.println("[INFO] - Uploading Bootloader...");
                controller.uploadBootLoader();
            } else {
                System.out.println("==============================================");
                System.out.println("[INFO] - Uploading Flash...");
                controller.uploadFlash();
            }
        });
        uploadButton.setBounds(50, 50, 100, 30);

        bootloaderSwitch.add(switchLabel);
        bootloaderSwitch.add(switchButton);
        bootloaderSwitch.add(uploadButton);

        add(bootloaderSwitch);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }
}
