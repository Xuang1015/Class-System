package panel;

import bean.ChatMember;
import bean.Room;
import frame.AddRoomFrame;
import frame.BaseFrame;
import frame.LoginFrame;
import frame.MemberMangerFrame;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.DataTypeChange;
import util.URLCommunicate;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ChatPanel extends Fragment {
    private JPanel chatpanel;
    private JScrollPane massagePanel;
    private JScrollPane textPanel;
    private JTextArea textField;
    private JPanel listPanel;
    private JPanel middle;
    private JScrollPane memberPanel;
    private JScrollPane roomPanel;
    protected JList memberList;
    protected JList roomList;
    private CardLayout cardLayout;
    private GridBagLayout gridBagLayout;
    private GridBagConstraints gridBagConstraints;
    protected Room[] rooms;
    protected ChatMember[] members = new ChatMember[0];
    private JLabel mem;
    private JButton memberMenu;
    protected ChatMember roomOwner;
    private boolean roomOwnerIsMe;
    private JLabel roomLabel;
    private JPanel top;
    private JButton addRoomButton;
    private JTextArea messageArea;
    private Room personalRoom;
    private boolean sent;

    public ChatPanel() {
        chatpanel = new JPanel();
        cardLayout = new CardLayout();
        gridBagLayout = new GridBagLayout();
        gridBagConstraints = new GridBagConstraints();
        chatpanel.setLayout(gridBagLayout);

        Image image;
        try {
            image = ImageIO.read(new File("resource/image/bg.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
            image = null;
        }
        Image finalImage = image;
        messageArea = new JTextArea() {
            @Override
            protected void paintComponent(Graphics g) {
                if (finalImage != null) {
                    int width = getWidth();
                    int height = getHeight();
                    int imageWidth = finalImage.getWidth(null);
                    int xCount = width / imageWidth + 1;
                    int imageHeight = finalImage.getHeight(null);
                    int yCount = height / imageHeight + 1;
                    for (int x = 0; x < xCount; x++) {
                        for (int y = 0; y < yCount; y++) {
                            g.drawImage(finalImage, x * imageWidth, y * imageHeight, null);
                        }
                    }
                }
                super.paintComponent(g);
            }
        };
        massagePanel = new JScrollPane(messageArea);
        messageArea.setFont(new Font(Font.DIALOG, Font.PLAIN, 24));
        messageArea.setEditable(false);
        messageArea.setOpaque(false);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        massagePanel.setPreferredSize(new Dimension(800, 600));
        massagePanel.setBackground(Color.BLUE);
        // 在这里获取房间和成员名单,加到列表里
        getRoomList();
        memberList = new JList();
        getMemberList(0);
        roomList = new JList();
        // 上线、下线会用到的改变名字颜色的监听器
        LoginFrame.webSocket.setNameChangeListener(message -> {
            for (ChatMember chatMember : members) {
                if (message.getMessageType() == 1 && chatMember.getName().equals(message.getName())) {
                    chatMember.setState("在线");
                } else if (message.getMessageType() == 2 && chatMember.getName().equals(message.getName())) {
                    chatMember.setState("离线");
                }
            }
            memberList.updateUI();
        });
        // 接受并显示即时消息的监听器
        LoginFrame.webSocket.setMessageListener(0, message -> {
            messageArea.append(message.getName() + "(" + DataTypeChange.time2String(message.getTime()) + ")" + ":" + "\n" + "    " +message.getMsg() + "\n");
            messageArea.updateUI();
        });
        chatpanel.add(massagePanel, gridBagConstraints);
        // 发送消息的文本区域
        textField = new JTextArea();
        textField.setLineWrap(true);
        textField.setWrapStyleWord(true);
        textField.setColumns(27);
        textField.setRows(6);
        textField.setFont(new Font(Font.DIALOG, Font.PLAIN, 24));
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
                    String text = textField.getText();
                    sendMessage(text);
                    textField.setText(null);
                }
            }
        });
        textPanel = new JScrollPane(textField);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 1;
        textPanel.setPreferredSize(new Dimension(800, 250));
        chatpanel.add(textPanel, gridBagConstraints);
        // 将两个列表放进两个面板，再把两个面板放进listPanel,顶部和中间还有小菜单
        listPanel = new JPanel();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 3;
        listPanel.setPreferredSize(new Dimension(400, 900));
        chatpanel.add(listPanel, gridBagConstraints);
        // 顶部栏
        top = new JPanel();
        top.setPreferredSize(new Dimension(300, 50));
        listPanel.add(top);
        roomLabel = new JLabel("房间列表");
        top.add(roomLabel);
        addRoomButton = new JButton("创建房间");
        addRoomButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addRoomButton.addActionListener(e -> {
            BaseFrame.showFrame(new AddRoomFrame(), null);
        });
        top.add(addRoomButton);

        roomPanel = new JScrollPane(roomList);
        roomPanel.setPreferredSize(new Dimension(350, 400));
        roomList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                // 双击获取成员列表
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    getMemberList(rooms[roomList.getSelectedIndex()].getRooomId());
                }
            }
        });
        listPanel.add(roomPanel);
        // 中间栏
        middle = new JPanel();
        middle.setSize(new Dimension(400, 50));
        mem = new JLabel("成员列表");
        mem.setSize(new Dimension(200, 50));
        middle.add(mem);
        // 判断房主，是否生成管理成员的按钮
        confirmRoomLoader();
        if (roomOwner.getAuthority() == 2) {
            memberMenu = new JButton("管理成员");
            memberMenu.addActionListener(e -> {
                BaseFrame.showFrame(new MemberMangerFrame(), null);
            });
            middle.add(memberMenu);
        }
        listPanel.add(middle);

        memberList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    addPrivateRoom(members[memberList.getSelectedIndex()]);
                    getRoomList();
                    roomList.updateUI();
                    //TODO:看看列表
                    // 先确定房主，再清空成员列表
