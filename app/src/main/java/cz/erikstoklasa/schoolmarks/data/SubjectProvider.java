package cz.erikstoklasa.schoolmarks.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import cz.erikstoklasa.schoolmarks.data.SchoolContract.SubjectEntry;
public class SubjectProvider extends ContentProvider {

    public static final String LOG_TAG = SubjectProvider.class.getSimpleName();
    private SubjectDbHelper mDbHelper;
    private static final int SUBJECTS = 100;
    private static final int SUBJECT_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    //TODO Make comments

    static {
        sUriMatcher.addURI(SubjectEntry.CONTENT_AUTHORITY, SubjectEntry.PATH_SUBJECTS, SUBJECTS);
        sUriMatcher.addURI(SubjectEntry.CONTENT_AUTHORITY, SubjectEntry.PATH_SUBJECTS+"/#", SUBJECT_ID);
    }
    @Override
    public boolean onCreate() {
        mDbHelper = new SubjectDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor cursor;
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        switch (match) {
            case SUBJECTS:
                cursor = database.query(SubjectEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case SUBJECT_ID:
                selection = SubjectEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(SubjectEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return cursor;
    }


    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SUBJECTS:
                return insertSubject(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }
    private Uri insertSubject(Uri uri, ContentValues values) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(SubjectEntry.TABLE_NAME, null, values);
        String name = values.getAsString(SubjectEntry.COLUMN_NAME);
        String nameTeacher = values.getAsString(SubjectEntry.COLUMN_TEACHER);
        String marks = values.getAsString(SubjectEntry.COLUMN_MARKS);
        if (name == null) {
            throw new IllegalArgumentException("Subject requires a name");
        } else if (marks == null) {
            throw new IllegalArgumentException("Subject requires marks");
        } else if (nameTeacher == null) {
            throw new IllegalArgumentException("Subject requires a teacher's name");
        }
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SUBJECTS:
                return updateSubject(uri, contentValues, selection, selectionArgs);
            case SUBJECT_ID:
                selection = SubjectEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateSubject(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }
    private int updateSubject(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        return database.update(SubjectEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SUBJECTS:
                return database.delete(SubjectEntry.TABLE_NAME, selection, selectionArgs);
            case SUBJECT_ID:
                selection = SubjectEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return database.delete(SubjectEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SUBJECTS:
                return SubjectEntry.CONTENT_LIST_TYPE;
            case SUBJECT_ID:
                return SubjectEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}