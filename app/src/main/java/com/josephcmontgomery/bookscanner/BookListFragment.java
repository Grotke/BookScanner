package com.josephcmontgomery.bookscanner;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.josephcmontgomery.bookscanner.Database.BookScannerContract;
import com.josephcmontgomery.bookscanner.Database.Database;
import com.josephcmontgomery.bookscanner.Tools.BookInformation;

public class BookListFragment extends Fragment {

    private ListView listView;
    private final int BOOK_EDIT_REQUEST = 1;

    public BookListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_data_view, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listView == null) {
            setUpListView();
        }
        listView.setAdapter(null);
        Cursor dataResults = Database.getAllBooks(getActivity().getApplicationContext());
        if (dataResults.getCount() != 0) {
            listView.setAdapter(new DataCursorAdapter(getActivity(), dataResults, DataCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER));
        }
    }

    private void setUpListView(){
        listView = (ListView) getActivity().findViewById(R.id.data_list_view);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Cursor cursor = ((DataCursorAdapter) parent.getAdapter()).getCursor();
                        cursor.moveToPosition(position);
                        BookInformation book = packBookFromCursor(cursor);
                        Intent bookEditIntent = new Intent(getActivity(), BookEditActivity.class);
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
