package cz.erikstoklasa.schoolmarks.data;
import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class SchoolContract {
    //TODO Make comments
    public static class SubjectEntry implements BaseColumns {
        public static final String TABLE_NAME = "subjects";
        public static final String COLUMN_NAME = "subject_name";
        public static final String COLUMN_TEACHER = "teacher_name";
        public static final String COLUMN_MARKS = "marks";
        public static final String COLUMN_AVERAGE = "average";
        public static final String _ID = BaseColumns._ID;
        public static final String CONTENT_AUTHORITY = "cz.erikstoklasa.schoolmarks";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        public static final String PATH_SUBJECTS = "subjects";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SUBJECTS);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SUBJECTS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SUBJECTS;
    }
}
