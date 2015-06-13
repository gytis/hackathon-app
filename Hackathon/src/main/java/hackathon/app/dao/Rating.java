package hackathon.app.dao;

/**
 * Created by Maxim on 13/06/2015.
 */
public class Rating {

    private long id;
    private long userId;
    private long eventId;
    private int value;

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setValue(int value) {
        this.value = value;
    }


    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public long getEventId() {
        return eventId;
    }

    public int getValue() {
        return value;
    }


}
