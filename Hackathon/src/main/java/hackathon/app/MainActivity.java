package hackathon.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.facebook.*;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import hackathon.app.db.EventActivity;
import hackathon.app.event.EventsActivity;

public class MainActivity extends Activity {

    private CallbackManager callbackManager;

    private Intent mainActivityIntent;

    private Intent eventsActivityIntent;

    private TokenTracker tokenTracker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        eventsActivityIntent = new Intent(this, EventsActivity.class);
        mainActivityIntent = new Intent(this, MainActivity.class);
        callbackManager = CallbackManager.Factory.create();
        tokenTracker = new TokenTracker();

        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.registerCallback(callbackManager, new FacebookLoginCallback());
        startActivity(eventsActivityIntent);
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

            /*new FacebookDao().getUserInfo(new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(final JSONObject jsonObject, final GraphResponse graphResponse) {
                    new AsyncTask<Void, Void, List<User>>() {
                        @Override
                        protected List<User> doInBackground(Void... voids) {
                            return new UserDao().getUsers();
                        }

                        @Override
                        protected void onPostExecute(List<User> users) {
                            super.onPostExecute(users);
                            Log.v("MainActivity", jsonObject.toString());
//                            for (final User : users) {
//                                if ()
//                            }
                        }
                    }.execute();
                }
            });*/


            /*startActivity(eventsActivityIntent);*/

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
