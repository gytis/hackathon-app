package hackathon.app.db;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import hackathon.app.R;

public class EventActivity extends Activity {

    private int _eventId;
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
        Bundle b = getIntent().getExtras();
        _eventId = b.getInt("eventId");
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
}
