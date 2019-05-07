package frame;

import javax.swing.*;
import java.awt.*;

public class VoteProcessFrame extends BaseFrame {
    private JPanel panel;
    private GridBagConstraints gridBagConstraints;

    @Override
    public JComponent generate() {
        panel = new JPanel();
        gridBagConstraints = new GridBagConstraints();
        return panel;
    }
}
