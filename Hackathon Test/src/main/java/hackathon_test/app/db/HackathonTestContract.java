package hackathon_test.app.db;

import android.provider.BaseColumns;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class HackathonTestContract {

    private HackathonTestContract() {

    }

    public static abstract class RecordEntity implements BaseColumns {
        public static final String TABLE_NAME = "record";
        public static final String COLUMN_NAME_TITLE = "title";
    }

}
