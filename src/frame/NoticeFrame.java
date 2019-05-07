package frame;

import util.UIDesign;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Map;

public class NoticeFrame extends BaseFrame {
    private JPanel panel;
    private int xOld;
    private int yOld;
    private GridBagLayout gridBagLayout;
    private GridBagConstraints gridBagConstraints;
    private JTextField textField;;

    @Override
    public JComponent generate() {
        Map component = getContent();
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
                NoticeFrame.this.setLocation(xx, yy);//设置拖拽后，窗口的位置
            }
        });

        JButton mini = icons[0];
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        UIDesign.getMinimizeFunction(mini, this);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        panel.add(mini, gridBagConstraints);

        JButton close = icons[1];
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        UIDesign.getCloseFunction(close, this);
        panel.add(close, gridBagConstraints);

        textField = new JTextField((String) component.get("content"));
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        textField.setSize(new Dimension(100,60));
        textField.setEditable(false);
        panel.add(textField,gridBagConstraints);

        return panel;
    }
}
