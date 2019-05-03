package cz.erikstoklasa.schoolmarks;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import cz.erikstoklasa.schoolmarks.data.SchoolContract.SubjectEntry;

public class SubjectsFragment extends Fragment {
    public SubjectsFragment() {
    }
    Cursor cursor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_subjects, container, false);
        String[] projection = {
                SubjectEntry._ID,
                SubjectEntry.COLUMN_AVERAGE,
                SubjectEntry.COLUMN_NAME,
                SubjectEntry.COLUMN_MARKS,
                SubjectEntry.COLUMN_TEACHER};
        cursor = getActivity().getContentResolver().query(
                SubjectEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
        final SubjectCursorAdapter subjectAdapter = new SubjectCursorAdapter(getContext(), cursor);
        ListView listview = (ListView) rootView.findViewById(R.id.list);
        listview.setAdapter(subjectAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent subjectDetailIntent = new Intent(getActivity(), SubjectDetail.class);
                subjectDetailIntent.putExtra("name", cursor.getString(cursor.getColumnIndexOrThrow(SubjectEntry.COLUMN_NAME)));
                startActivity(subjectDetailIntent);
            }
        });
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) rootView;
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                final Snackbar updated = Snackbar.make(rootView, R.string.resresh_success, Snackbar.LENGTH_SHORT);
                subjectAdapter.notifyDataSetChanged();
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        updated.show();
                    }
                },400);
            }
        });
        return rootView;

    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {

            //((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.subjects));
        }
    }

}
