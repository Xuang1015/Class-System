package panel;

import bean.ChatMember;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class AddRoomPanel extends ChatPanel {
    private JPanel addroompanel;
    private JTextField nameField;
    private JLabel titleLabel;
    private JButton addRoomButton;
    private ChatMember[] selectMember = new ChatMember[0];
    private JLabel memLabel;
    private GridBagLayout gridBagLayout;
    private GridBagConstraints gridBagConstraints;

    public AddRoomPanel() {
        addroompanel = new JPanel();
        addroompanel.setPreferredSize(new Dimension(400,300));
        addroompanel.setLayout(gridBagLayout);
        gridBagLayout = new GridBagLayout();
        gridBagConstraints = new GridBagConstraints();

        titleLabel = new JLabel("房间名称");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        titleLabel.setPreferredSize(new Dimension(70,32));
        addroompanel.add(titleLabel,gridBagConstraints);

        nameField = new JTextField();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        nameField.setPreferredSize(new Dimension(100,32));
        addroompanel.add(nameField,gridBagConstraints);

        addRoomButton = new JButton("确认添加");
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        addroompanel.add(addRoomButton,gridBagConstraints);
        int j = 0;
        for (int i: memberList.getSelectedIndices()) {
            selectMember[j] = members[i];
            j++;
        }
        addRoomButton.addActionListener(e -> addRoom(nameField.getText(), selectMember));
    }

    @Override
    public JPanel getPanel() {
        return addroompanel;
    }
}
