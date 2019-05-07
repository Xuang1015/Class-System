package bean;

public class ChatMember {
    private String name;
    private long roomId;
    private long username;
    private int authority;
    private String state = "离线";

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void changeState(String state) {
        if (state.equals("离线")) {
            this.state = "在线";
        } else {
            this.state = "离线";
        }
    }

    public long getUsername() {
        return username;
    }

    public void setUsername(long username) {
        this.username = username;
    }

    public int getAuthority() {
        return authority;
    }

    public void setAuthority(int authority) {
        this.authority = authority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return name + "(" + state + ")";
    }

    public String toMyString() {
        return "ChatMember{" +
                "name='" + name + '\'' +
                ", roomId=" + roomId +
                ", username=" + username +
                ", authority=" + authority +
                ", state='" + state + '\'' +
                '}';
    }
}
