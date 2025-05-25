package ui.components;

import java.awt.Font;
import java.nio.file.Path;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import debug.ErrorReport;
import debug.ErrorReportDecoder;
import debug.infos.Call;

public class ReportDisplay extends JPanel {
    public ReportDisplay(Path ErrorReport) {
        super();
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Get the error report
        ErrorReportDecoder decoder = new ErrorReportDecoder(ErrorReport.toString());
        ErrorReport report = decoder.decodeErrorReport();

        // Display the error report
        add(new LabeledPanel("Software version: " + report.getContext().getVersion(), false));
        add(new LabeledPanel("Software state: " + report.getContext().getState(), false));
        add(new LabeledPanel("Boot count: " + report.getContext().getBootCount(), false));
        add(new LabeledPanel("Failed boot count: " + report.getContext().getFailedBootCount(), false));

        add(new JSeparator());

        add(new LabeledPanel("Saved registers:", true));
        add(createTablePanel(new String[]{"Register", "Value"}, new Object[][]{
            {"r0", "0x" + Integer.toHexString(report.getSavedRegisters().r()[0])},
            {"r1", "0x" + Integer.toHexString(report.getSavedRegisters().r()[1])},
            {"r2", "0x" + Integer.toHexString(report.getSavedRegisters().r()[2])},
            {"r3", "0x" + Integer.toHexString(report.getSavedRegisters().r()[3])},
            {"r12", "0x" + Integer.toHexString(report.getSavedRegisters().r12())},
            {"lr", "0x" + Integer.toHexString(report.getSavedRegisters().lr())},
            {"pc", "0x" + Integer.toHexString(report.getSavedRegisters().pc())},
            {"xpsr", "0x" + Integer.toHexString(report.getSavedRegisters().xpsr())}
        }));

        add(new JSeparator());

        add(new LabeledPanel("Faults:", true));
        add(createTablePanel(new String[]{"Fault", "Value"}, new Object[][]{
            {"cfsr", "0x" + Integer.toHexString(report.cfsr())},
            {"hfsr", "0x" + Integer.toHexString(report.hfsr())}
        }));

        add(new JSeparator());

        add(new LabeledPanel("Call stack:", true));
        Object[][] callStackData = new Object[report.getCallStack().calls().length][2];
        int i = 0;
        for (Call call : report.getCallStack().calls()) {
            callStackData[i][0] = "0x" + Integer.toHexString(call.lr());
            callStackData[i][1] = "0x" + Integer.toHexString(call.fp());
            i++;
        }
        add(createTablePanel(new String[]{"LR", "FP"}, callStackData));
    }

    private JScrollPane createTablePanel(String[] columnNames, Object[][] data) {
        JTable table = new JTable(data, columnNames);
        table.setGridColor(new java.awt.Color(238, 238, 238));
        table.setEnabled(false);
        table.setRowHeight(25);
        table.setFont(new Font("Monospaced", Font.PLAIN, 12));
        table.getColumnModel().getColumn(0).setWidth(50);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setAlignmentX(CENTER_ALIGNMENT);
        return scrollPane;
    }
}
