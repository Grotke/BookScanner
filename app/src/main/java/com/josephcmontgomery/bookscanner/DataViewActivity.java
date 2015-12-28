package com.josephcmontgomery.bookscanner;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

public class DataViewActivity extends AppCompatActivity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.data_list_view);
            Cursor dataResults = Database.getAllBooks(getApplicationContext());
        if(dataResults.getCount() != 0) {
            listView.setAdapter(new DataCursorAdapter(this, dataResults, 0));
            /* String[] columns = new String[]{BookScannerContract.Books.COLUMN_NAME_TITLE, BookScannerContract.Books.COLUMN_NAME_SUBTITLE};
            int[] to = new int[]{R.id.book_title_entry, R.id.book_location_entry};
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.list_item, dataResults, columns, to, 0);
            listView.setAdapter(adapter);*/
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
