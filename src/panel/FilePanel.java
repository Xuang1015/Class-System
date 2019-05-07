package panel;

import bean.File;
import frame.LoginFrame;
import online.cszt0.cmt.util.FileDialog;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.DataTypeChange;
import util.Iostream;
import util.URLCommunicate;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FilePanel extends Fragment {
    private JPanel filePanel;
    protected JPanel changePagePanel;
    private File[] files;
    private JTable fileTable;
    private JButton nextButton;
    private JButton preButton;
    private int filePage;
    private JButton prePage;
    private JButton nextPage;
    private JButton uploadButton;
    private JButton deleteFileButton;
    private int page;
    private String type;
    private String path;
    private java.io.File downFile;
    private final Object noticeLock = new Object();

    public class MyTableRender implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//            if( value instanceof ImageIcon ){
                JLabel jLabel = new JLabel();
                jLabel.setLayout(new BorderLayout());//设置布局
                jLabel.setIcon((Icon)value);//给jlable设置图片
//            }
            return jLabel;
//            else if( value instanceof File ) {
//                try {
//                    return new JLabel(new ImageIcon(ImageIO.read((File)value)));
//                } catch(IOException ex) {
//                    throw new RuntimeException(ex.getMessage(), ex);
//                }
//            } else {
//                String val = String.valueOf(value);
//                try {
//                    return new JLabel(new ImageIcon(ImageIO.read(new File(val))));
//                } catch(IOException ex) {
//                    throw new RuntimeException(ex.getMessage(), ex);
//                }
//            }
        }
    }

    public FilePanel() {
        filePanel = new JPanel();
        filePanel.setLayout(new BorderLayout());
        changePagePanel = new JPanel();
        filePanel.add(changePagePanel, BorderLayout.SOUTH);
        // (1)使用表格来组织形式（第一行为图标，这里不在表头写出来，用空格表示）
        String[] columnName = {" ", "文件名", "时间", "大小", "发布者"};
        files = new File[0];
        // 使用表格模型
        fileTable = new JTable(new AbstractTableModel() {
            public String getColumnName(int column) {
                return columnName[column];
            }

            public int getRowCount() {
                return files.length;
            }

            public int getColumnCount() {
                return columnName.length;
            }

            public Object getValueAt(int row, int col) {
                synchronized (noticeLock) {
                    return files[row].getByIndex(col);
                }
            }
        });
        fileTable.getColumnModel().getColumn(0).setCellRenderer(new MyTableRender());
        // 表格显示UI的设置
        fileTable.setShowVerticalLines(false);
        fileTable.setRowHeight(64);
        filePanel.add(fileTable.getTableHeader(), BorderLayout.NORTH);
        filePanel.add(fileTable, BorderLayout.CENTER);
        // 图标的样式设置,使其较小不可手动调整
        fileTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        fileTable.getColumnModel().getColumn(1).setPreferredWidth(600);
        fileTable.getColumnModel().getColumn(2).setPreferredWidth(300);
        fileTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        fileTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        fileTable.getTableHeader().setResizingAllowed(false);
        fileTable.getTableHeader().setReorderingAllowed(false);
        filePanel.updateUI();
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
        getFiles(0);
        // 两个按钮的相关事件
        nextPage.addActionListener(e -> getFiles(page + 1));
        prePage.addActionListener(e -> getFiles(page - 1));
        // 功能2：上传文件：使用上传按钮
        uploadButton = new JButton("上传文件");
        uploadButton.addActionListener(e -> uploadFile());
        changePagePanel.add(uploadButton);
        // 功能3：删除文件：只有管理员或者上传者有这个权限
        deleteFileButton = new JButton("删除文件");
        changePagePanel.add(deleteFileButton);
        deleteFileButton.setEnabled(false);
        deleteFileButton.addActionListener(e -> deleteFile());
        fileTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (files[fileTable.getSelectedRow()].getUploader() == LoginFrame.username) {
                    deleteFileButton.setEnabled(true);
                    deleteFileButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                } else {
                    deleteFileButton.setEnabled(false);
                    deleteFileButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });
        // 功能4：下载文件：双击表格元素以获得文件下载地址，再次确认开始下载
        fileTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    getDownloadPath(fileTable.getSelectedRow());
                    int value = JOptionPane.showConfirmDialog(fileTable,"确认要下载此文件吗？","请确认",JOptionPane.YES_NO_OPTION);
                    if (value == JOptionPane.YES_OPTION) {
                        downloadFile(path);
                    }
                }
            }
        });
    }

    private void getFiles(int newPage) {
        new Thread(() -> {
            Map<String, Object> getFileRequest = new HashMap<>();
            getFileRequest.put("page", newPage);
            getFileRequest.put("size", 10);
            try {
                JSONObject file = URLCommunicate.get("http://121.250.216.117:8080/file/fileList", getFileRequest);
                JSONObject data = file.getJSONObject("data");
                System.out.println(data);
                JSONObject temp;
                JSONArray resultArray = data.getJSONArray("data");
                int len = resultArray.length();
                if (len == 0) {
                    JOptionPane.showMessageDialog(getPanel(), "已经没有更多文件啦！");
                    return;
                } else {
                    synchronized (noticeLock) {
                        files = new File[len];
                        for (int i = 0; i < len; i++) {
                            temp = new JSONObject(resultArray.getString(i));
                            files[i] = new File();
                            files[i].setFileName(temp.getString("name"));
                            files[i].setTime(DataTypeChange.time2String(temp.getLong("time")));
                            files[i].setSize(DataTypeChange.size2String(temp.getLong("size")));
                            files[i].setUploaderName(temp.getString("uploaderName"));
                            files[i].setFileId(temp.getLong("fileId"));
                            files[i].setUploader(temp.getLong("uploader"));
                        }
                        giveIcon(files);
                    }
                }
                fileTable.updateUI();
                page = newPage;
                // 上一页可以使用的判断标准
                prePage.setEnabled(page > 0);
            } catch (IOException | JSONException exception) {
                exception.printStackTrace();
            }
        }).start();
    }

    private void giveIcon(File[] files1) {
        files1 = files;
        for (File file: files1) {
            type = file.getFileName().substring(file.getFileName().lastIndexOf("."));
            switch (type) {
                // 文档
                case ".txt":
                case ".doc":
                case ".docx":
                case ".text":
                case ".dot":
                case ".md":
                    file.setTypeIcon(new ImageIcon("resource/icon/doc.png"));
                    break;
                // 电子书
                case ".pdf":
                    file.setTypeIcon(new ImageIcon("resource/icon/pdf.png"));
                    break;
                // 压缩文件
                case ".zip":
                case ".rar":
                case ".jar":
                case ".tar":
                case ".7z":
                    file.setTypeIcon(new ImageIcon("resource/icon/zip.png"));
                    break;
                // 图片
                case ".png":
                case ".jpg":
                case ".jpeg":
                case ".gif":
                    file.setTypeIcon(new ImageIcon("resource/icon/jpg.png"));
                    break;
                // 视频
                case ".mp4":
                case ".avi":
                case ".mpg":
                case ".mkv":
                    file.setTypeIcon(new ImageIcon("resource/icon/avi.png"));
                    break;
                // 音频
                case ".mp3":
                case ".wav":
                case ".aif":
                    file.setTypeIcon(new ImageIcon("resource/icon/mp3.png"));
                    break;
                // 代码
                case ".java":
                case ".html":
                case ".css":
                case ".cpp":
                case ".h":
                case ".c":
                case ".php":
                    file.setTypeIcon(new ImageIcon("resource/icon/code.png"));
                    break;
                // 表格
                case ".xlsx":
                case ".xls":
                case ".xlsm":
                case ".xlsb":
                    file.setTypeIcon(new ImageIcon("resource/icon/xls.png"));
                    break;
                // 幻灯片
                case ".ppt":
                case ".pptx":
                    file.setTypeIcon(new ImageIcon("resource/icon/ppt.png"));
                    break;
                // 可执行文件
                case ".exe":
                case ".cmd":
                case ".bat":
                case ".sys":
                    file.setTypeIcon(new ImageIcon("resource/icon/exe.png"));
                    break;
            }
        }
    }

    private void getDownloadPath(int row) {
        new Thread(()->{
            Map<String, Object> output = new HashMap<>();
            output.put("fileId", files[row].getFileId());
            try {
                JSONObject input = URLCommunicate.get("http://121.250.216.117:8080/file/filePath", output);
                if (input.getInt("code") == -1) {
                    JOptionPane.showMessageDialog(filePanel,"该文件不存在！");
                } else {
                    JSONObject data = input.getJSONObject("data");
                    path = data.getString("filePath");
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void downloadFile(String path1) {
        new Thread(()->{
            Map<String, Object> output = new HashMap<>();
            output.put("filePath", path1);
            try {
                FileDialog fileDialog = FileDialog.getPlatInstance();
                String savePath = fileDialog.showSaveDialog();
                if (savePath == null) {
                    return;
                }
                Iostream.output(savePath, URLCommunicate.getFile("http://121.250.216.117:8080/file/download",output));
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void uploadFile() {
        new Thread(()->{
            FileDialog fileDialog = FileDialog.getPlatInstance();
            String upPath = fileDialog.showOpenDialog();
            if (upPath == null) {
                return;
            }
            java.io.File file = new java.io.File(upPath);
            Map<String, Object> output = new HashMap<>();
            output.put("username", LoginFrame.username);
            output.put("fileName",file.getName());
            try {
                URLCommunicate.postFile("http://121.250.216.117:8080/file/upload", output, file);
                getFiles(0);
                fileTable.updateUI();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    protected void deleteFile() {
        Map<String, Object> output = new HashMap<>();
        output.put("fileId", files[fileTable.getSelectedRow()].getFileId());
        output.put("username", LoginFrame.username);
        try {
            URLCommunicate.post("http://121.250.216.117:8080/file/delete", output);
            getFiles(0);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public JPanel getPanel() {
        return filePanel;
    }
}
