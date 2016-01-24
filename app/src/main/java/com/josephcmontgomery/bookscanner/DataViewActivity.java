package com.josephcmontgomery.bookscanner;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.josephcmontgomery.bookscanner.Database.BookScannerContract;
import com.josephcmontgomery.bookscanner.Database.Database;
import com.josephcmontgomery.bookscanner.Tools.BookInformation;

public class DataViewActivity extends AppCompatActivity {
    private ListView listView;
    private final int BOOK_EDIT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_view);
        setUpToolbar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(listView == null) {
            setUpListView();
        }
        listView.setAdapter(null);
        Cursor dataResults = Database.getAllBooks(getApplicationContext());
        if (dataResults.getCount() != 0) {
            listView.setAdapter(new DataCursorAdapter(this, dataResults, DataCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER));
        }
    }

    private void setUpToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setUpListView(){
        listView = (ListView) findViewById(R.id.data_list_view);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Cursor cursor = ((DataCursorAdapter) parent.getAdapter()).getCursor();
                        cursor.moveToPosition(position);
                        BookInformation book = packBookFromCursor(cursor);
                        Intent bookEditIntent = new Intent(DataViewActivity.this, BookEditActivity.class);
                        bookEditIntent.putExtra("bookInfo", book);
                        bookEditIntent.putExtra("deleteEnabled", true);
                        startActivityForResult(bookEditIntent, BOOK_EDIT_REQUEST);
                    }
                }

        );
    }
    private BookInformation packBookFromCursor(Cursor cursor){
        BookInformation book = new BookInformation();
        book.title = cursor.getString(cursor.getColumnIndexOrThrow(BookScannerContract.Books.COLUMN_NAME_TITLE));
        book.timeLastUpdated = cursor.getString(cursor.getColumnIndexOrThrow(BookScannerContract.Books.COLUMN_NAME_DATE_SCANNED));
        book.averageRating = cursor.getDouble(cursor.getColumnIndexOrThrow(BookScannerContract.Books.COLUMN_NAME_AVERAGE_RATING));
        book.bookId = cursor.getInt(cursor.getColumnIndexOrThrow(BookScannerContract.Books._ID));
        book.description = cursor.getString(cursor.getColumnIndexOrThrow(BookScannerContract.Books.COLUMN_NAME_DESCRIPTION));
        book.isbn = cursor.getString(cursor.getColumnIndexOrThrow(BookScannerContract.Books.COLUMN_NAME_ISBN));
        book.location = cursor.getString(cursor.getColumnIndexOrThrow(BookScannerContract.Books.COLUMN_NAME_LOCATION));
        book.pageCount = cursor.getInt(cursor.getColumnIndexOrThrow(BookScannerContract.Books.COLUMN_NAME_PAGE_COUNT));
        book.ratingsCount = cursor.getInt(cursor.getColumnIndexOrThrow(BookScannerContract.Books.COLUMN_NAME_RATINGS_COUNT));
        book.imageURL = cursor.getString(cursor.getColumnIndexOrThrow(BookScannerContract.Books.COLUMN_NAME_IMAGE_URL));
        return book;
    }

}
