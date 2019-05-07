package panel;

import javax.swing.*;
import java.awt.*;

public class AdminFilePanel extends FilePanel {
    private JButton deleteFileButton;

    public AdminFilePanel() {
        deleteFileButton = new JButton("删除文件");
        changePagePanel.add(deleteFileButton);
        deleteFileButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteFileButton.addActionListener(e -> deleteFile());
    }
}
