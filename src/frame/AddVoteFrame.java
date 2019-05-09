package frame;

import org.json.JSONException;
import util.UIDesign;
import bean.Choice;
import util.URLCommunicate;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddVoteFrame extends BaseFrame {
    private JPanel panel;
    private GridBagConstraints gridBagConstraints;
    private JScrollPane contentPanel;
    private JTextArea contentArea;
    private JLabel choiceLabel;
    private JTextField addChoiceField;
    private JLabel conLabel;
    private JLabel choLabel;
    private ArrayList<bean.Choice> choices = new ArrayList<>();
    private JPanel choicePanel;
    private JList choiceList;
    private JPanel toolPanel;
    private JButton addChoiceButton;
    private JButton deleteChoiceButton;
    private JButton publishButton;
    private JLabel emptyLabel;
    private long choiceId = 0;
    private int xOld;
    private int yOld;

    @Override
    public JComponent generate() {
        panel = new JPanel();
        gridBagConstraints = new GridBagConstraints();
        panel.setLayout(new GridBagLayout());
        panel.setPreferredSize(new Dimension(550,500));
        panel.setBorder(new EmptyBorder(0,0,30,0));
        panel.setBackground(new Color(0x52B7F7));

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
                AddVoteFrame.this.setLocation(xx, yy);//设置拖拽后，窗口的位置
            }
        });

        JButton mini = icons[0];
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        UIDesign.getMinimizeFunction(mini, this);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        panel.add(mini, gridBagConstraints);

        JButton close = icons[1];
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        UIDesign.getCloseFunction(close, this);
        panel.add(close, gridBagConstraints);
        // 添加事项内容
        conLabel = new JLabel("事项内容:");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weighty = 0.8;
        panel.add(conLabel, gridBagConstraints);

        contentArea = new JTextArea();
        contentPanel = new JScrollPane(contentArea);
        contentPanel.setPreferredSize(new Dimension(300, 200));
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panel.add(contentPanel, gridBagConstraints);
        // 添加选项
        choLabel = new JLabel("添加选项：");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panel.add(choLabel, gridBagConstraints);

        addChoiceField = new JTextField();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        addChoiceField.setPreferredSize(new Dimension(300, 30));
        panel.add(addChoiceField, gridBagConstraints);
        // 显示现有的选项
        choiceLabel = new JLabel("现有选项:");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        panel.add(choiceLabel, gridBagConstraints);

        choiceList = new JList();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        choiceList.setListData(choices.toArray());
        choiceList.setBackground(Color.WHITE);
        choiceList.setPreferredSize(new Dimension(300, 120));
        panel.add(choiceList, gridBagConstraints);
        // 功能栏
        toolPanel = new JPanel();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        panel.add(toolPanel, gridBagConstraints);
        // 添加选项
        addChoiceButton = new JButton("添加选项");
        addChoiceButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addChoiceButton.addActionListener(e -> {
            if (addChoiceField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(panel, "请勿添加空选项！");
            } else {
                addChoice(addChoiceField.getText());
                choiceList.updateUI();
                addChoiceField.setText(null);
            }
        });
        toolPanel.add(addChoiceButton);
        // 删除选项
        deleteChoiceButton = new JButton("删除选项");
        deleteChoiceButton.setEnabled(false);
        deleteChoiceButton.setToolTipText("请先选择你想要删除的选项，再点击此按钮");
        choiceList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (choiceList.getSelectedIndex() != -1) {
                    deleteChoiceButton.setEnabled(true);
                    deleteChoiceButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            }
        });
        deleteChoiceButton.addActionListener(e -> {
            choices.remove(choiceList.getSelectedIndex());
            choiceList.setListData(choices.toArray());
            choiceList.updateUI();
            deleteChoiceButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            deleteChoiceButton.setEnabled(false);
        });
        toolPanel.add(deleteChoiceButton);
        // 插入一个空的Label，把提交和其他的区别开来
        emptyLabel = new JLabel("   ");
        toolPanel.add(emptyLabel);
        // 提交事项
        publishButton = new JButton("发布");
        publishButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        publishButton.addActionListener(e -> {
            if (contentArea.getText().isEmpty() || choices.size() == 0) {
                JOptionPane.showMessageDialog(panel, "请勿提交空的内容或选项！");
            } else {
                // 先传内容
                new Thread(() -> {
                    Map<String, Object> putcontent = new HashMap<>();
                    putcontent.put("announcer", LoginFrame.username);
                    putcontent.put("content", contentArea.getText());
                    try {
                        URLCommunicate.post("http://121.250.216.117:8080/vote/add", putcontent);
                    } catch (IOException | JSONException e1) {
                        e1.printStackTrace();
                    }
                }).start();
                // 再传选项
                new Thread(() -> {
                    Map<String, Object> putChoices = new HashMap<>();
                    for (Choice choice : choices) {
                        putChoices.put("voteId", choice.getChoiceId());
                        putChoices.put("content", choice.getChoiceContent());
                    }
                    try {
                        URLCommunicate.post("http://121.250.216.117:8080/vote/addChoice", putChoices);
                    } catch (IOException | JSONException e1) {
                        e1.printStackTrace();
                    }
                }).start();
            }
        });
        toolPanel.add(publishButton);

        return panel;
    }

    private void addChoice(String content) {
        Choice choiceAdded = new Choice();
        choiceAdded.setChoiceContent(content);
        choiceAdded.setChoiceId(choiceId);
        choices.add(choiceAdded);
        choiceList.setListData(choices.toArray());
        choiceId++;
    }
}
