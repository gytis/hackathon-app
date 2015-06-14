package hackathon.app.notifications;

import java.util.List;

import hackathon.app.dao.Event;
import hackathon.app.dao.Ticket;
import hackathon.app.dao.User;

/**
 * Created by Maxim on 13/06/2015.
 */
public interface NotificationServiceListener {
    public static String message = "Your friend %s is going to %s on %s at %s!";

    public List<Ticket> getTickets();
    public User getUser(long id);
    public Event getEvent(long id);

}
