package model.pus.data;

import java.awt.Component;

import javax.swing.event.ChangeListener;

public abstract class PusData {
    protected ChangeListener changeListener;

    public abstract byte[] getBytes();
    public abstract Component getComponent();

    public void setChangeListener(ChangeListener listener) {
        this.changeListener = listener;
    }
}
