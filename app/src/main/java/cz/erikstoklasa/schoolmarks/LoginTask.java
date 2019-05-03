package cz.erikstoklasa.schoolmarks;

import android.os.AsyncTask;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public class LoginTask extends AsyncTask<String, Void, String[]>{
    //TODO Think it over
    @Override
    protected String[] doInBackground(String... strings) {
        return strings;
    }

    @Override
    protected void onPostExecute(String[] strings) {
        super.onPostExecute(strings);
    }
}
