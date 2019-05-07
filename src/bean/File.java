package bean;

import javax.swing.*;

public class File {
    private ImageIcon typeIcon;
    private String fileName;
    private String time;
    private String size;
    private String uploaderName;
    private long fileId;
    private long uploader;

    public long getUploader() {
        return uploader;
    }

    public void setUploader(long uploader) {
        this.uploader = uploader;
    }

    public long getFileId() {
        return fileId;
    }

    public void setFileId(long fileId) {
        this.fileId = fileId;
    }

    public ImageIcon getTypeIcon() {
        return typeIcon;
    }

    public void setTypeIcon(ImageIcon typeIcon) {
        this.typeIcon = typeIcon;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUploaderName() {
        return uploaderName;
    }

    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }

    public Object getByIndex(int index) {
        switch (index) {
            case 0:
                return typeIcon;
            case 1:
                return fileName;
            case 2:
                return time;
            case 3:
                return size;
            case 4:
                return uploaderName;
            case 5:
                return fileId;
            case 6:
                return uploader;
            default:
                throw new IndexOutOfBoundsException();
        }
    }
}
