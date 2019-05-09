package frame;

import util.UIDesign;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Map;

public class LogFrame extends BaseFrame {
    private JPanel panel;
    private GridBagConstraints gridBagConstraints;
    private JTextArea logArea;
    private JScrollPane logPanel;
    private int xOld;
    private int yOld;

    @Override
    public JComponent generate() {
        Map component = getContent();
        panel = new JPanel();
        panel.setBackground(new Color(0x52B7F7));
        panel.setPreferredSize(new Dimension(500, 400));
        panel.setBorder(new EmptyBorder(0, 0, 50, 0));
        gridBagConstraints = new GridBagConstraints();

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
                LogFrame.this.setLocation(xx, yy);//设置拖拽后，窗口的位置
            }
        });

        JButton mini = icons[0];
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        UIDesign.getMinimizeFunction(mini, this);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        panel.add(mini, gridBagConstraints);

        JButton close = icons[1];
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        UIDesign.getCloseFunction(close, this);
        panel.add(close, gridBagConstraints);

        logArea = new JTextArea();
        logArea.setText((String) component.get("log"));
        logArea.setFont(new Font(Font.DIALOG,Font.PLAIN,16));
        logPanel = new JScrollPane(logArea);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        logPanel.setPreferredSize(new Dimension(400, 300));
        panel.add(logPanel, gridBagConstraints);

        return panel;
    }
}
