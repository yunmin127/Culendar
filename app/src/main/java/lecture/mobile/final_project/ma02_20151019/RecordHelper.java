package lecture.mobile.final_project.ma02_20151019;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class RecordHelper extends SQLiteOpenHelper {
    final static String TAG = "RecordHelper";
    final static String DB_NAME = "record.db";
    public final static String TABLE_NAME = "record_table";
    public final static String COL_ID = "_id";
    public final static String COL_MONTH = "month";
    public final static String COL_DAY = "day";
    public final static String COL_TITLE = "title";
    public final static String COL_AUTHOR = "author";
    public final static String COL_LINE = "line";

    public RecordHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" + COL_ID + " integer primary key autoincrement, " + COL_MONTH + " NUMBER, " + COL_DAY + " NUMBER, "
                + COL_TITLE + " TEXT, " + COL_AUTHOR + " TEXT, " + COL_LINE + " NUMBER)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}