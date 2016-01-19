package com.josephcmontgomery.bookscanner;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.josephcmontgomery.bookscanner.Database.Database;

public class DataViewActivity extends AppCompatActivity {
    private ListView listView;
    private final int NO_FLAGS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_view);

        setUpToolbar();

        listView = (ListView) findViewById(R.id.data_list_view);
        Cursor dataResults = Database.getAllBooks(getApplicationContext());
        if(dataResults.getCount() != 0) {
            listView.setAdapter(new DataCursorAdapter(this, dataResults, NO_FLAGS));
        }
    }

    private void setUpToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

}
