package frame;

import jdk.nashorn.internal.scripts.JO;
import org.json.JSONException;
import util.UIDesign;
import util.URLCommunicate;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PublishNoticeFrame extends BaseFrame {
    private JPanel panel;
    private GridBagLayout gridBagLayout;
    private GridBagConstraints gridBagConstraints;
    private JLabel titleLable;
    private JTextField title;
    private JLabel contentLabel;
    private JScrollPane contentPane;
    private JTextArea content;
    private JButton publish;
    private int xOld;
    private int yOld;
    @Override
    public JComponent generate(){
        panel = new JPanel();
        panel.setBackground(new Color(0x52B7F7));
        setContentPane(panel);
        gridBagLayout = new GridBagLayout();
        gridBagConstraints = new GridBagConstraints();
        panel.setLayout(gridBagLayout);
        panel.setBorder(new EmptyBorder(0, 50, 20, 0));

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
                PublishNoticeFrame.this.setLocation(xx, yy);//设置拖拽后，窗口的位置
            }
        });

        JButton mini = icons[0];
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        UIDesign.getMinimizeFunction(mini, this);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        panel.add(mini, gridBagConstraints);

        JButton close = icons[1];
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        UIDesign.getCloseFunction(close, this);
        panel.add(close, gridBagConstraints);

        titleLable = new JLabel("标题:");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panel.add(titleLable,gridBagConstraints);

        title = new JTextField();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        title.setPreferredSize(new Dimension(300,32));
        panel.add(title,gridBagConstraints);

        contentLabel = new JLabel("内容：");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panel.add(contentLabel,gridBagConstraints);

        content = new JTextArea();
        contentPane = new JScrollPane(content);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        content.setColumns(19);
        content.setRows(5);
        content.setFont(new Font(Font.DIALOG,Font.PLAIN,20));
        content.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
                    //sendMessage();
                }
            }
        });
        panel.add(contentPane,gridBagConstraints);

        publish = new JButton("发布");
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        publish.setCursor(new Cursor(Cursor.HAND_CURSOR));
        publish.addActionListener(e -> {
            if (title.getText().equals("")||content.getText().equals("")) {
                JOptionPane.showMessageDialog(panel,"所填不能为空！");
                return;
            }
            if (title.getText().length()>25) {
                JOptionPane.showMessageDialog(panel,"标题不能超过25个字！");
                return;
            }
            if (content.getText().length()>500) {
                JOptionPane.showMessageDialog(panel,"内容不能超过500个字！");
                return;
            }
            new Thread(()->{
                Map<String, Object> output = new HashMap<>();
                output.put("username",LoginFrame.username);
                output.put("title",title.getText());
                output.put("content",content.getText());
                try {
                    URLCommunicate.post("http://121.250.216.117:8080/notice/add",output);
                    JOptionPane.showMessageDialog(panel,"发布公告成功！");
                    dispose();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(panel,"未能连接服务器，发送失败！");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }).start();
        });
        panel.add(publish,gridBagConstraints);

        return panel;
    }
}
