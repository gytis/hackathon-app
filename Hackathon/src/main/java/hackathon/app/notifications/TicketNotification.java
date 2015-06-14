package hackathon.app.notifications;

/**
 * Created by Maxim on 13/06/2015.
 */

import android.app.PendingIntent;
import android.content.Context;
import android.app.Notification;
import android.app.NotificationManager;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import hackathon.app.dao.Event;
import hackathon.app.dao.Ticket;
import hackathon.app.dao.User;


public class TicketNotification extends AsyncTask<NotificationServiceListener, Void, Void> {

    private List<Notification> notifications = new ArrayList<Notification>();
    private Context context;
    private Class targetContext;
    private NotificationManager notificationManager;
    private volatile boolean running = false;
    private static TicketNotification instance = new TicketNotification();

    private TicketNotification() {
        super();
    }

    public static TicketNotification service() {
        return instance;
    }

    public synchronized void start(Context applicationContext, Class targetContext, NotificationServiceListener notificationServiceListener) {
        this.context = applicationContext;
        this.targetContext = targetContext;
        execute(notificationServiceListener);
    }

    public synchronized void stop() {
        running = false;
        cancel(true);
    }

    @Override
    protected void onPreExecute() {
        final String nContext = Context.NOTIFICATION_SERVICE;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    protected Void doInBackground(NotificationServiceListener... args) {
        final NotificationServiceListener n = args[0];
        run(n);
        return null;
    }

    private void run(NotificationServiceListener n) {
        List<Ticket> list;
        while (running) {
            list = n.getTickets();

            for (Ticket t : list) {
                if (isRecent(t.getCreatedAt().getTime(), 60)) {
                    Event event = n.getEvent(t.getId());
                    User user = n.getUser(t.getId());

                    final String message = createMessage("Name", "EventName", "Date", "Date created");

                    fireNotification(message);
                }
            }
            delay(3 * 1000);
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

    private void fireNotification(String message) {
        final String notTitle = "New event!";
        Intent intent = new Intent(context, targetContext);

        Bundle b = new Bundle();
        b.putString("key", "message");

        intent.putExtra("key", notTitle);

        Notification n = new Notification.Builder(context)
                .setContentTitle(notTitle)
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setContentText("Ticket")
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(context, 0, intent, 0)).build();

        notificationManager.notify(1, n);
    }


}
