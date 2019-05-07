package panel;

import bean.Choice;

import javax.swing.*;
import java.awt.*;

public class ShowVotePanel extends VotePanel {
    private JPanel showVotePanel;
    private GridBagConstraints gridBagConstraints;
    private JScrollPane contentPanel;
    private JTextArea contentArea;
    private JScrollPane publicOpinionPanel;
    private JTextArea publicOpinionArea;
    private JPanel choicePanel;
    private JScrollPane sendOpinionPanel;
    private JTextArea sendOpinionArea;

    public ShowVotePanel() {
        showVotePanel = new JPanel();
        showVotePanel.setLayout(new GridBagLayout());
        gridBagConstraints = new GridBagConstraints();

        contentArea = new JTextArea();
        contentPanel = new JScrollPane(contentArea);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        contentArea.append(votes[select].getContent());
        contentArea.setEditable(false);
        contentPanel.setPreferredSize(new Dimension(800, 600));
        showVotePanel.add(contentPanel, gridBagConstraints);

        choicePanel = new JPanel();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        choicePanel.setPreferredSize(new Dimension(800, 300));
        for (Choice choice : choices) {
            choicePanel.add(choice.getChoiceContent());
        }
        showVotePanel.add(choicePanel, gridBagConstraints);

        publicOpinionArea = new JTextArea();
        publicOpinionArea.setOpaque(false);
        publicOpinionPanel = new JScrollPane(publicOpinionArea);
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        publicOpinionPanel.setBackground(new Color(0x5CE0F7));
        for (String comment : comments) {
            publicOpinionArea.append(comment + "\n" + "" + "\n");
        }
        publicOpinionArea.setEditable(false);
        publicOpinionPanel.setPreferredSize(new Dimension(380, 600));
        showVotePanel.add(publicOpinionPanel, gridBagConstraints);

        sendOpinionArea = new JTextArea();
        sendOpinionPanel = new JScrollPane(sendOpinionArea);
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        sendOpinionPanel.setPreferredSize(new Dimension(380, 300));
        showVotePanel.add(sendOpinionPanel, gridBagConstraints);
    }
}
