package frame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public abstract class BaseFrame extends JFrame{
    private Map<String, Object> content;

    public BaseFrame() {
        Image icon = null;
        try {
            icon = ImageIO.read(new File("resource/image/hs.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setIconImage(icon);
    }

    public Map<String, Object> getContent() {
        return content;
    }

    public static void ShowFrame(BaseFrame thisFrame, Map<String, Object> thisContent) {
        thisFrame.content = thisContent;
        JComponent pane = thisFrame.generate();
        thisFrame.setContentPane(pane);
        JMenuBar menuBar = thisFrame.createMenuBar();
        thisFrame.setJMenuBar(menuBar);
        thisFrame.pack();
        thisFrame.setLocationRelativeTo(null);
        thisFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        thisFrame.setVisible(true);
    }

    public abstract JComponent generate();

    public JMenuBar createMenuBar() {
        return null;
    }
}
