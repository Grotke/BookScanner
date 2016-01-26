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

import com.josephcmontgomery.bookscanner.Database.Database;

public class BookListFragment extends Fragment {

    private ListView listView;
    private final int BOOK_EDIT_REQUEST = 1;

    public BookListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.booklist_fragment, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listView == null) {
            setUpListView();
        }
        listView.setAdapter(null);
        Cursor dataResults = Database.getAllBooks(getContext());
        if (dataResults.getCount() != 0) {
            listView.setAdapter(new DataCursorAdapter(getActivity(), dataResults, DataCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER));
        }
    }

    private void setUpListView(){
        listView = (ListView) getView().findViewById(R.id.data_list_view);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent bookEditIntent = new Intent(getActivity(), BookEditSwipeActivity.class);
                        bookEditIntent.putExtra("position", position);
                        startActivityForResult(bookEditIntent, BOOK_EDIT_REQUEST);
                    }
                }
        );
    }
}
