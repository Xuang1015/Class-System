package panel;

import frame.AddVoteFrame;
import frame.BaseFrame;
import frame.LoginFrame;
import frame.LogFrame;
import org.json.JSONException;
import org.json.JSONObject;
import util.URLCommunicate;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AdminVotePanel extends VotePanel {
    private JButton addVoteButton;
    private JButton logButton;

    public AdminVotePanel() {
        addVoteButton = new JButton("发起投票");
        addVoteButton.addActionListener(e -> BaseFrame.showFrame(new AddVoteFrame(),null));
        changePagePanel.add(addVoteButton);

        logButton = new JButton("管理员日志");
        logButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logButton.addActionListener(e -> {
            new Thread(()->{
                Map<String,Object> output = new HashMap<>();
                output.put("username", LoginFrame.username);
                try {
                    JSONObject input = URLCommunicate.get("http://121.250.216.117:8080/log/getLog", output);
                    Map component = new HashMap();
                    component.put("log", input.getJSONObject("data").getString("log"));
                    BaseFrame.showFrame(new LogFrame(),component);
                } catch (IOException | JSONException e1) {
                    e1.printStackTrace();
                }
            }).start();
        });
        changePagePanel.add(logButton);
    }
}
