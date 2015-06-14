package hackathon.app.dao;

import android.text.format.Time;

import java.util.Date;

/**
 * Created by Maxim on 13/06/2015.
 */
public class Ticket {
    private long id;
    private long userId;
    private long eventId;
    private Date date;
    private Date createdAt;

    public void setId(long id) {
        this.id = id;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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

    public Date getDate() {
        return date;
    }

    public Date getCreatedAt() {
        return createdAt;
    }


}
