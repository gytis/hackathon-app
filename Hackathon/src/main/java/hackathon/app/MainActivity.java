package hackathon.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends Activity {

    private CallbackManager callbackManager;

    private Intent eventsActivityIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        eventsActivityIntent = new Intent(this, EventsActivity.class);

        if (AccessToken.getCurrentAccessToken() != null) {
            Log.v(this.getClass().getName(), "Facebook token exists. Redirecting");
            startActivity(eventsActivityIntent);
        } else {
            Log.v(this.getClass().getName(), "Facebook token does not exist. Need to log in");
        }

        callbackManager = CallbackManager.Factory.create();

        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.registerCallback(callbackManager, new FacebookLoginCallback());
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if (AccessToken.getCurrentAccessToken() != null) {
            Log.v(this.getClass().getName(), "Facebook token exists. Redirecting");
            startActivity(eventsActivityIntent);
        } else {
            Log.v(this.getClass().getName(), "Facebook token does not exist. Need to log in");
        }
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        if (AccessToken.getCurrentAccessToken() != null) {
            Log.v(this.getClass().getName(), "Facebook token exists. Redirecting");
            startActivity(eventsActivityIntent);
        } else {
            Log.v(this.getClass().getName(), "Facebook token does not exist. Need to log in");
        }
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
            startActivity(eventsActivityIntent);

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

}
