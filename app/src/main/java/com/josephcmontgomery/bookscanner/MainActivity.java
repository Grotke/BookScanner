package com.josephcmontgomery.bookscanner;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity{
    private Button scanBtn, viewBtn;
    private TextView formatTxt, contentTxt;
    private BookInformation book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        scanBtn = (Button)findViewById(R.id.scan_button);
        viewBtn = (Button)findViewById(R.id.view_books_button);
        formatTxt = (TextView)findViewById(R.id.scan_format);
        contentTxt = (TextView)findViewById(R.id.scan_content);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.scan_button){
                    IntentIntegrator scanIntegrator = new IntentIntegrator(MainActivity.this);
                    scanIntegrator.initiateScan();
                }
            }
        });
        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.view_books_button){
                    Intent intent = new Intent(MainActivity.this, DataViewActivity.class);
                    startActivity(intent);
                }
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if(scanningResult != null){
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + scanContent;
            book = new BookInformation();
            book.isbn = scanContent;
            formatTxt.setText("FORMAT: " + scanFormat);
            contentTxt.setText("CONTENT: " + scanContent);
            new GetBookByISBN().execute(book);
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class GetBookByISBN extends AsyncTask<BookInformation,Void,Void>{
        protected Void doInBackground(BookInformation... books) {
            InputStream is;
            try {
                String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + books[0].isbn;
                is = getBookSearchResults(url);
                readJsonStream(is, books[0]);
                if(books[0] != null){
                    Log.e("BOOK", books[0].toString());
                    Database.insertBook(books[0], MainActivity.this.getApplicationContext());
                }
            } catch (Exception e) {
                if(e.getMessage() != null) {
                    Log.e("EXCEPTION", e.getMessage());
                }
            }
            return null;
        }


        private InputStream getBookSearchResults(String inUrl) throws Exception{
            URL outUrl = new URL(inUrl);
            HttpURLConnection conn = (HttpURLConnection) outUrl.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("RESPONSE CODE", "The response is: " + response);
            return conn.getInputStream();
        }

        private void readJsonStream(InputStream in, BookInformation book) throws Exception {
            JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            try {
                BookJsonInterpreter.processSearchResult(reader, book);
            } finally {
                reader.close();
            }
        }
    }
}
