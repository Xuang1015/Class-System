package panel;

import frame.AddVoteFrame;
import frame.BaseFrame;
import frame.VoteProcessFrame;

import javax.swing.*;

public class AdminVotePanel extends VotePanel {
    private JButton addVoteButton;
    private JButton summarizeVoteButton;

    public AdminVotePanel() {
        addVoteButton = new JButton("发起投票");
        addVoteButton.addActionListener(e -> BaseFrame.showFrame(new AddVoteFrame(),null));
        changePagePanel.add(addVoteButton);

        summarizeVoteButton = new JButton("投票进度");
        addVoteButton.addActionListener(e -> BaseFrame.showFrame(new VoteProcessFrame(),null));
        changePagePanel.add(summarizeVoteButton);
    }
}
