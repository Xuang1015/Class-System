package util;

import frame.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Cursor;
import java.awt.event.MouseMotionAdapter;

public class UIDesign {
    public static JButton[] unDecorated(JFrame frame) {
        frame.setUndecorated(true);
        JButton[] icons = new JButton[2];

        ImageIcon miniIcon = new ImageIcon("resource/icon/minimize.png");
        JButton mini = new JButton(miniIcon);
        mini.setBackground(new Color(0x52B7F7));
        mini.setContentAreaFilled(false);
        mini.setFocusable(false);
        icons[0] = mini;

        ImageIcon closeIcon = new ImageIcon("resource/icon/close.png");
        JButton close = new JButton(closeIcon);
        close.setBackground(new Color(0x52B7F7));
        close.setContentAreaFilled(false); // 取消按钮边框
        close.setFocusable(false); // 取消文字边框
        icons[1] = close;

        return icons;
    }

    public static void getMinimizeFunction(JButton jButton, JFrame jFrame) {
        jButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
//                jFrame.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
//                jFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON1) {
                    jFrame.setExtendedState(JFrame.ICONIFIED);
                }
            }
        });
    }

    public static void getCloseFunction(JButton jButton, JFrame jFrame) {
        jButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
//                jFrame.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
//                jFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON1) {
                    jFrame.dispose();
                }
            }
        });
    }
}
