package cz.erikstoklasa.schoolmarks;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import cz.erikstoklasa.schoolmarks.data.SchoolContract;

public class loginActivity extends AppCompatActivity {
    //TODO Make comments
    String[][] marks ={
            {"5,18.září 2017,Lineární rovnice", "3,19.září 2017,Thalethova kružnice", "5,19.září 2017,Thalethova kružnice", "2,20.září 2017,Slovní úlohy", "1,21.září 2017,Výrazy", "1,25.září 2017,Rozložení na součin", "1,25.září 2017,Druhá mocnina na součin + použití vzorce při rozkladu", "5,28.září 2017,Práce v hodině", "1,28.září 2017,Domácí úkol"},
            {"1,28.září 2017,Západní Evropa", "3,28.září 2017,Střední Evropa"},
            {"5,28.září 2017,Horopis", "2,28.září 2017,Západní Čechy"},
            {"3,28.září 2017,Postava", "4,28.září 2017,Obličej", "1,28.září 2017,Mandaly"},
            {"3,28.září 2017,Microsoft Word", "1,28.září 2017,Microsoft PowerPoint", "1,28.září 2017,Microsoft Excel"},
            {"5,28.září 2017,60m sprint", "3,28.září 2017,1500m"},
            {"1,28.září 2017,Fázová slovesa", "1,28.září 2017,Slovíčka"},
            {"5,28.září 2017,Koncovky", "4,28.září 2017,Předložky"},
            {"1,28.září 2017,Present perfect", "1,28.září 2017,Prepositions"} };
    String[][] subjects = { {"Maths", "Geography", "Global education", "Arts", "ICT", "P.E.",
            "German","ÚDJ", "English"},
            {"Nechvátalová", "Peška", "Peška", "Šípová", "Včelák", "Florián", "Šípová", "Kolář",
                    "Vostřáková"} };
    int index = 0;
    String[] eachMark;
    Intent loginIntent;
    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        loginIntent = new Intent(this, MainActivity.class);
        Button loginBtn = (Button) findViewById(R.id.loginBtn);
        final EditText pswdTextView = (EditText) findViewById(R.id.password);
        final EditText loginTextView = (EditText) findViewById(R.id.login);
        final CheckBox rememberBox = (CheckBox) findViewById(R.id.rememberMe);
        pswdTextView.setTypeface(Typeface.DEFAULT);
        pswdTextView.setTransformationMethod(new PasswordTransformationMethod());
        Button insert = (Button) findViewById(R.id.setDummyData);
        Button info = (Button) findViewById(R.id.info_button);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDialog wpd = new InfoDialog();
                wpd.show(getFragmentManager(), "info");
            }
        });
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    getApplicationContext().getContentResolver().query(SchoolContract.SubjectEntry.CONTENT_URI, null, null, null, null, null);
                    getApplicationContext().getContentResolver().delete(SchoolContract.SubjectEntry.CONTENT_URI, null, null);
                }finally{
                    index = 0;
                }
                for (String j[] : marks) {
                    String name = subjects[0][index];
                    String teacherName = subjects[1][index];
                    String marksBundleRow = "";
                    for (String k : j) {
                        marksBundleRow += k + ";";
                    }
                    String marksArray[] = marksBundleRow.split(";");
                    float avg = 0f;
                    int sum = 0;
                    for (String a : marksArray) {
                        eachMark = a.split(",");
                        sum += Integer.parseInt(eachMark[0]);
                    }
                    avg = (float) sum / marksArray.length;
                    ContentValues values = new ContentValues();
                    values.put(SchoolContract.SubjectEntry.COLUMN_NAME, name);
                    values.put(SchoolContract.SubjectEntry.COLUMN_TEACHER, teacherName);
                    values.put(SchoolContract.SubjectEntry.COLUMN_MARKS, marksBundleRow);
                    values.put(SchoolContract.SubjectEntry.COLUMN_AVERAGE, String.format("%.2f", Float.valueOf(avg)));

                    // Insert the new row, returning the primary key value of the new row
                    getApplicationContext().getContentResolver().insert(SchoolContract.SubjectEntry.CONTENT_URI, values);
                    index++;
                }
                startActivity(loginIntent);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(loginTextView.getText().toString().trim().equals("")) && !(pswdTextView.getText().toString().trim().equals(""))){
                    if(Login.login(loginTextView.getText().toString().trim(), pswdTextView.getText().toString().trim(), rememberBox.isChecked())){

                        getApplicationContext().getContentResolver().delete(SchoolContract.SubjectEntry.CONTENT_URI, null, null);
                        pswdTextView.setText("");
                        startActivity(loginIntent);
                        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.success_login), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else{
                        WrongPasswordDialog wpd = new WrongPasswordDialog();
                        wpd.show(getFragmentManager(), "wrongpassword");
                    }
                }else if (loginTextView.getText().toString().trim().equals(""))
                    loginTextView.setError(getString(R.string.error_field_required));
                else if (pswdTextView.getText().toString().trim().equals(""))
                    pswdTextView.setError(getString(R.string.error_field_required));
            }
        });
    }


}
