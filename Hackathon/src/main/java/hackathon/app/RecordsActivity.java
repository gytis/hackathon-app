package hackathon.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import hackathon.app.R;
import hackathon.app.db.HackathonTestDbHelper;
import hackathon.app.db.Record;
import hackathon.app.db.RecordDao;

public class RecordsActivity extends Activity {

    private RecordDao recordDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        recordDao = new RecordDao(new HackathonTestDbHelper(getApplicationContext()));

        updateRecords();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void addRecord(final View view) {
        Log.v("MainActivity", "Adding new record");

        final EditText input = (EditText) findViewById(R.id.recordTitle);
        recordDao.insert(input.getText().toString());
        updateRecords();
    }

    public void updateRecords() {
        Log.v("MainActivity", "Updating records");

        final TableLayout tableLayout = (TableLayout) findViewById(R.id.records);
        tableLayout.removeAllViews();

        for (final Record record : recordDao.getAll()) {
            final TextView textView = new TextView(this);
            final TableRow tableRow = new TableRow(this);

            textView.setText(record.getTitle());
            tableRow.addView(textView);
            tableLayout.addView(tableRow);
        }
    }
}
