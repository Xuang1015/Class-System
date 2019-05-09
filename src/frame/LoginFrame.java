package frame;

import org.json.JSONException;
import org.json.JSONObject;
import util.Iostream;
import util.PasswordHelper;
import util.UIDesign;
import util.URLCommunicate;
import util.WebSocketConnection;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginFrame extends BaseFrame {
    public static long username;
    public static String password;
    public static WebSocketConnection webSocket;
    private JPanel panel;
    private JTextField textField;
    private JPasswordField passwordField;
    private JLabel retrieve;
    private JCheckBox jCheckBox;
    private JButton button;
    private JLabel register;
    private String uname;

    public LoginFrame() {
        setTitle("用户登录");
    }

    @Override
    public JComponent generate() {
        panel = new JPanel();
        panel.setBackground(new Color(0x52B7F7));
        panel.setBorder(new EmptyBorder(24, 60, 40, 30));
        GridBagLayout gridBagLayout = new GridBagLayout();
        panel.setLayout(gridBagLayout);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        Font font1 = new Font("方正兰亭超细黑简体 常规", Font.PLAIN, 13);
        Font font2 = new Font("等线", Font.PLAIN, 20);
        Font font3 = new Font("等线", Font.PLAIN, 16);

        JButton icons[] = UIDesign.unDecorated(this);

        JButton mini = icons[0];
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 1;
        UIDesign.getMinimizeFunction(mini, this);
        panel.add(mini, gridBagConstraints);

        JButton close = icons[1];
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 1;
        UIDesign.getCloseFunction(close, this);
        panel.add(close, gridBagConstraints);

        ImageIcon imageIcon = new ImageIcon("resource/image/logo.png");
        JLabel image = new JLabel(imageIcon);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 7;
        panel.add(image, gridBagConstraints);

        JLabel separation2 = new JLabel("  ");
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 5;
        panel.add(separation2, gridBagConstraints);

        JLabel user = new JLabel("用户名:");
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.weightx = 0.2;
        user.setFont(font2);
        panel.add(user, gridBagConstraints);

        textField = new JTextField();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        textField.setBackground(new Color(0x7CCDF7));
        panel.add(textField, gridBagConstraints);

        // 注册前面的空格不要删，好看
        register = new JLabel("    注册");
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 1;
        register.setFont(font3);
        register.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON1) {
                    showFrame(new RegisterFrame(), null);
                }
            }
        });
        register.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(register, gridBagConstraints);

        JLabel password = new JLabel("密码：");
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.fill = GridBagConstraints.NONE;
        password.setFont(font2);
        panel.add(password, gridBagConstraints);

        passwordField = new JPasswordField();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        passwordField.setBackground(new Color(0x7CCDF7));
        // 判断用户是否有保存密码，有的话直接给
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                uname = textField.getText();
                String pws = null;
                try {
                    pws = Iostream.input("cache/account/" + uname);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                if (!"".equals(pws)) {
                    passwordField.setText(pws);
                }
            }
        });

        panel.add(passwordField, gridBagConstraints);

        retrieve = new JLabel("找回密码");
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 1;
        retrieve.setFont(font3);
        retrieve.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON1) {
                    BaseFrame.showFrame(new ResetPasswordFrame(),null);
                }
            }
        });
        retrieve.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(retrieve, gridBagConstraints);

        JLabel separation = new JLabel("  ");
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 5;
        panel.add(separation, gridBagConstraints);

        jCheckBox = new JCheckBox("记住密码");
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        jCheckBox.setOpaque(false);
        jCheckBox.setFont(font3);
        panel.add(jCheckBox, gridBagConstraints);

        button = new JButton("登录");
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.weightx = 0.5;
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
//                showFrame(new MainFrame(), null);
//                dispose(); // 记得删除哦
            }
        });
        button.addActionListener(e -> {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            textField.setEnabled(false);
            passwordField.setEnabled(false);
            retrieve.setEnabled(false);
            jCheckBox.setEnabled(false);
            button.setEnabled(false);
            register.setEnabled(false);
            new Thread(() -> {
                try {
                    Map<String, Object> output = new HashMap<>();
                    String usernameString = (textField.getText());
                    String pwd = passwordField.getText();
                    // TODO: 测试用：跳过登录，发布时改为 false
                    if (true && usernameString.isEmpty()) {
                        usernameString = "201800301032";
                        pwd = "L20001203";
                    }
                    long username = Long.parseLong(usernameString);
                    LoginFrame.username = username;
                    output.put("username", username);
                    output.put("password", PasswordHelper.encrypt(pwd));
                    JSONObject input = URLCommunicate.post("http://121.250.216.117:8080/login/do", output);
                    int status = input.getInt("code");
                    if (status == 0) {
                        System.out.println("登录成功！");
                        // 在这里建立一个webSocket
                        webSocket = WebSocketConnection.wsConnect("http://121.250.216.117:8080/chat/" + username);
                        // 在这里用户可以去选择记住密码,若如此，保存到本地
                        if (jCheckBox.isSelected()) {
                            LoginFrame.password = pwd;
                            Iostream.output("cache/account/" + LoginFrame.username, LoginFrame.password);
                        }
                        dispose();  // 销毁窗口
                        JSONObject data = input.getJSONObject("data");
                        if (data.getInt("authority") == 1) {
                            showFrame(new MainFrame(), null);
                        } else {
                            showFrame(new AdminFrame(), null);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "用户名或密码错误！");
                        restore();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(this, "连接失败");  // dialog的子类。错误信息提示
                    restore();
                } catch (JSONException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(this, "无法解析服务器信息！");
                    restore();
                }
            }).start();

        });
        panel.add(button, gridBagConstraints);

        JLabel explain = new JLabel("  说明：用户名为学号，初始密码为身份证后六位");
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 5;
        explain.setFont(font1);
        panel.add(explain, gridBagConstraints);

        setResizable(false);

        return panel;
    }

    private void restore() {
        textField.setEnabled(true);
        passwordField.setEnabled(true);
        retrieve.setEnabled(true);
        jCheckBox.setEnabled(true);
        button.setEnabled(true);
        register.setEnabled(true);
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
}
