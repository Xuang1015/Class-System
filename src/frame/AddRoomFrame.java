package frame;

import bean.Room;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.Iostream;
import util.UIDesign;
import bean.ChatMember;
import util.URLCommunicate;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddRoomFrame extends BaseFrame {
    private JPanel addroompanel;
    private GridBagLayout gridBagLayout;
    private GridBagConstraints gridBagConstraints;
    private JLabel titleLabel;
    private JTextField nameField;
    private JLabel memberLabel;
    private JPanel memberPanel;
    private JButton addRoomButton;
    private Map component;
    private ChatMember[] members = new ChatMember[0];
    private JList memberList;
    private ChatMember[] selectMember = new ChatMember[0];
    private Room[] rooms = new Room[0];
    private JList roomList;

    @Override
    public JComponent generate() {
        component = getContent();
        addroompanel = new JPanel();
        gridBagLayout = new GridBagLayout();
        gridBagConstraints = new GridBagConstraints();
        addroompanel.setLayout(gridBagLayout);
        addroompanel.setPreferredSize(new Dimension(350,300));
        addroompanel.setBackground(new Color(0x52B7F7));

        JButton icons[] = UIDesign.unDecorated(this);

        JButton mini = icons[0];
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        UIDesign.getMinimizeFunction(mini, this);
        addroompanel.add(mini, gridBagConstraints);

        JButton close = icons[1];
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        UIDesign.getCloseFunction(close, this);
        addroompanel.add(close, gridBagConstraints);

        titleLabel = new JLabel("房间名称");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weighty = 0.8;
        addroompanel.add(titleLabel,gridBagConstraints);

        nameField = new JTextField();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        nameField.setPreferredSize(new Dimension(100,32));
        addroompanel.add(nameField,gridBagConstraints);

        memberLabel = new JLabel("成员列表");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        addroompanel.add(memberLabel,gridBagConstraints);

        memberPanel = new JPanel();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        addroompanel.add(memberPanel,gridBagConstraints);

        addRoomButton = new JButton("确认添加");
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        addroompanel.add(addRoomButton,gridBagConstraints);
        int j = 0;
        // 拿到组件
        memberList =(JList) component.get("memberList");
        memberPanel.add(memberList);
        members = (ChatMember[])component.get("members");
        roomList = (JList)component.get("roomList");
        rooms = (Room[])component.get("rooms");
        for (int i: memberList.getSelectedIndices()) {
            selectMember[j] = members[i];
            j++;
        }
        addRoomButton.addActionListener(e -> addRoom(nameField.getText(), selectMember));

        return addroompanel;
    }

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

    private void addRoom(String title, ChatMember[] member) {
        new Thread(() -> {
            Map<String, Object> output = new HashMap<>();
            output.put("username", LoginFrame.username);
            output.put("title", title);
            try {
                JSONObject input = URLCommunicate.post("http://121.250.216.117:8080/chat/roomAdd", output);
                JSONObject data = input.getJSONObject("data");
                Iostream.output("cache/message/" + data.getLong("roomId"), "欢迎来到群聊：" + title);
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
