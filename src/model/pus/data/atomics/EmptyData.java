package model.pus.data.atomics;

import java.awt.Component;

import model.pus.data.PusData;

public class EmptyData extends PusData {
    @Override
    public byte[] getBytes() {
        return new byte[0];
    }

    @Override
    public Component getComponent() {
        return null;
    }
}
