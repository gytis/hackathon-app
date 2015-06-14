package hackathon.app;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.hardware.usb.UsbRequest;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.facebook.*;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import hackathon.app.dao.Event;
import hackathon.app.dao.EventDao;
import hackathon.app.dao.Ticket;
import hackathon.app.dao.TicketDao;
import hackathon.app.dao.User;
import hackathon.app.dao.UserDao;
import hackathon.app.db.EventActivity;
import hackathon.app.event.EventsActivity;
import hackathon.app.facebook.FacebookService;
import hackathon.app.notifications.NotificationServiceListener;
import hackathon.app.notifications.TicketNotification;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import hackathon.app.notifications.TicketNotification;

public class MainActivity extends Activity {

    private CallbackManager callbackManager;

    private Intent mainActivityIntent;

    private Intent eventsActivityIntent;

    private TokenTracker tokenTracker;

    private UserDao userDao;

    private final FacebookService facebookService = new FacebookService();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        userDao = new UserDao(getApplicationContext());
        eventsActivityIntent = new Intent(this, EventsActivity.class);
        mainActivityIntent = new Intent(this, MainActivity.class);
        callbackManager = CallbackManager.Factory.create();
        tokenTracker = new TokenTracker();

        if (AccessToken.getCurrentAccessToken() != null) {
            startActivity(eventsActivityIntent);
        }

        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.registerCallback(callbackManager, new FacebookLoginCallback());

        try {
            //fireNotificationService();
        } catch (Exception e) {
            //Log.v("NOTIFICATION EXCEPTION: ", e.getMessage());
        }
    }

    private void fireNotificationService() {
        TicketNotification.service().start(getApplicationContext(), EventActivity.class, new NotificationServiceListener() {
            @Override
            public List<Ticket> getTickets() {
                return TicketDao.getTickets();
            }

            @Override
            public User getUser(long id) {
                final Long userId = new CurrentUserHolder(getApplicationContext()).getCurrentUserId();
                for (final User user : userDao.getUsers()) {
                    if (user.getId() == userId) {
                        return user;
                    }
                }

                return null;
            }

            @Override
            public Event getEvent(long id) {
                final EventDao eventDao = new EventDao();

                for (final Event event : eventDao.getEvents()) {
                    if (event.getId() == id) {
                        return event;
                    }
                }

                return null;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tokenTracker.stopTracking();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private class FacebookLoginCallback implements FacebookCallback<LoginResult> {

        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.v("Facebook Login", "success");
            Boolean test;

            facebookService.getUserInfo(new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                    try {
                        //checks if user is registered (inside the method)
                        Log.v("MainActivity", jsonObject.toString());
                        userDao.registerUser(jsonObject.getString("id"), jsonObject.getString("name"));
                    } catch (JSONException e) {
                        Log.v("MainActivity", e.getMessage());
                    }
                }
            });

            // TODO check if registered
            // TODO register
            // TODO upload friends
            // TODO redirect
        }

        @Override
        public void onCancel() {
            Log.v("Facebook Login", "cancel");
        }

        @Override
        public void onError(FacebookException exception) {
            Log.v("Facebook Login", exception.getMessage());
        }
    }

    private class TokenTracker extends AccessTokenTracker {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                Log.v("MainActivity", "Access token set to null");
                new CurrentUserHolder(getApplicationContext()).setCurrentUserId(-1);
                startActivity(mainActivityIntent);
            } else {
                Log.v("MainActivity", "Access token set to " + currentAccessToken);
                startActivity(eventsActivityIntent);
            }
        }
    }

    public void testEvent(View caller) {
        Intent eventView = new Intent(this, EventActivity.class);
        Bundle b = new Bundle();
        b.putInt("eventId", 1); //Your id
        eventView.putExtras(b); //Put your id to your next Intent
        startActivity(eventView);
        finish();
    }
}
