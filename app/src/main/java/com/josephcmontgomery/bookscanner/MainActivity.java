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
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

//TODO: Handle database with non-isbn barcodes. Inform user of book not found. Add barcode library.
//TODO: Adjust for different screen sizes. Deal with no internet connection.
//TODO: Figure out activity result fail error. Figure out error on exiting app.
//TODO: Guard against SQL injection. Change database to persist.
//TODO: Check security issues for mobile apps.
//TODO: Get API key for Google Books. Check how it handles a lot of requests.
//TODO: Profile performance on memory and cpu, and download size.
//TODO: Add way to manually add book. Add way to add location and view all books scanned.
//TODO: Make sure back button doesn't take to previous screens on book location editing screen.
public class MainActivity extends AppCompatActivity{
    private ArrayList<BookInformation> books;
    private Button scanBtn, viewBtn, editBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        scanBtn = (Button)findViewById(R.id.scan_button);
        viewBtn = (Button)findViewById(R.id.view_books_button);
        editBtn = (Button)findViewById(R.id.edit_button);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.scan_button){
                    books = new ArrayList<BookInformation>();
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
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.edit_button){
                    Intent intent = new Intent(MainActivity.this, BookEditActivity.class);
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
            if(resultCode == RESULT_OK) {
                String scanContent = scanningResult.getContents();
                BookInformation book = new BookInformation();
                book.isbn = scanContent;
                books.add(book);
                IntentIntegrator scanIntegrator = new IntentIntegrator(MainActivity.this);
                scanIntegrator.initiateScan();
            }
            else if(resultCode == RESULT_CANCELED){
                new GetBookByISBN().execute(books);
            }
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

    private class GetBookByISBN extends AsyncTask<ArrayList<BookInformation>,Void,Void>{
        protected Void doInBackground(ArrayList<BookInformation>... books) {
            InputStream is;
            for(int i = 0; i < books[0].size(); i++) {
                try {
                    BookInformation book = books[0].get(i);
                    String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + book.isbn;
                    is = getBookSearchResults(url);
                    if (readJsonStream(is, book)) {
                        Database.insertBook(book, MainActivity.this.getApplicationContext());
                    }
                } catch (Exception e) {
                    if (e.getMessage() != null) {
                        Log.e("EXCEPTION", e.getMessage());
                    }
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

        private boolean readJsonStream(InputStream in, BookInformation book) throws Exception {
            JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            try {
                return BookJsonInterpreter.processSearchResult(reader, book);
            } finally {
                reader.close();
            }
        }
    }
}
