package model.pus.data.atomics.integers;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;

import model.pus.data.PusData;

public class UInt32Data extends PusData {
    private String name = "";
    private long value;
    private long min = 0;
    private long max = 0xFFFFFFFFL;

    public UInt32Data(long value) {
        if (value < 0 || value > 0xFFFFFFFFL) {
            throw new IllegalArgumentException("Value must be in the range [0, 2^32 - 1]");
        }
        this.value = value;
    }

    public UInt32Data(long min, long max) {
        if (min < 0 || max > 0xFFFFFFFFL || min > max) {
            throw new IllegalArgumentException("Invalid range for unsigned 32-bit integer");
        }
        this.value = min; // Default to the minimum value
        this.min = min;
        this.max = max;
    }

    public UInt32Data(String name, long value) {
        this(value);
        this.name = name;
    }

    public UInt32Data(String name, long min, long max) {
        this(min, max);
        this.name = name;
    }

    @Override
    public byte[] getBytes() {
        return new byte[]{
            (byte) (value & 0xFF),
            (byte) ((value >> 8) & 0xFF),
            (byte) ((value >> 24) & 0xFF),
            (byte) ((value >> 16) & 0xFF)
        };
    }

    @Override
    public Component getComponent() {
        JPanel panel = new JPanel();

        if (!name.isEmpty()) {
            panel.add(new JLabel(name + ": "));
        }

        // Integer selection widget to set the value (we want precise integer selection)
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(value, min, max, 1L)) {
            @Override
            public String getToolTipText() {
                return "Unsigned 32-bit integer value";
            }
        };

        spinner.setMaximumSize(new Dimension(70, 20));

        spinner.addChangeListener(e -> {
            Number n = (Number) spinner.getValue();
            value = n.longValue();
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
