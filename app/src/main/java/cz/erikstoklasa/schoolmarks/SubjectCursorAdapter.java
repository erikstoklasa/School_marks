package cz.erikstoklasa.schoolmarks;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cz.erikstoklasa.schoolmarks.data.SchoolContract.SubjectEntry;

public class SubjectCursorAdapter extends CursorAdapter {

    public SubjectCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }
    String name;
    String teacher;
    float avg;
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = (TextView) view.findViewById(R.id.subject_name);
        TextView teacherTextView = (TextView) view.findViewById(R.id.subject_teacher);
        TextView avgTextView = (TextView) view.findViewById(R.id.subject_avg);
        name = cursor.getString(cursor.getColumnIndexOrThrow(SubjectEntry.COLUMN_NAME));
        teacher = cursor.getString(cursor.getColumnIndexOrThrow(SubjectEntry.COLUMN_TEACHER));
        avg = Float.parseFloat(cursor.getString(cursor.getColumnIndexOrThrow(SubjectEntry.COLUMN_AVERAGE)).replace(",", "."));
        GradientDrawable avgCircle = (GradientDrawable) avgTextView.getBackground();
        avgCircle.setColor(getMarkColor(avg));
        nameTextView.setText(name);
        teacherTextView.setText(teacher);
        avgTextView.setText(String.format("%.1f", avg));
        name = "";
        teacher = "";
        avg = 0f;
    }
    private int getMarkColor(float mark) {
        int markColorResourceId;
        int markFloor = Math.round(mark);
        switch (markFloor) {
            case 0:
            case 1:
                markColorResourceId = R.color.mark1;
                break;
            case 2:
                markColorResourceId = R.color.mark2;
                break;
            case 3:
                markColorResourceId = R.color.mark3;
                break;
            case 4:
                markColorResourceId = R.color.mark4;
                break;
            default:
                markColorResourceId = R.color.mark5;
                break;
        }
        return ContextCompat.getColor(mContext, markColorResourceId);
    }
}
