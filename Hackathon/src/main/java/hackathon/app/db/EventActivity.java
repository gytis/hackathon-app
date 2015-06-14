package hackathon.app.db;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;

import java.util.List;
import java.util.ListIterator;

import hackathon.app.MainActivity;
import hackathon.app.R;
import hackathon.app.dao.Event;
import hackathon.app.dao.EventDao;

public class EventActivity extends Activity {

    private long _eventId;
    /*
    Create a bundle for an intent

    Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
    Bundle b = new Bundle();
    b.putInt("key", 1); //Your id
    intent.putExtras(b); //Put your id to your next Intent
    startActivity(intent);
    finish();
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        if (AccessToken.getCurrentAccessToken() == null) {
            startActivity(new Intent(this, MainActivity.class));
        }

        Bundle b = getIntent().getExtras();
        _eventId = b.getLong("eventId");
        fetchEvent(_eventId);

        setContentView(R.layout.activity_event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        FacebookSdk.sdkInitialize(getApplicationContext());

        if (AccessToken.getCurrentAccessToken() == null) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void fetchEvent(final long id) {
        new AsyncTask<Void, Void, List<Event>>() {
            @Override
            protected List<Event> doInBackground(Void... voids) {
                return new EventDao().getEvents();
            }

            @Override
            protected void onPostExecute(List<Event> events) {
                ListIterator<Event> iter = events.listIterator();
                Event event = null;
                while(iter.hasNext()) {
                    event = iter.next();
                    if (event.getId() == id) {
                        break;
                    }
                }
                populateEventData(event);
            }
        }.execute();
    }

    public void confirmAttendance(View btn) {
        int id = 1; //dummy user ID
        
    }

    private void populateEventData(Event event) {
        final TextView eventDescription = (TextView) findViewById(R.id.eventDescription);
        eventDescription.setText(Html.fromHtml(event.getDescription()));

        final TextView eventLocation = (TextView) findViewById(R.id.eventLocation);
        eventLocation.setText(event.getStreet() + ", " + event.getTown() + ", " + event.getPostcode());

    }
}
