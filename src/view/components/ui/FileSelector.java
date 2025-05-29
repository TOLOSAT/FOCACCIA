package view.components.ui;

import java.nio.file.Path;

import javax.swing.JFileChooser;

public class FileSelector extends JFileChooser {
    private Path selectedFile;

    public FileSelector() {
        super();

        setCurrentDirectory(new java.io.File("."));
        setFileSelectionMode(JFileChooser.FILES_ONLY);
        setMultiSelectionEnabled(false);

        int result = showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = getSelectedFile().toPath();
        }

        setVisible(false);
    }

    public Path getFile() {
        return selectedFile;
    }
}
