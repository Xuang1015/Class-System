package frame;

import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class LoginFrame extends BaseFrame{
    private JPanel panel;
    private JTextField textField;
    private JPasswordField passwordField;
    private JLabel retrieve;
    private JCheckBox jCheckBox;
    private JButton button;

    public LoginFrame() {
        setTitle("用户登录");
    }

    @Override
    public JComponent generate() {
        panel = new JPanel();
        panel.setBackground(new Color(0x52B7F7));
        panel.setBorder(new EmptyBorder(24,50,40,30));
        GridBagLayout gridBagLayout = new GridBagLayout();
        panel.setLayout(gridBagLayout);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        Font font1 = new Font("方正兰亭超细黑简体 常规", Font.LAYOUT_LEFT_TO_RIGHT, 13);
        Font font2 = new Font("等线", Font.LAYOUT_LEFT_TO_RIGHT, 20);
        Font font3 = new Font("等线", Font.LAYOUT_LEFT_TO_RIGHT, 16);

        this.setUndecorated(true);
        ImageIcon closeIcon = new ImageIcon("resource/icon/close.png");
        JButton close = new JButton(closeIcon);
        close.setBackground(new Color(0x52B7F7));
        close.setContentAreaFilled(false); // 取消按钮边框
        close.setFocusable(false); // 取消文字边框
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 1;
        close.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON1) {
                    dispose();
                }
            }
        });
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
        panel.add(passwordField, gridBagConstraints);

        JLabel separation = new JLabel("  ");
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 5;
        panel.add(separation, gridBagConstraints);

        jCheckBox = new JCheckBox("记住密码 ");
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.fill = GridBagConstraints.NONE;
        jCheckBox.setOpaque(false);
        jCheckBox.setFont(font3);
        panel.add(jCheckBox, gridBagConstraints);

        button = new JButton("登录");
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.weightx = 0.5;
        button.addActionListener(e -> {
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                textField.setEnabled(false);
                passwordField.setEnabled(false);
                retrieve.setEnabled(false);
                jCheckBox.setEnabled(false);
                button.setEnabled(false);

            try {
                // link
                URL url = new URL("http://121.250.216.117:8080/login");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");
//                connection.setRequestProperty("stuNo", textField.getText());
//                connection.setRequestProperty("password", passwordField.getText());
                connection.connect();
                // output
                OutputStream outputStream = connection.getOutputStream();
                String id = "stuNo=" + URLEncoder.encode(textField.getText()) + "&password=" + URLEncoder.encode(passwordField.getText());
                outputStream.write(id.getBytes());  // 暂时存在缓冲流
                outputStream.flush();  // 把缓冲流推到后端
                // input
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String input = "";
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    input += line + "\n";
                }
                // close
                outputStream.close();
                bufferedReader.close();
                System.out.println(input);  // 发布的时候记得删掉哦
                JSONObject jsonObject = new JSONObject(input);
                int status = jsonObject.getInt("status");
                if (status == 0) {
                    System.out.println("登录成功！");
                    dispose();  // 销毁窗口
                } else {
                    JOptionPane.showMessageDialog(this, "用户名或密码错误！");
                    restore();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(this,"连接失败");  // dialog的子类。错误信息提示
                restore();
            } catch (JSONException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(this, "无法解析服务器信息！");
                restore();
            }
        });
        panel.add(button, gridBagConstraints);

        retrieve = new JLabel("找回密码");
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.weightx = 0.5;
        retrieve.setFont(font3);
        retrieve.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON1) {
                    System.out.println("Wow!");  // 写一个找回密码的窗口
                }
            }
        });
        retrieve.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(retrieve, gridBagConstraints);


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
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
}
