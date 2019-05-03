package cz.erikstoklasa.schoolmarks.data;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import cz.erikstoklasa.schoolmarks.data.SchoolContract.SubjectEntry;

public class SubjectDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SchoolDatabase.db";
    //TODO Make comments

    public SubjectDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    String SQL_CREATE_ENTRIES =
                    "CREATE TABLE " + SubjectEntry.TABLE_NAME + " (" +
                    SubjectEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SubjectEntry.COLUMN_NAME + " TEXT," +
                    SubjectEntry.COLUMN_TEACHER + " TEXT," +
                    SubjectEntry.COLUMN_AVERAGE + " TEXT," +
                    SubjectEntry.COLUMN_MARKS + " TEXT);";

    String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SubjectEntry.TABLE_NAME;
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        Log.v(SubjectDbHelper.class.getSimpleName(),SQL_CREATE_ENTRIES);
    }
}
