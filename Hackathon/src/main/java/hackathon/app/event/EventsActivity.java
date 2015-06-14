package hackathon.app.event;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import com.facebook.FacebookSdk;
import hackathon.app.dao.Event;
import hackathon.app.dao.EventDao;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class EventsActivity extends ListActivity {

    private final ArrayList<String> listViewData = new ArrayList<String>();
    private EventListAdapter eventListAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        this.eventListAdapter = new EventListAdapter(this, listViewData);
        setListAdapter(this.eventListAdapter);
        final EventDao eventDao = new EventDao();

        new AsyncTask<Void, Void, List<Event>>() {
            @Override
            protected List<Event> doInBackground(Void... params) {
                return eventDao.getEvents();
            }

            protected void onPostExecute(List<Event> result) {
                final ArrayList<String> eventNames = new ArrayList<String>();
                for (Event event: result) {
                    eventNames.add(event.getName());
                    // Limit hack
                    int count = 0;
                    if (count > 50) {
                        break;
                    }
                    count++;
                }
                eventListAdapter.addDataToList(eventNames);
            }
        }.execute();
    }

}
