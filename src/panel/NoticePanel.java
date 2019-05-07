package panel;

import bean.Notice;
import frame.BaseFrame;
import frame.NoticeFrame;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.DataTypeChange;
import util.URLCommunicate;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NoticePanel extends Fragment {
    private JPanel noticePanel;
    protected JPanel changePagePanel;
    protected Notice[] notices;
    protected JTable noticeTable;
    private JButton prePage;
    private JButton nextPage;
    protected int page;

    private final Object noticeLock = new Object();

    public NoticePanel() {
        noticePanel = new JPanel();
        noticePanel.setLayout(new BorderLayout());
        changePagePanel = new JPanel();
        noticePanel.add(changePagePanel, BorderLayout.SOUTH);
        // (1)使用表格来组织形式
        String[] columnName = {"标题", "时间", "发布者"};
        notices = new Notice[0];
        // 使用表格模型
        noticeTable = new JTable(new AbstractTableModel() {
            public String getColumnName(int column) {
                return columnName[column];
            }

            public int getRowCount() {
                return notices.length;
            }

            public int getColumnCount() {
                return columnName.length;
            }

            public Object getValueAt(int row, int col) {
                synchronized (noticeLock) {
                    return notices[row].getByIndex(col);
                }
            }
        });
        // 表格显示UI的设置
        noticeTable.setShowVerticalLines(false);
        noticeTable.setRowHeight(64);
        noticePanel.add(noticeTable.getTableHeader(), BorderLayout.NORTH);
        noticePanel.add(noticeTable, BorderLayout.CENTER);
        // id列的样式设置,使其与表头宽度适配且不可手动调整
        TableColumn idColumn = noticeTable.getColumnModel().getColumn(0);
        idColumn.sizeWidthToFit();
        idColumn.setResizable(false);
        noticePanel.updateUI();
        // (2)用两个按钮去操作这个表格：上一页，下一页
        prePage = new JButton("上一页");
        nextPage = new JButton("下一页");
        prePage.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nextPage.setCursor(new Cursor(Cursor.HAND_CURSOR));
        changePagePanel.add(prePage);
        changePagePanel.add(nextPage);
        // 记载当前页码
        page = 0;
        // 功能1：获得公告列表，分为进门加载，上一页加载和下一页加载
        prePage.setEnabled(false);
        // 进门先加载一页
        getNotice(0);
        // 两个按钮的相关事件
        nextPage.addActionListener(e -> getNotice(page + 1));
        prePage.addActionListener(e -> getNotice(page - 1));
        // 功能2：获得公告：双击表格元素以获得公告
        noticeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    showNotice(noticeTable.getSelectedRow(), noticePanel);
                }
            }
        });
    }

    protected void getNotice(int newPage) {
        new Thread(() -> {
            Map<String, Object> getNoticesRequest = new HashMap<>();
            getNoticesRequest.put("page", newPage);
            getNoticesRequest.put("size", 10);
            try {
                JSONObject notice = URLCommunicate.get("http://121.250.216.117:8080/notice/getAll", getNoticesRequest);
                JSONObject data = notice.getJSONObject("data");
                JSONObject temp;
                JSONArray resultArray = data.getJSONArray("data");
                int len = resultArray.length();
                if (len == 0) {
                    JOptionPane.showMessageDialog(getPanel(), "已经没有更多公告啦！");
                    return;
                } else {
                    synchronized (noticeLock) {
                        notices = new Notice[len];
                        for (int i = 0; i < len; i++) {
                            temp = new JSONObject(resultArray.getString(i));
                            notices[i] = new Notice();
                            notices[i].setAnnouncer(temp.getString("announcer"));
                            notices[i].setId(temp.getInt("noticeId"));
                            notices[i].setTitle(temp.getString("title"));
                            notices[i].setTime(DataTypeChange.time2String(temp.getLong("time")));
                        }
                    }
                }
                noticeTable.updateUI();
                page = newPage;
                // 上一页可以使用的判断标准
                prePage.setEnabled(page > 0);
            } catch (IOException exception) {
                exception.printStackTrace();
                JOptionPane.showMessageDialog(getPanel(), "无法连接到服务器，获取公告失败！");
            } catch (JSONException exception) {
                exception.printStackTrace();
                JOptionPane.showMessageDialog(getPanel(), "无法解析服务器的公告信息！");
            }
        }).start();
    }

    private void showNotice(int row, JComponent jComponent) {
        new Thread(() -> {
            Map<String, Object> output = new HashMap<>();
            output.put("id", notices[row].getId());
            try {
                JSONObject input = URLCommunicate.get("http://121.250.216.117:8080/notice/get", output);
                JSONObject data = input.getJSONObject("data");
                String content = data.getString("content");
                // 在这里使用一个公告窗口来显示
                Map<String, Object> component = new HashMap<>();
                component.put("content", content);
                BaseFrame.showFrame(new NoticeFrame(), component);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(jComponent, "服务器连接失败！");
            } catch (JSONException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(jComponent, "获取公告内容失败！");
            }
        }).start();
    }

    public JPanel getPanel() {
        return noticePanel;
    }
}
