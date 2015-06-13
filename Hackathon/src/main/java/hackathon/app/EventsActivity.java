package hackathon.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import hackathon.app.dao.FacebookDao;
import hackathon.app.dao.User;
import hackathon.app.dao.UserDao;
import org.json.JSONObject;

import java.util.List;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class EventsActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_events);

        Button getUserInfoButton = (Button) findViewById(R.id.getUserInfoButton);
        getUserInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView view = (TextView) findViewById(R.id.userInfoTextView);
                view.setText("Kuku");
                Log.v("aaa", "click");
//                new FacebookDao().getUserInfo(new GraphRequest.GraphJSONObjectCallback() {
//                    @Override
//                    public void onCompleted(final JSONObject jsonObject, final GraphResponse graphResponse) {
//                        Log.v("aaa", "got fb data" + jsonObject.toString());
//                        new AsyncTask<Void, Void, List<User>>() {
//                            @Override
//                            protected List<User> doInBackground(Void... voids) {
//                                return new UserDao().getUsers();
//                            }
//
//                            @Override
//                            protected void onPostExecute(List<User> users) {
//                                super.onPostExecute(users);
//                                Log.v("MainActivity", jsonObject.toString());
////                            for (final User : users) {
////                                if ()
////                            }
//                            }
//                        }.execute();
//                    }
//                });
            }
        });
    }

}
