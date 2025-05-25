package ui.components;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import debug.ErrorReport;

public class ContextDisplay extends JPanel {
    public ContextDisplay() {
        super();
    }

    public ContextDisplay(ErrorReport report) {
        super();

        add(new LabeledPanel("Software version: " + report.getContext().getVersion(), false));
        add(new LabeledPanel("Software state: " + report.getContext().getState(), false));
        add(new LabeledPanel("Boot count: " + report.getContext().getBootCount(), false));
        add(new LabeledPanel("Failed boot count: " + report.getContext().getFailedBootCount(), false));


        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }
}
