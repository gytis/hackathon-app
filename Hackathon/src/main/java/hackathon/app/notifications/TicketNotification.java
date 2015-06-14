package hackathon.app.notifications;

/**
 * Created by Maxim on 13/06/2015.
 */

import android.app.PendingIntent;
import android.content.Context;
import android.app.Notification;
import android.app.NotificationManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import hackathon.app.CurrentUserHolder;
import hackathon.app.dao.Event;
import hackathon.app.dao.EventDao;
import hackathon.app.dao.Ticket;
import hackathon.app.dao.TicketDao;
import hackathon.app.dao.User;
import hackathon.app.dao.UserDao;


public class TicketNotification {

    private List<Notification> notifications = new ArrayList<Notification>();
    private Context context;
    private Class targetActivity;
    private NotificationManager notificationManager;
    private volatile boolean running = false;
    private static TicketNotification instance = new TicketNotification();
    private UserDao userDao;
    public static String message = "Your friend %s is going to %s on %s at %s!";

    private TicketNotification() {
        super();
    }

    public static TicketNotification service() {
        return instance;
    }

    public void start(Context applicationContext, Class targetActivity, UserDao userDao) {
        this.userDao = userDao;
        this.running = true;
        this.context = applicationContext;
        this.targetActivity = targetActivity;
    }

    public synchronized void stop() {
        running = false;
    }

    /*@Override
    protected void onPreExecute() {
        running = true;
        final String nContext = Context.NOTIFICATION_SERVICE;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    protected Void doInBackground(NotificationServiceListener... args) {
        final NotificationServiceListener n = args[0];
        run(n);
        return null;
    }*/

    public List<Ticket> getTickets() {
        return TicketDao.getTickets();
    }

    public User getUser(long id) {
        final Long userId = new CurrentUserHolder(context).getCurrentUserId();
        for (final User user : userDao.getUsers()) {
            if (user.getId() == userId) {
                return user;
            }
        }

        return null;
    }

    public Event getEvent(long id) {
        final EventDao eventDao = new EventDao();

        for (final Event event : eventDao.getEvents()) {
            if (event.getId() == id) {
                return event;
            }
        }

        return null;
    }

    public void run() {
        List<Ticket> list;
        while (running) {
            list = getTickets();

            for (Ticket t : list) {
                if (true/*isRecent(t.getCreatedAt().getTime(), 60)*/) {
                    Event event = getEvent(t.getId());
                    if (event == null) continue;

                    User user = getUser(t.getId());
                    if (user == null) continue;

                    DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
                    String date = df.format(t.getDate());
                    String dateCreated = df.format(t.getCreatedAt());

                    final String message = createMessage(user.getName(), event.getName(), date, dateCreated);

                    fireNotification(message, event.getId());
                }
            }
            try {
                Thread.sleep(10000);
            } catch(InterruptedException e) {}
        }
    }

    private boolean isRecent(long time, int timeSpan) { // minutes
        return System.currentTimeMillis() - time <= timeSpan * 60 * 1000;
    }

    private String createMessage(String userName, String eventName, String date, String time) {
        return String.format(NotificationServiceListener.message, userName, eventName, date, time);
    }

    private void delay(int timeDelay) { // delay in millis
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start <= timeDelay)
            try {
                Thread.sleep(1);
            } catch(InterruptedException e) {}
    }

    private void fireNotification(String message, long eventId) {
        final String notTitle = "New event!";
        Intent intent = new Intent(context, targetActivity);

        Bundle b = new Bundle();
        b.putLong("eventId", eventId);
        intent.putExtras(b);

        Notification n = new Notification.Builder(context)
                .setContentTitle(notTitle)
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(context, 0, intent, 0)).build();

        notificationManager.notify(1, n);
    }


}