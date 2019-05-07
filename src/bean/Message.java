package bean;

import java.util.Objects;

public class Message {
    private long time;
    private String name;
    private long username;
    private String msg;
    private int messageType;
    private long rommId;

    @Override
    public String toString() {
        return "Message{" +
                "time=" + time +
                ", name='" + name + '\'' +
                ", username=" + username +
                ", msg='" + msg + '\'' +
                ", messageType=" + messageType +
                ", rommId=" + rommId +
                '}';
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public long getRommId() {
        return rommId;
    }

    public void setRommId(long rommId) {
        this.rommId = rommId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUsername() {
        return username;
    }

    public void setUsername(long username) {
        this.username = username;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
