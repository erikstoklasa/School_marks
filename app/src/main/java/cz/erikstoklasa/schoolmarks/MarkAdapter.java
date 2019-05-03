package cz.erikstoklasa.schoolmarks;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MarkAdapter extends ArrayAdapter<Mark> {

    public MarkAdapter(Activity context, ArrayList<Mark> marks) {
        super(context, 0, marks);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        Mark currentSubject = getItem(position);
        TextView descriptionTextView = (TextView) listItemView.findViewById(R.id.subject_name);
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.subject_teacher);
        TextView markTextView = (TextView) listItemView.findViewById(R.id.subject_avg);
            descriptionTextView.setText(currentSubject.getMarkDescription());
            dateTextView.setText(currentSubject.getDate());
            markTextView.setText(String.format("%.1f", currentSubject.getMark()));
            GradientDrawable markCircle = (GradientDrawable) markTextView.getBackground();
            markCircle.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        return listItemView;
    }
}