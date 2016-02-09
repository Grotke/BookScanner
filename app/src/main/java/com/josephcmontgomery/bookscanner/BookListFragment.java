package com.josephcmontgomery.bookscanner;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.josephcmontgomery.bookscanner.Database.Database;
import com.josephcmontgomery.bookscanner.Tools.BookInformation;

import java.util.ArrayList;

public class BookListFragment extends Fragment {
    private OnBookListListener callback;
    private ListView listView;

    public interface OnBookListListener{
        void onBookSelected(int position);
        void onBookCountAvailable(int bookCount);
    }

    public BookListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            callback = (OnBookListListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnBookListListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.booklist_fragment, container, false);
    }

    @Override
    public void onResume() {
        if (listView == null) {
            setUpListView();
        }
        listView.setAdapter(null);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        setDataSource();
        super.onResume();
    }

    private void setDataSource(){
        int count;
        Bundle bundle = getArguments();
        if(bundle != null && bundle.containsKey("books")){
            ArrayList<BookInformation> books = (ArrayList<BookInformation>) bundle.getSerializable("books");
            BookListAdapter adapter = new BookListAdapter(getContext(), R.layout.list_item, books);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            count = adapter.getCount();
        }
        else {
            Cursor cursor = Database.getAllBooks(getActivity().getApplicationContext());
            BookListCursorAdapter dataAdapter = new BookListCursorAdapter(getActivity(), cursor, BookListCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            listView.setAdapter(dataAdapter);
            dataAdapter.notifyDataSetChanged();
            count = dataAdapter.getCount();
        }
        callback.onBookCountAvailable(count);
    }

    private void setUpListView(){
        listView = (ListView) getView().findViewById(R.id.data_list_view);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        callback.onBookSelected(position);
                    }
                }
        );
    }
}
