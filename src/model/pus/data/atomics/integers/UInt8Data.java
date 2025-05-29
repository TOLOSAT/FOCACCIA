package model.pus.data.atomics.integers;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;

import model.pus.data.PusData;

public class UInt8Data extends PusData {
    private String name = "";
    private int value;
    private int min = 0;
    private int max = 0xFFF;

    public UInt8Data(int value) {
        if (value < 0 || value > 0xFF) {
            throw new IllegalArgumentException("Value must be in the range [0, 2^8 - 1]");
        }
        this.value = value;
    }

    public UInt8Data(int min, int max) {
        if (min < 0 || max > 0xFF || min > max) {
            throw new IllegalArgumentException("Invalid range for unsigned 8-bit integer");
        }
        this.value = min; // Default to the minimum value
        this.min = min;
        this.max = max;
    }

    public UInt8Data(String name, int value) {
        this(value);
        this.name = name;
    }

    public UInt8Data(String name, int min, int max) {
        this(min, max);
        this.name = name;
    }

    @Override
    public byte[] getBytes() {
        return new byte[]{
            (byte) value
        };
    }

    @Override
    public Component getComponent() {
        JPanel panel = new JPanel();

        if (!name.isEmpty()) {
            panel.add(new JLabel(name + ": "));
        }

        // Integer selection widget to set the value
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(value, min, max, 1)) {
            @Override
            public String getToolTipText() {
                return "Unsigned 8-bit integer value";
            }
        };

        spinner.setMaximumSize(new Dimension(70, 20));

        spinner.addChangeListener(e -> {
            value = (int) spinner.getValue();
            System.out.println("[LISTENER] - Spinner value changed: " + value);

            if (changeListener != null) {
                changeListener.stateChanged(new ChangeEvent(this));
            }
        });

        panel.add(spinner);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        return panel;
    }
}