//                    memberList.setListData(new Object[0]);
                    memberList.updateUI();
                }
            }
        });
        memberPanel = new JScrollPane(memberList);
        listPanel.add(memberPanel);
        memberPanel.setPreferredSize(new Dimension(350, 400));
    }

    private void confirmRoomLoader() {
        roomOwner = new ChatMember();
        for (ChatMember chatMember : members) {
            if (chatMember.getAuthority() == 2) {
                roomOwner = chatMember;
            }
        }
        roomOwnerIsMe = roomOwner.getUsername() == LoginFrame.username;
    }

    public JPanel getPanel() {
        return chatpanel;
    }

    private void sendMessage(String message) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", LoginFrame.username);
            jsonObject.put("roomId", rooms[roomList.getSelectedIndex()].getRooomId());
            jsonObject.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        String output = "username=" + LoginFrame.username + "&" + "roomId=" + rooms[roomList.getSelectedIndex()].getRooomId() + "&" + "message=" + message;
        LoginFrame.webSocket.getWebSocket().send(jsonObject.toString());
    }

    @SuppressWarnings("unchecked")
    private void getRoomList() {
        new Thread(() -> {
            Map<String, Object> output = new HashMap<>();
            output.put("page", 0);
            output.put("size", 300);
            try {
                JSONObject input = URLCommunicate.get("http://121.250.216.117:8080/chat/getRoomList", output);
                JSONObject data = input.getJSONObject("data");
                JSONArray resultArray = data.getJSONArray("data");
                JSONObject temp;
                Room tempRoom = null;
                int len = resultArray.length();
                rooms = new Room[len];
                for (int i = 0; i < len; i++) {
                    temp = new JSONObject(resultArray.getString(i));
                    rooms[i] = new Room();
                    rooms[i].setRooomId(temp.getLong("roomId"));
                    // 在这里判断大厅,放到tempRoom中
                    if (rooms[i].getRooomId() == 0) {
                        tempRoom = rooms[i];
                    }
                    rooms[i].setRoomTitle(temp.getString("title"));
                }
                System.arraycopy(rooms, 0, rooms, 1, rooms.length - 1);
                rooms[0] = tempRoom;
                roomList.setListData(rooms);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @SuppressWarnings("unchecked")
    protected void getMemberList(long id) {
        new Thread(() -> {
            Map<String, Object> output = new HashMap<>();
            output.put("roomId", id);
            try {
                JSONObject input = URLCommunicate.get("http://121.250.216.117:8080/chat/getMemberList", output);
                if (input.getInt("code") == -1) {
                    JOptionPane.showConfirmDialog(chatpanel, "该房间不存在！");
                } else {
                    JSONObject data = input.getJSONObject("data");
                    JSONArray resultArray = data.getJSONArray("data");
                    JSONObject temp;
                    int len = resultArray.length();
                    members = new ChatMember[len];
                    for (int i = 0; i < len; i++) {
                        temp = new JSONObject(resultArray.getString(i));
                        members[i] = new ChatMember();
                        members[i].setName(temp.getString("name"));
                        members[i].setUsername(temp.getLong("username"));
                        members[i].setAuthority(temp.getInt("authority"));
                        members[i].setRoomId(temp.getLong("roomId"));
                        int stand = temp.getInt("isOnline");
                        if (stand==1) {
                            members[i].setState("在线");
                        } else {
                            members[i].setState("离线");
                        }
                    }
                }
                memberList.setListData(members);
                if (!sent) {
                    sent = true;
                    LoginFrame.webSocket.getWebSocket().send("ready");
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void addPrivateRoom(ChatMember member) {
        new Thread(() -> {
            Map<String, Object> output = new HashMap<>();
            output.put("username1", LoginFrame.username);
            output.put("username2", member.getUsername());
            try {
                JSONObject input = URLCommunicate.post("http://121.250.216.117:8080/chat/personalRoomAdd", output);
                JSONObject data = input.getJSONObject("data");
                personalRoom = new Room();
                personalRoom.setRooomId(data.getLong("roomId"));
                personalRoom.setRoomTitle(data.getString("title"));
                getRoomList();
                roomList.updateUI();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

        }).start();
    }

    protected void addRoom(String title, ChatMember[] member) {
        new Thread(() -> {
            Map<String, Object> output = new HashMap<>();
            output.put("username", LoginFrame.username);
            output.put("title", title);
            try {
                JSONObject input = URLCommunicate.post("http://121.250.216.117:8080/chat/roomAdd", output);
                JSONObject data = input.getJSONObject("data");
                for (ChatMember m : member) {
                    Map<String, Object> addMemberRequest = new HashMap<>();
                    addMemberRequest.put("roomId", data.getLong("roomId"));
                    addMemberRequest.put("username", m.getUsername());
                    URLCommunicate.post("http://121.250.216.117:8080/chat/addToRoom", addMemberRequest);
                }
                getRoomList();
                roomList.updateUI();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
