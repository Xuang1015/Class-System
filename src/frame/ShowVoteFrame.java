package frame;

import bean.Choice;
import bean.Vote;
import org.json.JSONException;
import panel.ShowVotePanel;
import util.UIDesign;
import util.URLCommunicate;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ShowVoteFrame extends BaseFrame {
    private JPanel showVotePanel;
    private GridBagLayout gridBagLayout;
    private GridBagConstraints gridBagConstraints;
    private JScrollPane contentPanel;
    private JTextArea contentArea;
    private JScrollPane publicOpinionPanel;
    private JTextArea publicOpinionArea;
    private JPanel choicePanel;
    private JScrollPane sendOpinionPanel;
    private JTextArea sendOpinionArea;
    private ButtonGroup buttonGroup;
    private Map components;
    private Choice[] choices = new Choice[0];
    private int xOld;
    private int yOld;

    @Override
    public JComponent generate() {
        components = getContent();
        showVotePanel = new JPanel();
        gridBagLayout = new GridBagLayout();
        gridBagConstraints = new GridBagConstraints();
        showVotePanel.setLayout(gridBagLayout);
        showVotePanel.setBackground(new Color(0x52B7F7));
        showVotePanel.setBorder(new EmptyBorder(0, 50, 30, 0));

        Font font = new Font(Font.DIALOG, Font.PLAIN, 30);

        choices = (Choice[]) components.get("choices");

        JButton[] icons = UIDesign.unDecorated(this);
        //处理拖动事件---去掉默认边框后，不能拖动了，具体实现如下
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                xOld = e.getX();//记录鼠标按下时的坐标
                yOld = e.getY();
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int xOnScreen = e.getXOnScreen();
                int yOnScreen = e.getYOnScreen();
                int xx = xOnScreen - xOld;
                int yy = yOnScreen - yOld;
                ShowVoteFrame.this.setLocation(xx, yy);//设置拖拽后，窗口的位置
            }
        });

        JButton mini = icons[0];
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        UIDesign.getMinimizeFunction(mini, this);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        showVotePanel.add(mini, gridBagConstraints);

        JButton close = icons[1];
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        UIDesign.getCloseFunction(close, this);
        showVotePanel.add(close, gridBagConstraints);

        contentArea = new JTextArea();
        contentArea.setFont(font);
        contentArea.setColumns(26);
        contentArea.setBackground(new Color(0xCEF0F7));
        contentPanel = new JScrollPane(contentArea);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        Vote[] votes = (Vote[]) components.get("votes");
        JTable voteTable = (JTable) components.get("voteTable");
        contentArea.append(votes[voteTable.getSelectedRow()].getContent());
        contentArea.setEditable(false);
        contentPanel.setPreferredSize(new Dimension(800, 600));
        showVotePanel.add(contentPanel, gridBagConstraints);

        choicePanel = new JPanel();
        buttonGroup = new ButtonGroup();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        choicePanel.setPreferredSize(new Dimension(800, 300));
        for (Choice choice : choices) {
            choice.getChoiceContent().setFont(font);
            choicePanel.add(choice.getChoiceContent());
            buttonGroup.add(choice.getChoiceContent());
        }
        showVotePanel.add(choicePanel, gridBagConstraints);

        publicOpinionArea = new JTextArea();
        publicOpinionArea.setFont(font);
        publicOpinionArea.setColumns(15);
        publicOpinionArea.setBackground(new Color(0xE6F4F7));
        publicOpinionPanel = new JScrollPane(publicOpinionArea);
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        for (String comment : (String[]) components.get("comments")) {
            publicOpinionArea.append(comment + "\n");
        }
        publicOpinionArea.setEditable(false);
        publicOpinionPanel.setPreferredSize(new Dimension(450, 600));
        showVotePanel.add(publicOpinionPanel, gridBagConstraints);

        sendOpinionArea = new JTextArea();
        sendOpinionArea.setFont(font);
        sendOpinionArea.setColumns(1);
        sendOpinionPanel = new JScrollPane(sendOpinionArea);
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        sendOpinionPanel.setPreferredSize(new Dimension(450, 300));
        sendOpinionArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
                    String opinion = sendOpinionArea.getText();
                    if (buttonGroup.getSelection() == null) {
                        JOptionPane.showMessageDialog(showVotePanel, "请先投票，再发表评论！");
                        return;
                    }
                    comment(opinion);
                    sendVote();
                    sendOpinionArea.setText(null);
                }
            }
        });
        showVotePanel.add(sendOpinionPanel, gridBagConstraints);

        return showVotePanel;
    }

    private void comment(String text) {
        Map<String, Object> output = new HashMap<>();
        output.put("voteId", components.get("voteId"));
        output.put("content", text);
        try {
            URLCommunicate.post("http://121.250.216.117:8080/vote/comment", output);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendVote() {
        Map<String, Object> output = new HashMap<>();
        output.put("voteId", components.get("voteId"));
        long result = 0;
        for (Choice choice: choices) {
            if (buttonGroup.getSelection().equals(choice.getChoiceContent())) {
                result = choice.getChoiceId();
            }
        }
        output.put("choiceId", result);
        try {
            URLCommunicate.post("http://121.250.216.117:8080/vote/choose", output);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
