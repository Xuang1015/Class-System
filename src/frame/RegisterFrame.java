package frame;

import org.json.JSONException;
import org.json.JSONObject;
import util.PasswordHelper;
import util.UIDesign;
import util.URLCommunicate;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegisterFrame extends BaseFrame {
    private JPanel panel;
    private GridBagLayout gridBagLayout;
    private GridBagConstraints gridBagConstraints;
    private JLabel email;
    private JTextField emailField;
    private JLabel username;
    private JTextField usernameField;
    private JLabel newname;
    private JTextField nameField;
    private JLabel newpassword;
    private JPasswordField passwordField;
    private JLabel empty1;
    private JLabel empty2;
    private JLabel empty3;
    private JLabel empty4;
    private JLabel empty5;
    private JButton submit;
    //用于处理拖动事件，表示鼠标按下时的坐标，相对于JFrame
    int xOld = 0;
    int yOld = 0;
    Font font = new Font("等线",Font.BOLD,16);

    @Override
    public JComponent generate() {
        panel = new JPanel();
        panel.setBackground(new Color(0x52B7F7));
        gridBagLayout = new GridBagLayout();
        panel.setLayout(gridBagLayout);
        gridBagConstraints = new GridBagConstraints();
        panel.setPreferredSize(new Dimension(720,350));
        panel.setBorder(new EmptyBorder(0,90,20,0));

        JButton icons[] = UIDesign.unDecorated(this);
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
                RegisterFrame.this.setLocation(xx, yy);//设置拖拽后，窗口的位置
            }
        });

        JButton mini = icons[0];
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 1;
        UIDesign.getMinimizeFunction(mini, this);
        panel.add(mini, gridBagConstraints);

        JButton close = icons[1];
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 1;
        UIDesign.getCloseFunction(close, this);
        panel.add(close, gridBagConstraints);
        // 注册用户邮箱栏
        empty1 = new JLabel(" ");
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panel.add(empty1,gridBagConstraints);

        email = new JLabel("请输入您的邮箱：");
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 1;
        email.setFont(font);
        panel.add(email,gridBagConstraints);

        emailField = new JTextField();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(emailField,gridBagConstraints);
        // 学号输入栏
        empty2 = new JLabel(" ");
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        panel.add(empty2,gridBagConstraints);

        username = new JLabel("请输入您的学号：");
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 1;
        username.setFont(font);
        panel.add(username,gridBagConstraints);

        usernameField = new JTextField();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(usernameField,gridBagConstraints);
        // 用户名输入栏
        empty3 = new JLabel(" ");
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        panel.add(empty3,gridBagConstraints);

        newname = new JLabel("请输入新用户名：");
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 1;
        newname.setFont(font);
        panel.add(newname,gridBagConstraints);

        nameField = new JTextField();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(nameField,gridBagConstraints);
        // 密码输入栏
        empty4 = new JLabel(" ");
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        panel.add(empty4,gridBagConstraints);

        newpassword = new JLabel("请输入密码：");
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 1;
        newpassword.setFont(font);
        panel.add(newpassword,gridBagConstraints);

        passwordField = new JPasswordField();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(passwordField,gridBagConstraints);

        empty5 = new JLabel(" ");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        panel.add(empty5,gridBagConstraints);

        submit = new JButton("注册");
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 1;
        submit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (emailField.getText().equals("") || usernameField.getText().equals("") || nameField.getText().equals("") || passwordField.getText().equals("")) {
                    JOptionPane.showMessageDialog(panel, "请填写完所有的信息！");
                    return;
                }
                if (!emailField.getText().matches("[a-zA-Z0-9_]+@[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*")){
                    JOptionPane.showMessageDialog(panel,"请输入正确的邮箱形式！");
                    return;
                }
                if(!usernameField.getText().matches("[0-9]{12}")){
                    JOptionPane.showMessageDialog(panel,"请输入正确的学号！");
                    return;
                }

                new Thread(() -> {
                    Map<String, Object> output = new HashMap<>();
                    output.put("mail",emailField.getText());
                    output.put("username",usernameField.getText());
                    output.put("name",nameField.getText());
                    output.put("password", PasswordHelper.encrypt(passwordField.getText()));
                    try {
                        JSONObject input = URLCommunicate.post("http://121.250.216.117:8080/login/register", output);
                        if(input.getInt("code") == -1){
                            JOptionPane.showMessageDialog(panel,"重复注册");
                        } else {
                            JOptionPane.showMessageDialog(panel,"注册成功！");
                            dispose();
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        JOptionPane.showMessageDialog(panel, "连接失败");  // dialog的子类。错误信息提示
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                        JOptionPane.showMessageDialog(panel, "无法解析服务器信息！");
                    }
                }).start();
            }
        });
        panel.add(submit,gridBagConstraints);

        return panel;
    }
}
