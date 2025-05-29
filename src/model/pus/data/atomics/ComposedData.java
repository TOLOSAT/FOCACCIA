package model.pus.data.atomics;

import java.awt.Component;
import java.util.HexFormat;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.event.ChangeListener;

import model.pus.data.PusData;

public class ComposedData extends PusData {
    private final PusData[] data;

    public ComposedData(PusData... data) {
        this.data = data;
    }

    @Override
    public byte[] getBytes() {
        int totalLength = 0;
        for (PusData d : data) {
            totalLength += d.getBytes().length;
        }

        byte[] result = new byte[totalLength];
        int index = 0;
        for (PusData d : data) {
            byte[] bytes = d.getBytes();
            System.arraycopy(bytes, 0, result, index, bytes.length);
            index += bytes.length;
        }

        System.out.println("data : " + HexFormat.ofDelimiter(":").formatHex(result));

        return result;
    }

    @Override
    public Component getComponent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        boolean first = true;
        for (PusData d : data) {
            Component component = d.getComponent();
            if (component != null) {
                if (!first) {
                    panel.add(Box.createHorizontalStrut(10));
                }
                panel.add(component);
                first = false;
            }
        }
        return panel;
    }

    @Override
    public void setChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
        for (PusData d : data) {
            d.setChangeListener(e -> {
                if (this.changeListener != null) {
                    this.changeListener.stateChanged(e);
                }
            });
        }
    }
}
