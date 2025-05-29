package view.pages;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import view.components.ContextDisplay;

public class ReportPage extends JPanel {
    private ContextDisplay contextDisplay = new ContextDisplay();

    public ReportPage() {
        super();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // // Add a file selector to select the error report file
        // FileSelector fileSelector = new FileSelector();
        // ErrorReportDecoder decoder = new ErrorReportDecoder(fileSelector.getFile().toString());
        // ErrorReport report = decoder.decodeErrorReport();
        // add(fileSelector);

        // // Display the error report
        // ContextDisplay contextDisplay = new ContextDisplay(report);
        // add(contextDisplay);
    }
}
