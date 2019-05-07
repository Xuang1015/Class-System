package panel;

import bean.Choice;
import bean.Vote;
import frame.BaseFrame;
import frame.ShowVoteFrame;
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

public class VotePanel extends Fragment {
    private JPanel votePanel;
    protected JPanel changePagePanel;
    protected Vote[] votes = new Vote[0];
    protected String[] comments = new String[0];
    protected Choice[] choices = new Choice[0];
    protected JTable voteTable;
    protected int select;
    private JButton prePage;
    private JButton nextPage;
    private int page;

    private final Object noticeLock = new Object();

    public VotePanel() {
        votePanel = new JPanel();
        votePanel.setLayout(new BorderLayout());
        changePagePanel = new JPanel();
        votePanel.add(changePagePanel, BorderLayout.SOUTH);
        // (1)使用表格来组织形式
        String[] columnName = {"标题", "时间", "发布者"};
        // 使用表格模型
        voteTable = new JTable(new AbstractTableModel() {
            public String getColumnName(int column) {
                return columnName[column];
            }

            public int getRowCount() {
                return votes.length;
            }

            public int getColumnCount() {
                return columnName.length;
            }

            public Object getValueAt(int row, int col) {
                synchronized (noticeLock) {
                    return votes[row].getByIndex(col);
                }
            }
        });
        // 表格显示UI的设置
        voteTable.setShowVerticalLines(false);
        voteTable.setRowHeight(64);
        votePanel.add(voteTable.getTableHeader(), BorderLayout.NORTH);
        votePanel.add(voteTable, BorderLayout.CENTER);
        // id列的样式设置,使其与表头宽度适配且不可手动调整
        TableColumn idColumn = voteTable.getColumnModel().getColumn(0);
        idColumn.sizeWidthToFit();
        idColumn.setResizable(false);
        votePanel.updateUI();
        // (2)用两个按钮去操作这个表格：上一页，下一页
        prePage = new JButton("上一页");
        nextPage = new JButton("下一页");
        prePage.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nextPage.setCursor(new Cursor(Cursor.HAND_CURSOR));
        changePagePanel.add(prePage);
        changePagePanel.add(nextPage);
        // 记载当前页码
        page = 0;
        // 功能1：获得投票列表，分为进门加载，上一页加载和下一页加载
        prePage.setEnabled(false);
        // 进门先加载一页
        getVotes(0);
        // 两个按钮的相关事件
        nextPage.addActionListener(e -> getVotes(page + 1));
        prePage.addActionListener(e -> getVotes(page - 1));
        // 功能2：开始投票：双击表格元素以开始投票
        voteTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    select = voteTable.getSelectedRow();
                    showVote(voteTable.getSelectedRow(), votePanel);
                }
            }
        });
    }

    protected void getVotes(int newPage) {
        new Thread(() -> {
            Map<String, Object> getNoticesRequest = new HashMap<>();
            getNoticesRequest.put("page", newPage);
            getNoticesRequest.put("size", 10);
            try {
                JSONObject notice = URLCommunicate.get("http://121.250.216.117:8080/vote/getVoteList", getNoticesRequest);
                JSONObject data = notice.getJSONObject("data");
                JSONObject temp;
                JSONArray resultArray = data.getJSONArray("data");
                int len = resultArray.length();
                if (len == 0) {
                    JOptionPane.showMessageDialog(getPanel(), "已经没有更多需要投票的事项啦！");
                    return;
                } else {
                    synchronized (noticeLock) {
                        votes = new Vote[len];
                        for (int i = 0; i < len; i++) {
                            temp = new JSONObject(resultArray.getString(i));
                            votes[i] = new Vote();
                            votes[i].setContent(temp.getString("content"));
                            votes[i].setAnnouncer(temp.getLong("announcer"));
                            votes[i].setAnnouncerName(temp.getString("announcerName"));
                            votes[i].setVoteId(temp.getInt("voteId"));
                            votes[i].setTitle();
                            votes[i].setTime(DataTypeChange.time2String(temp.getLong("time")));
                            votes[i].setStatus(temp.getLong("status"));
                        }
                    }
                }
                voteTable.updateUI();
                page = newPage;
                // 上一页可以使用的判断标准
                prePage.setEnabled(page > 0);
            } catch (IOException exception) {
                exception.printStackTrace();
                JOptionPane.showMessageDialog(getPanel(), "无法连接到服务器，获取投票事项失败！");
            } catch (JSONException exception) {
                exception.printStackTrace();
                JOptionPane.showMessageDialog(getPanel(), "无法解析服务器的投票事项信息！");
            }
        }).start();
    }

    private void showVote(int row, JComponent jComponent) {
        new Thread(() -> {
            Map<String, Object> output = new HashMap<>();
            output.put("voteId", votes[row].getVoteId());
            try {
                JSONObject input = URLCommunicate.get("http://121.250.216.117:8080/vote/getVote", output);
                JSONObject data = input.getJSONObject("data");
                // 在这里获取评论并储存在comments数组中
                JSONArray commentJSON = data.getJSONArray("comments");
                int comLen = commentJSON.length();
                comments = new String[comLen];
                for (int i = 0; i < comLen; i++) {
                    comments[i] = commentJSON.getString(i);
                }
                // 在这里获取选项并显示保存到choices数组中
                JSONArray choicesJSON = data.getJSONArray("choices");
                int choLen = choicesJSON.length();
                choices = new Choice[choLen];
                for (int i = 0; i < choLen; i++) {
                    choices[i] = new Choice();
                    choices[i].setChoiceId(choicesJSON.getJSONObject(i).getLong("choiceId"));
                    choices[i].setChoiceContent(choicesJSON.getJSONObject(i).getString("content"));
                }
                // 在这里使用一个公告窗口来显示
                Map<String, Object> component = new HashMap<>();
                component.put("votes", votes);
                component.put("choices",choices);
                component.put("comments", comments);
                component.put("voteTable", voteTable);
                component.put("voteId", votes[voteTable.getSelectedRow()].getVoteId());
                BaseFrame.showFrame(new ShowVoteFrame(), component);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(jComponent, "服务器连接失败！");
            } catch (JSONException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(jComponent, "获取事项内容失败！");
            }
        }).start();
    }

    @Override
    public JPanel getPanel() {
        return votePanel;
    }
}
