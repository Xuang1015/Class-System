package frame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

import panel.ChatPanel;
import panel.FilePanel;
import panel.NoticePanel;
import panel.VotePanel;
import util.*;

public class MainFrame extends BaseFrame {

    // 用于实现菜单栏UI的类
    private static class MenuButton {
        private JButton button;
        private int status;


        public void changeStatus() {
            switch (status) {
                case 0: // 默认
                    button.setOpaque(false);
                    break;
                case 1: // 加深
                    button.setBackground(new Color(0x2E9FF7));
                    button.setOpaque(true);
                    break;
                case 2: // 白色
                    button.setBackground(Color.WHITE);
                    button.setOpaque(true);
                    break;
            }
            button.repaint();
        }
    }

    private JPanel panel; // 底部的面板容器
    private JPanel mainpanel;
    CardLayout cardLayout = new CardLayout(0, 0);
    GridBagLayout gridBagLayout = new GridBagLayout();
    GridBagConstraints gridBagConstraints = new GridBagConstraints();
    private JButton noticeButton;
    private JButton chatButton;
    private JButton fileButton;
    private JButton voteButton;
    private MenuButton[] menuButtons = new MenuButton[4];
    private JLabel emptyLabel;
    private NoticePanel noticePanel;
    private ChatPanel chatPanel;
    private FilePanel filePanel;
    private VotePanel votePanel;
    private ImageIcon noticeIcon;
    //用于处理拖动事件，表示鼠标按下时的坐标，相对于JFrame
    int xOld = 0;
    int yOld = 0;

    @Override
    public JComponent generate() {
        panel = new JPanel();
        panel.setBackground(new Color(0x52B7F7));
        setContentPane(panel);
        panel.setLayout(gridBagLayout);
        panel.setBorder(new EmptyBorder(0, 0, 20, 0));

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
                MainFrame.this.setLocation(xx, yy);//设置拖拽后，窗口的位置
            }
        });

        JButton mini = icons[0];
        gridBagConstraints.gridx = 19;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        UIDesign.getMinimizeFunction(mini, this);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        panel.add(mini, gridBagConstraints);

        JButton close = icons[1];
        gridBagConstraints.gridx = 20;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        UIDesign.getCloseFunction(close, this);
        panel.add(close, gridBagConstraints);
// 一、主面板为父容器的四个功能面板
        mainpanel = new JPanel();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 17;
        gridBagConstraints.gridheight = 10;
        mainpanel.setPreferredSize(new Dimension(1200, 900));
        panel.add(mainpanel, gridBagConstraints);
        mainpanel.setLayout(cardLayout);
    // 1.公告面板
        noticePanel = newInstanceNoticePanel();
        mainpanel.add("notice", noticePanel.getPanel());
    // 2.聊天功能面板
        chatPanel = new ChatPanel();
        mainpanel.add("chat", chatPanel.getPanel());
    // 3.文件共享面板
        filePanel = newInstanceFilePanel();
        mainpanel.add("file", filePanel.getPanel());
    // 4.传阅及投票面板
        votePanel = newInstanceVotePanel();
        mainpanel.add("vote", votePanel.getPanel());
// 二、菜单栏按钮的实例化与布局
    // 1.公告按钮
        noticeIcon = new ImageIcon("resource/icon/notice.png");
        noticeButton = new JButton(noticeIcon);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        noticeButton.setBackground(new Color(0x52B7F7));
        noticeButton.setContentAreaFilled(false);
        noticeButton.setFocusable(false);
        noticeButton.setToolTipText("公告");
        menuButtons[0] = new MenuButton();
        menuButtons[0].button = noticeButton;
        menuButtons[0].status = 2;
        menuButtons[0].changeStatus();
        changeButtonUI(menuButtons[0], mainpanel, "notice");
        panel.add(noticeButton, gridBagConstraints);
        // 在这里用图标监听器，用图标改变的方式显示是否有新通知
        LoginFrame.webSocket.setIconChangeListener(message -> {
            System.out.println(message);
            ImageIcon NoticeIcon = new ImageIcon("resource/icon/newNotice.png");
            noticeButton = new JButton(NoticeIcon);
        });
        noticeButton.addActionListener(e -> {
            noticeButton = new JButton(noticeIcon);
        });
    // 2.聊天按钮
        ImageIcon chatIcon = new ImageIcon("resource/icon/chat.png");
        chatButton = new JButton(chatIcon);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        chatButton.setBackground(new Color(0x52B7f7));
        chatButton.setContentAreaFilled(false);
        chatButton.setFocusable(false);
        chatButton.setToolTipText("聊天");
        menuButtons[1] = new MenuButton();
        menuButtons[1].button = chatButton;
        menuButtons[1].status = 0;
        changeButtonUI(menuButtons[1], mainpanel, "chat");
        panel.add(chatButton, gridBagConstraints);
        LoginFrame.webSocket.setDefaultMessageListener(msg->{
            System.out.println(msg);
            ImageIcon newChatIcon = new ImageIcon("resource/icon/newChat.png");
            chatButton = new JButton(newChatIcon);
        });
        chatButton.addActionListener(e -> {
            chatButton = new JButton(chatIcon);
        });
    // 3.文件共享按钮.
        ImageIcon fileIcon = new ImageIcon("resource/icon/file.png");
        fileButton = new JButton(fileIcon);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        fileButton.setBackground(new Color(0x52B7f7));
        fileButton.setContentAreaFilled(false);
        fileButton.setFocusable(false);
        fileButton.setToolTipText("文件共享");
        menuButtons[2] = new MenuButton();
        menuButtons[2].button = fileButton;
        menuButtons[2].status = 0;
        changeButtonUI(menuButtons[2], mainpanel, "file");
        panel.add(fileButton, gridBagConstraints);
    // 4.传阅投票按钮
        ImageIcon voteIcon = new ImageIcon("resource/icon/vote.png");
        voteButton = new JButton(voteIcon);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        voteButton.setBackground(new Color(0x52B7f7));
        voteButton.setContentAreaFilled(false);
        voteButton.setFocusable(false);
        voteButton.setToolTipText("事项传阅及投票");
        menuButtons[3] = new MenuButton();
        menuButtons[3].button = voteButton;
        menuButtons[3].status = 0;
        changeButtonUI(menuButtons[3], mainpanel, "vote");
        panel.add(voteButton, gridBagConstraints);
    // 5.拿来凑地方的空标签
        ImageIcon empty = new ImageIcon("resource/icon/empty.png");
        emptyLabel = new JLabel(empty);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;

        return panel;
    }

    protected NoticePanel newInstanceNoticePanel() {
        return new NoticePanel();
    }

    protected FilePanel newInstanceFilePanel() {
        return new FilePanel();
    }

    protected VotePanel newInstanceVotePanel() {
        return new VotePanel();
    }

    private void changeButtonUI(MenuButton jButton, JComponent jPanel, String name) {
        jButton.button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                for (MenuButton menuButton : menuButtons) {
                    if (menuButton == jButton) {
                        continue;
                    }
                    if (menuButton.status == 2) {
                        menuButton.status = 0;
                        menuButton.changeStatus();
                    }
                }
                if (jButton.status == 1) {
                    jButton.status = 2;
                    jButton.changeStatus();
                }
                cardLayout.show(jPanel, name);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                jButton.button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                if (jButton.status == 0) {
                    jButton.status = 1;
                    jButton.changeStatus();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                jButton.button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                if (jButton.status == 1) {
                    jButton.status = 0;
                    jButton.changeStatus();
                }
            }

        });
    }


}
