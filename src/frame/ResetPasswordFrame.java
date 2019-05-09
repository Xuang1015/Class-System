package frame;

import org.json.JSONException;
import org.json.JSONObject;
import util.PasswordHelper;
import util.UIDesign;
import util.URLCommunicate;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResetPasswordFrame extends BaseFrame {
    private JPanel panel;
    private GridBagConstraints gridBagConstraints;
    private JLabel nameLabel;
    private JTextField nameField;
    private JLabel pwLabel;
    private JPasswordField pwField;
    private JLabel compwLabel;
    private JPasswordField compwField;
    private JTextField verifactionField;
    private JButton getVerButton;
    private JButton publishButton;
    private int xOld;
    private int yOld;

    @Override
    public JComponent generate() {
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        gridBagConstraints = new GridBagConstraints();
        panel.setBorder(new EmptyBorder(20,0,10,0));
        panel.setBackground(new Color(0x52B7F7));
        panel.setPreferredSize(new Dimension(600,400));

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
                ResetPasswordFrame.this.setLocation(xx, yy);//设置拖拽后，窗口的位置
            }
        });

        JButton mini = icons[0];
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        UIDesign.getMinimizeFunction(mini, this);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        panel.add(mini, gridBagConstraints);

        JButton close = icons[1];
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        UIDesign.getCloseFunction(close, this);
        panel.add(close, gridBagConstraints);

        nameLabel = new JLabel("您的用户名：");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panel.add(nameLabel, gridBagConstraints);

        nameField = new JTextField();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        nameField.setPreferredSize(new Dimension(400,30));
        panel.add(nameField, gridBagConstraints);

        pwLabel = new JLabel("您的新密码：");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weighty = 0.8;
        panel.add(pwLabel, gridBagConstraints);

        pwField = new JPasswordField();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        pwField.setPreferredSize(new Dimension(400,30));
        panel.add(pwField, gridBagConstraints);

        compwLabel = new JLabel("确认新密码：");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        panel.add(compwLabel, gridBagConstraints);

        compwField = new JPasswordField();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        compwField.setPreferredSize(new Dimension(400,30));
        panel.add(compwField, gridBagConstraints);

        verifactionField = new JTextField();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        verifactionField.setPreferredSize(new Dimension(120,30));
        panel.add(verifactionField, gridBagConstraints);

        getVerButton = new JButton("获取验证码");
        getVerButton.setToolTipText("此验证码将会发到您的邮箱，请注意查收");
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        getVerButton.addActionListener(e -> {
            if (!nameField.getText().matches("[0-9]{12}")) {
                JOptionPane.showMessageDialog(panel, "请输入正确的学号！");
                return;
            }
            new Thread(() -> {
                Map<String, Object> output = new HashMap<>();
                output.put("username", nameField.getText());
                try {
                    URLCommunicate.post("http://121.250.216.117:8080/login/codeGet", output);
                } catch (IOException | JSONException e1) {
                    e1.printStackTrace();
                }
            }).start();
        });
        panel.add(getVerButton, gridBagConstraints);

        publishButton = new JButton("提交");
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        publishButton.addActionListener(e -> {
            if (!pwField.getText().equals(compwField.getText())) {
                JOptionPane.showMessageDialog(panel, "两次输入的密码不一致！");
                return;
            }
            if (verifactionField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(panel, "请输入验证码！");
                return;
            }
            new Thread(() -> {
                Map<String, Object> output = new HashMap<>();
                output.put("username", nameField.getText());
                output.put("newPassword", PasswordHelper.encrypt(pwField.getText()));
                output.put("code", verifactionField.getText());
                try {
                    JSONObject input = URLCommunicate.post("http://121.250.216.117:8080/login/reset", output);
                    if (input.getInt("code") == -1) {
                        JOptionPane.showMessageDialog(panel, "验证码过期！");
                        return;
                    }
                    JOptionPane.showMessageDialog(panel,"提交成功！");
                    dispose();
                } catch (IOException | JSONException e1) {
                    e1.printStackTrace();
                }
            }).start();
        });
        panel.add(publishButton, gridBagConstraints);

        return panel;
    }
}
