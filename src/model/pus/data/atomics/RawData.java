package model.pus.data.atomics;

import java.awt.Component;
import java.util.HexFormat;

import model.pus.data.PusData;

public class RawData extends PusData {
    private final byte[] data;

    public RawData(String data) {
        this.data = HexFormat.ofDelimiter(":").parseHex(data);
    }

    @Override
    public byte[] getBytes() {
        return data;
    }

    @Override
    public Component getComponent() {
        // // Textual widget to set the raw data
        // return new javax.swing.JTextField(HexFormat.ofDelimiter(":").formatHex(data)) {
        //     @Override
        //     public String getToolTipText() {
        //         return "Raw data in hexadecimal format";
        //     }
        // };
        return null;
    }
}
