package hackathon.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;

public class MainActivity extends Activity {

    private CallbackManager callbackManager;

    private AccessTokenTracker accessTokenTracker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Log.v("MainActivity", "access token changed oldAccessToken=" + oldAccessToken + ", currentAccessToken=" + currentAccessToken);

                if (currentAccessToken == null) {
                    final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    final Intent intent = new Intent(getApplicationContext(), RecordsActivity.class);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v("MainActivity", "Activity result. requestCode=" + requestCode + ", resultCode="
                + resultCode + ", data=" + data);

        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        Log.v("MainActivity", "Destroy access token");

        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

}
