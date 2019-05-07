package bean;

public class Notice {
    private Integer id;
    private String announcer;
    private String title;
    private String time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAnnouncer() {
        return announcer;
    }

    public void setAnnouncer(String announcer) {
        this.announcer = announcer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Object getByIndex(int index) {
        switch (index) {
            case 0:
                return title;
            case 1:
                return time;
            case 2:
                return announcer;
            case 3:
                return id;
            default:
                throw new IndexOutOfBoundsException();
        }
    }
}
