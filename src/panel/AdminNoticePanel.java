package panel;

import frame.BaseFrame;
import frame.PublishNoticeFrame;
import org.json.JSONException;
import org.json.JSONObject;
import util.URLCommunicate;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AdminNoticePanel extends NoticePanel {
    public AdminNoticePanel() {
        JButton publish = new JButton("新公告");
        changePagePanel.add(publish);
        publish.setCursor(new Cursor(Cursor.HAND_CURSOR));
        publish.addActionListener(e -> {
            BaseFrame.showFrame(new PublishNoticeFrame(),null);
        });

        JButton delete = new JButton("删除公告");
        publish.setCursor(new Cursor(Cursor.HAND_CURSOR));
        publish.setToolTipText("若想删除公告，请先单击选中该公告，再点击删除按钮");
        delete.addActionListener(e->{
            int value = JOptionPane.showConfirmDialog(noticeTable,"你确定删除该公告吗？","请确认",JOptionPane.YES_NO_OPTION);
            if(value == JOptionPane.YES_OPTION) {
                deleteNotice(noticeTable.getSelectedRow());
            }
        });
        changePagePanel.add(delete);
    }

    private void deleteNotice(int row) {
         new Thread(()->{
             Map<String,Object> output = new HashMap<>();
             output.put("noticeId",notices[row].getId());
             try {
                 JSONObject input = URLCommunicate.post("http://121.250.216.117:8080/notice/delete",output);
                 if(input.getInt("code")==-1){
                     JOptionPane.showMessageDialog(noticeTable,"无法找到该公告！");
                 }
                 getNotice(page);
             } catch (IOException e) {
                 e.printStackTrace();
                 JOptionPane.showMessageDialog(noticeTable,"连接服务器失败！");
             } catch (JSONException e) {
                 e.printStackTrace();
                 JOptionPane.showMessageDialog(noticeTable,"删除失败！");
             }
         }).start();
    }
}
