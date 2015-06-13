package hackathon.app.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class RecordDao {

    private final HackathonTestDbHelper dbHelper;

    public RecordDao(final HackathonTestDbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void insert(final String title) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        final ContentValues values = new ContentValues();
        values.put(HackathonTestContract.RecordEntity.COLUMN_NAME_TITLE, title);

        db.insert(HackathonTestContract.RecordEntity.TABLE_NAME, null, values);
    }

    public List<Record> getAll() {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final String [] projection = {
                HackathonTestContract.RecordEntity._ID,
                HackathonTestContract.RecordEntity.COLUMN_NAME_TITLE
        };
        final String sortOrder = HackathonTestContract.RecordEntity._ID + " DESC";
        final Cursor cursor = db.query(HackathonTestContract.RecordEntity.TABLE_NAME, projection, null, null, null, null, sortOrder);
        final List<Record> records = new ArrayList<Record>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            final long id = cursor.getLong(cursor.getColumnIndexOrThrow(HackathonTestContract.RecordEntity._ID));
            final String title = cursor.getString(cursor.getColumnIndexOrThrow(HackathonTestContract.RecordEntity.COLUMN_NAME_TITLE));

            records.add(new Record(id, title));
            cursor.moveToNext();
        }

        return records;
    }

}
