package frame;

import panel.AddRoomPanel;
import util.UIDesign;

import javax.swing.*;
import java.awt.*;

public class AddRoomFrame extends BaseFrame {
    private JPanel panel;
    private GridBagLayout gridBagLayout;
    private GridBagConstraints gridBagConstraints;
    private AddRoomPanel contentPanel;

    @Override
    public JComponent generate() {
        panel = new JPanel();
        gridBagLayout = new GridBagLayout();
        gridBagConstraints = new GridBagConstraints();
        panel.setLayout(gridBagLayout);
        panel.setBackground(new Color(0x52B7F7));

        JButton icons[] = UIDesign.unDecorated(this);

        JButton mini = icons[0];
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        UIDesign.getMinimizeFunction(mini, this);
        panel.add(mini, gridBagConstraints);

        JButton close = icons[1];
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        UIDesign.getCloseFunction(close, this);
        panel.add(close, gridBagConstraints);

        contentPanel = new AddRoomPanel();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panel.add(contentPanel.getPanel(),gridBagConstraints);

        return panel;
    }
}
