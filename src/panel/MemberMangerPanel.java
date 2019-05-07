package panel;

import bean.ChatMember;
import org.json.JSONException;
import util.URLCommunicate;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MemberMangerPanel extends ChatPanel {
    private JPanel panel;
    private JScrollPane listPanel;
    private JPanel manegerPanel;
    private JButton add;
    private JButton delete;
    private JButton dissolution;
    private ChatMember addedMember;
    private JList littleMemberList;
    private JButton confirmDelete;
    private ChatMember deletedMember;

    public MemberMangerPanel() {
        panel = new JPanel();

        // 进门默认获得大厅的名单
        getMemberList(0);
        littleMemberList = memberList;
        listPanel = new JScrollPane(littleMemberList);
        listPanel.setSize(new Dimension(200,300));
        panel.add(listPanel);
        // 管理功能面板（类似于菜单）
        manegerPanel = new JPanel();
        add = new JButton("添加成员");
        add.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add.setToolTipText("若想添加成员，先选择需要添加的成员,再点击此按钮");
        addedMember = (ChatMember) littleMemberList.getSelectedValue();
        add.addActionListener(e -> {
            panel.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            new Thread(()->{
                Map<String,Object> input = new HashMap<>();
                input.put("roomId", roomOwner.getRoomId());
                input.put("username", addedMember.getUsername());
                try {
                    URLCommunicate.post("http://121.250.216.117:8080/chat/addToRoom",input);
                    JOptionPane.showConfirmDialog(manegerPanel,"添加成功！");
                    panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    getMemberList(roomOwner.getRoomId());
                    memberList.updateUI();
                } catch (IOException | JSONException e1) {
                    e1.printStackTrace();
                    JOptionPane.showConfirmDialog(manegerPanel,"添加失败！");
                }
            }).start();
        });
        manegerPanel.add(add);

        delete = new JButton("删除成员");
        delete.setToolTipText("若想删除成员。请先点击此按钮获得房间名单，单击该成员后点击确认删除");
        confirmDelete = new JButton("确认删除");
        confirmDelete.setEnabled(false);
        delete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        delete.addActionListener(e -> {
            getMemberList(roomOwner.getRoomId());
            littleMemberList = memberList;
            littleMemberList.updateUI();
            confirmDelete.setEnabled(true);
            confirmDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        });
        deletedMember = (ChatMember)littleMemberList.getSelectedValue();
        confirmDelete.addActionListener(e -> {
            new Thread(()->{
                Map<String,Object> input = new HashMap<>();
                input.put("username",deletedMember.getUsername());
                input.put("roomId", roomOwner.getRoomId());
                try {
                    URLCommunicate.post("http://121.250.216.117:8080/chat/removeFromRoom",input);
                    JOptionPane.showConfirmDialog(manegerPanel, "删除成功！");
                    getMemberList(roomOwner.getRoomId());
                    littleMemberList = memberList;
                    littleMemberList.updateUI();
                    memberList.updateUI();
                    confirmDelete.setEnabled(false);
                    confirmDelete.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                } catch (IOException | JSONException e1) {
                    e1.printStackTrace();
                }
            }).start();
        });
        manegerPanel.add(delete);
        manegerPanel.add(confirmDelete);

        dissolution = new JButton("解散群聊");
        manegerPanel.add(dissolution);

    }
}
