package bean;

public class Vote {
    private String content;
    private String title;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle() {
        if (content.length()<10) {
            title = content;
            return;
        }
        this.title = this.content.substring(0,10) + "……";
    }

    private long announcer;
    private String announcerName;
    private long voteId;
    private String time;
    private long status;

    public long getAnnouncer() {
        return announcer;
    }

    public void setAnnouncer(long announcer) {
        this.announcer = announcer;
    }

    public String getAnnouncerName() {
        return announcerName;
    }

    public void setAnnouncerName(String announcerName) {
        this.announcerName = announcerName;
    }

    public long getVoteId() {
        return voteId;
    }

    public void setVoteId(long voteId) {
        this.voteId = voteId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public Object getByIndex(int index) {
        switch (index) {
            case 0:
                return title;
            case 1:
                return time;
            case 2:
                return announcerName;
            default:
                throw new IndexOutOfBoundsException();
        }
    }
}
