package hackathon.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class CurrentUserHolder extends SQLiteOpenHelper {

    public static final String TABLE_CURRENT_USER = "current_user";
    public static final String COLUMN_USER_ID = "user_id";

    private static final String DATABASE_NAME = "current_user.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_CURRENT_USER + "(" + COLUMN_USER_ID
            + " string primary key);";

    private String[] allColumns = { COLUMN_USER_ID };

    public CurrentUserHolder(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public long getCurrentUserId() {
        Cursor cursor = getReadableDatabase().query(TABLE_CURRENT_USER, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        if (cursor.isAfterLast()) {
            return -1;
        }

        return cursor.getLong(0);
    }

    public void setCurrentUserId(final long id) {
        getWritableDatabase().delete(TABLE_CURRENT_USER, null, null);

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, id);

        getWritableDatabase().insert(TABLE_CURRENT_USER, null, values);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURRENT_USER);
        onCreate(db);
    }
}
