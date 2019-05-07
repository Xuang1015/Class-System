package frame;

import panel.MemberMangerPanel;
import util.UIDesign;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class MemberMangerFrame extends BaseFrame {
    private JPanel panel;
    private GridBagLayout gridBagLayout;
    private GridBagConstraints gridBagConstraints;
    private MemberMangerPanel contentPanel;
    private int xOld;
    private int yOld;

    @Override
    public JComponent generate() {
        panel = new JPanel();
        panel.setBackground(new Color(0x52B7F7));
        panel.setLayout(gridBagLayout);
        panel.setSize(new Dimension(300,300));

        JButton icons[] = UIDesign.unDecorated(this);//处理拖动事件---去掉默认边框后，不能拖动了，具体实现如下
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
                MemberMangerFrame.this.setLocation(xx, yy);//设置拖拽后，窗口的位置
            }
        });

        JButton mini = icons[0];
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        UIDesign.getMinimizeFunction(mini, this);
        panel.add(mini, gridBagConstraints);

        JButton close = icons[1];
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        UIDesign.getCloseFunction(close, this);
        panel.add(close, gridBagConstraints);

        contentPanel = new MemberMangerPanel();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panel.add(contentPanel.getPanel(),gridBagConstraints);

        return panel;
    }
}
