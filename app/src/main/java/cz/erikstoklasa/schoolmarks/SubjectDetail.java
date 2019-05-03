package cz.erikstoklasa.schoolmarks;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import cz.erikstoklasa.schoolmarks.data.SchoolContract;

public class SubjectDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_detail);
        final LineChart chart = (LineChart) findViewById(R.id.chart);
        List<Entry> entries = new ArrayList<Entry>();
        Intent i = getIntent();
        String name = i.getStringExtra("name");
        final String selection = SchoolContract.SubjectEntry.COLUMN_NAME+"=?";
        final String selectionArgs[] = {name};
        Cursor cursor = this.getContentResolver().query(SchoolContract.SubjectEntry.CONTENT_URI, null, selection, selectionArgs, null);
        cursor.moveToFirst();
        float avg = Float.parseFloat(cursor.getString(cursor.getColumnIndexOrThrow(SchoolContract.SubjectEntry.COLUMN_AVERAGE)).replace(",", "."));
        String teacher = cursor.getString(cursor.getColumnIndexOrThrow(SchoolContract.SubjectEntry.COLUMN_TEACHER));
        String marksBundle = cursor.getString(cursor.getColumnIndexOrThrow(SchoolContract.SubjectEntry.COLUMN_MARKS));
        cursor.close();
        String marksArray[] = marksBundle.split(";");
        String[] eachMark;
        ArrayList<Mark> marksList = new ArrayList<>();
        ListView listview = (ListView) findViewById(R.id.listView);
        int index = 0;
        for (String a : marksArray) {
            eachMark = a.split(",");
            index += 1;
            entries.add(new Entry(index, Float.parseFloat(eachMark[0])));
            marksList.add(new Mark(Integer.parseInt(eachMark[0]), eachMark[1], eachMark[2]));
        }
        LineDataSet dataSet = new LineDataSet(entries, "");
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(ColorTemplate.rgb("3f51b5"));
        dataSet.setFillAlpha(500);
        dataSet.setCircleColor(ColorTemplate.rgb("ffc107"));
        Description d = new Description();
        chart.setDrawBorders(false);

        d.setEnabled(false);
        chart.setDescription(d);
        dataSet.setCircleRadius(7f);
        dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        dataSet.setDrawCircleHole(false);
        dataSet.setLineWidth(0f);
        chart.getXAxis().setDrawAxisLine(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setDrawLabels(false);
        chart.getAxisLeft().setDrawLabels(false);
        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.setMinOffset(0);
        chart.setExtraOffsets(15,15,15,0);
        chart.getAxisRight().setDrawLabels(false);
        chart.getLegend().setEnabled(false);
        chart.setDoubleTapToZoomEnabled(false);
        LineData lineData = new LineData(dataSet);
        lineData.setValueTextSize(18f);
        lineData.setValueTypeface(Typeface.DEFAULT_BOLD);
        lineData.setHighlightEnabled(false);
        lineData.setValueFormatter(new MyValueFormatter());
        chart.setNoDataText(":( Žádné známky");
        chart.setData(lineData);
        final MarkAdapter markAdapter = new MarkAdapter(this, marksList);
        listview.setAdapter(markAdapter);
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setSubtitle(teacher);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        final TextView avgTextView = (TextView) findViewById(R.id.subjectAvg);
        final GradientDrawable avgCircle = (GradientDrawable) avgTextView.getBackground();
        avgCircle.setColor(getMarkColor(avg));
        avgTextView.setText(String.format("%.2f", avg));
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swrl);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                chart.invalidate();
                final Snackbar updated = Snackbar.make(findViewById(R.id.swrl), R.string.resresh_success, Snackbar.LENGTH_SHORT);
                Cursor cursor = getApplicationContext().getContentResolver().query(SchoolContract.SubjectEntry.CONTENT_URI, null, selection, selectionArgs, null);
                cursor.moveToFirst();
                float avg = Float.parseFloat(cursor.getString(cursor.getColumnIndexOrThrow(SchoolContract.SubjectEntry.COLUMN_AVERAGE)).replace(",", "."));
                avgCircle.setColor(getMarkColor(avg));
                avgTextView.setText(String.format("%.2f", avg));
                String marksBundle = cursor.getString(cursor.getColumnIndexOrThrow(SchoolContract.SubjectEntry.COLUMN_MARKS));
                cursor.close();
                String marksArray[] = marksBundle.split(";");
                String[] eachMark;
                markAdapter.clear();
                for (String a : marksArray) {
                    eachMark = a.split(",");
                    markAdapter.add(new Mark(Integer.parseInt(eachMark[0]), eachMark[1], eachMark[2]));
                }
                markAdapter.notifyDataSetChanged();
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        updated.show();
                    }
                },400);
            }
        });
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
        return ContextCompat.getColor(this, markColorResourceId);
    }
}
