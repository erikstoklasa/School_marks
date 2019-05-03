package cz.erikstoklasa.schoolmarks;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cz.erikstoklasa.schoolmarks.data.SchoolContract.SubjectEntry;

public class DashboardFragment extends Fragment {
    //Root view of the fragment
    private View rootView;
    // Required blank constructor
    public DashboardFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflating the root view
        rootView = inflater.inflate(R.layout.activity_dashboard, container, false);
        //Projections
        String[] columnsA = {SubjectEntry.COLUMN_AVERAGE};
        String[] columnsD = {SubjectEntry.COLUMN_AVERAGE, SubjectEntry.COLUMN_NAME, SubjectEntry.COLUMN_TEACHER};
        //Cursor for the total average card
        Cursor cursorA = getContext().getContentResolver().query(SubjectEntry.CONTENT_URI, columnsA, null, null, SubjectEntry.COLUMN_AVERAGE+" ASC");
        float sum = 0;
        float avg = 0;
        try{
            while (cursorA.moveToNext()) {
                //Replacing the , with .
                sum += Float.parseFloat(cursorA.getString(0).replace(",", "."));
            }
            avg = sum / cursorA.getCount();
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }finally {
                cursorA.close();
        }

        //Referring to TextViews
        TextView wavgTextView = rootView.findViewById(R.id.subjects_worst_avg);
        TextView avgTextView = rootView.findViewById(R.id.subjects_avg);
        TextView teacherTextView = rootView.findViewById(R.id.subjects_worst_avg_teacher);
        TextView nameTextView = rootView.findViewById(R.id.subjects_worst_avg_name);
        //Referring to GradientDrawables
        GradientDrawable wavgCircle = (GradientDrawable) wavgTextView.getBackground();
        GradientDrawable avgCircle = (GradientDrawable) avgTextView.getBackground();
        //Referring to CardViews
        CardView worstAvgCard = rootView.findViewById(R.id.worst_avg_card);
        CardView totalAvgCard = rootView.findViewById(R.id.total_avg_card);
        Cursor cursorD = getContext().getContentResolver().query(SubjectEntry.CONTENT_URI, columnsD, null, null, SubjectEntry.COLUMN_AVERAGE+" DESC");
        try{
            cursorD.moveToFirst();
            //Don't want to display the worst average card when no data is in the database
            if(cursorD.getCount() != 0){
                //Replacing the , with .
                float wavg = Float.parseFloat(cursorD.getString(0).replace(",", "."));
                final String subjectName = cursorD.getString(1);
                String subjectTeacherName = cursorD.getString(2);
                //Starting a new activity on click
                worstAvgCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent subjectDetailIntent = new Intent(getActivity(), SubjectDetail.class);
                        subjectDetailIntent.putExtra("name", subjectName);
                        startActivity(subjectDetailIntent);
                    }
                });
                nameTextView.setText(subjectName);
                teacherTextView.setText(subjectTeacherName);
                wavgTextView.setText(String.format("%.2f",wavg));
                wavgCircle.setColor(getMarkColor(wavg));
            }else{
                ((ViewGroup) worstAvgCard.getParent()).removeView(worstAvgCard);
            }
        }finally {
            cursorD.close();
        }

        if(cursorA.getCount() != 0){
            avgTextView.setText(String.format("%.2f",avg));
            avgCircle.setColor(getMarkColor(avg));
        }else{
            TextView a = new TextView(getContext());
            a.setText(getString(R.string.no_subjects_found));
            ((ViewGroup) totalAvgCard.getParent()).addView(a);
            ((ViewGroup) totalAvgCard.getParent()).removeView(totalAvgCard);
        }
        return rootView;
    }
    //Title of the tab
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && isResumed()) {
            //((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.dashboard));
        }
   }
   //Returning a color resource id based on the mark
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
        return ContextCompat.getColor(getContext(), markColorResourceId);
    }
}
