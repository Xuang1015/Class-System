package bean;

public class Room {
    private long rooomId;
    private String roomTitle;

    public long getRooomId() {
        return rooomId;
    }

    public void setRooomId(long rooomId) {
        this.rooomId = rooomId;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    @Override
    public String toString() {
        return roomTitle;
    }
}
