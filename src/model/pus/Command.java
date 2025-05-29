package model.pus;

import java.awt.Component;
import java.util.HexFormat;

import javax.swing.event.ChangeListener;

import model.pus.data.PusData;
import model.pus.data.atomics.RawData;
import utils.crc.CRC16;

public class Command {
    private static final String COMMAND_FORMAT = "18:55:c0:00:00:%02x:29:%02x:%02x:00:00";

    private ChangeListener changeListener;

    private int service;
    private int subservice;
    private byte[] command;
    private byte[] header;
    private byte[] data;
    private PusData dataObject;

    public Command(int service, int subservice, PusData data) {
        this.service = service;
        this.subservice = subservice;
        this.dataObject = data;
        this.data = data.getBytes();

        this.header = new RawData(
            String.format(
                COMMAND_FORMAT,
                this.data.length + 6,
                service,
                subservice
            )
        ).getBytes();

        byte[] tmp = new byte[this.header.length + this.data.length];

        System.arraycopy(this.header, 0, tmp, 0, this.header.length);
        System.arraycopy(this.data, 0, tmp, this.header.length, this.data.length);

        byte[] checksum = CRC16.CRC16_CCITT_FALSE(tmp);

        this.command = new byte[tmp.length + checksum.length];
        System.arraycopy(tmp, 0, this.command, 0, tmp.length);
        System.arraycopy(checksum, 0, this.command, tmp.length, checksum.length);
    }

    public byte[] getBytes() {
        // Toujours regénérer la commande à partir des données actuelles
        this.data = dataObject.getBytes();
        this.header = new RawData(
            String.format(
                COMMAND_FORMAT,
                this.data.length + 6,
                service,
                subservice
            )
        ).getBytes();
        byte[] tmp = new byte[this.header.length + this.data.length];
        System.arraycopy(this.header, 0, tmp, 0, this.header.length);
        System.arraycopy(this.data, 0, tmp, this.header.length, this.data.length);
        byte[] checksum = CRC16.CRC16_CCITT_FALSE(tmp);
        this.command = new byte[tmp.length + checksum.length];
        System.arraycopy(tmp, 0, this.command, 0, tmp.length);
        System.arraycopy(checksum, 0, this.command, tmp.length, checksum.length);
        return this.command;
    }

    public Component getComponent() {
        this.dataObject.setChangeListener(e -> {
            this.command = this.getBytes();

            if (this.changeListener != null) {
                this.changeListener.stateChanged(e);
            }

            System.out.println("[LISTENER] - Command updated: " + HexFormat.ofDelimiter(":").formatHex(this.command));
        });

        Component component = this.dataObject.getComponent();

        return component;
    }

    public void setChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }
}
