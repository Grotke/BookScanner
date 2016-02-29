package com.josephcmontgomery.bookscanner;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import com.josephcmontgomery.bookscanner.Tools.BookInformation;
import com.josephcmontgomery.bookscanner.Tools.BookJsonParser;
import com.josephcmontgomery.bookscanner.Tools.InternetMonitor;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TaskFragment extends Fragment{

    interface TaskListener{
        void onPreExecute();
        void onPostExecute(BookInformation book);
    }
    private TaskListener callback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (TaskListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement TaskListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    public void launchBookTask(String isbn){
        if(InternetMonitor.isConnected(getContext())) {
            new GetBookByISBN().execute(isbn);
        }
        else{
            CharSequence text = "Can't get book. No Internet.";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(getContext(), text, duration);
            toast.show();
        }
    }

    private class GetBookByISBN extends AsyncTask<String,Void,BookInformation> {
        @Override
        protected void onPreExecute(){
            if(callback != null) {
                callback.onPreExecute();
            }
        }
        protected BookInformation doInBackground(String... isbns) {
            InputStream inStream;
            BookInformation book = null;
            for(String isbn: isbns) {
                try {
                    String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn +"&key=" + DeveloperKey.DEVELOPER_KEY;
                    inStream = getBookSearchResults(url);
                    book = parseJsonStream(inStream, isbn);
                } catch (Exception e) {
                    if (e.getMessage() != null) {
                        Log.d("EXCEPTION", e.getMessage());
                    }
                    book = new BookInformation();
                }
            }
            return book;
        }

        @Override
        protected void onPostExecute(BookInformation book) {
            if(callback != null) {
                callback.onPostExecute(book);
            }
        }

        private BookInformation parseJsonStream(InputStream inStream, String isbn) throws Exception {
            JsonReader reader = new JsonReader(new InputStreamReader(inStream, "UTF-8"));
            try {
                return BookJsonParser.processSearchResult(reader, isbn);
            }
            finally{
                reader.close();
            }
        }

        private InputStream getBookSearchResults(String inUrl) throws Exception{
            HttpURLConnection conn = setUpHttpConnection(inUrl);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("RESPONSE CODE", "The response is: " + response);
            return conn.getInputStream();
        }

        private HttpURLConnection setUpHttpConnection(String url) throws Exception {
            URL outUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) outUrl.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            return conn;
        }
    }
}
